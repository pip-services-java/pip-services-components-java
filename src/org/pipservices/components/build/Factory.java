package org.pipservices.components.build;

import java.util.*;

import org.pipservices.commons.reflect.*;
import org.pipservices.commons.refer.*;

/**
 * Basic component factory that creates components using registered types and factory functions.
 * <p>
 * #### Example ###
 * <pre>
 * {@code
 * Factory factory = new Factory();
 * 
 * factory.registerAsType(
 * 		new Descriptor("mygroup", "mycomponent1", "default", "*", "1.0"),
 * 		MyComponent1
 * );
 * factory.register(
 * 		new Descriptor("mygroup", "mycomponent2", "default", "*", "1.0"),
 * 		(locator) => {
 * 			return new MyComponent2();
 * 		}
 * );
 * 
 * factory.create(new Descriptor("mygroup", "mycomponent1", "default", "name1", "1.0"))
 * factory.create(new Descriptor("mygroup", "mycomponent2", "default", "name2", "1.0"))
 * }
 * </pre>
 * @see Descriptor
 * @see IFactory
 */
public class Factory implements IFactory {

	public interface IComponentFactory {
		Object create(Object locator) throws Exception;
	}

	private class DefaultComponentFactory implements IComponentFactory {
		private Class<?> _type;

		public DefaultComponentFactory(Class<?> type) {
			_type = type;
		}

		public Object create(Object locator) throws Exception {
			return TypeReflector.createInstanceByType(_type);
		}
	}

	private class Registration {
		private Object _locator;
		private IComponentFactory _factory;

		public Registration(Object locator, IComponentFactory factory) {
			this._locator = locator;
			this._factory = factory;
		}

		public Object getLocator() {
			return _locator;
		}

		public IComponentFactory getFactory() {
			return _factory;
		}
	}

	private List<Registration> _registrations = new ArrayList<Registration>();

	/**
	 * Registers a component using a factory method.
	 * 
	 * @param locator a locator to identify component to be created.
	 * @param factory a factory function that receives a locator and returns a
	 *                created component.
	 */
	public void register(Object locator, IComponentFactory factory) {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		if (factory == null)
			throw new NullPointerException("Factory cannot be null");

		_registrations.add(new Registration(locator, factory));
	}

	/**
	 * Registers a component using its type (a constructor function).
	 * 
	 * @param locator a locator to identify component to be created.
	 * @param type    a component type.
	 */
	public void registerAsType(Object locator, Class<?> type) {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		if (type == null)
			throw new NullPointerException("Type cannot be null");

		IComponentFactory factory = new DefaultComponentFactory(type);
		_registrations.add(new Registration(locator, factory));
	}

	/**
	 * Checks if this factory is able to create component by given locator.
	 * 
	 * This method searches for all registered components and returns a locator for
	 * component it is able to create that matches the given locator. If the factory
	 * is not able to create a requested component is returns null.
	 * 
	 * @param locator a locator to identify component to be created.
	 * @return a locator for a component that the factory is able to create.
	 */
	@Override
	public Object canCreate(Object locator) {
		for (Registration registration : _registrations) {
			Object thisLocator = registration.getLocator();
			if (thisLocator.equals(locator))
				return thisLocator;
		}
		return null;
	}

	/**
	 * Creates a component identified by given locator.
	 * 
	 * @param locator a locator to identify component to be created.
	 * @return the created component.
	 * 
	 * @throws CreateException if the factory is not able to create the component.
	 */
	@Override
	public Object create(Object locator) throws CreateException {
		for (Registration registration : _registrations) {
			if (registration.getLocator().equals(locator)) {
				try {
					return registration.getFactory().create(locator);
				} catch (Exception ex) {
					if (ex instanceof CreateException)
						throw (CreateException) ex;

					throw (CreateException) new CreateException(null, "Failed to create object for " + locator)
							.withCause(ex);
				}
			}
		}
		return null;
	}

}
