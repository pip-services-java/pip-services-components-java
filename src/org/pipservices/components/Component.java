package org.pipservices.components;

import org.pipservices.commons.config.*;
import org.pipservices.components.count.CompositeCounters;
import org.pipservices.commons.errors.ConfigException;
import org.pipservices.components.log.CompositeLogger;
import org.pipservices.commons.refer.*;

/**
 * Abstract component that supportes configurable dependencies, logging
 * and performance counters.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>dependencies:
 *   <ul>
 *   <li>[dependency name 1]: Dependency 1 locator (descriptor)
 *   <li>...
 *   <li>[dependency name N]: Dependency N locator (descriptor)
 * 	 </ul>
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:counters:*:*:1.0       (optional) {@link org.pipservices.components.count.ICounters} components to pass collected measurements
 * <li>*:logger:*:*:1.0         (optional) {@link org.pipservices.components.log.ILogger} components to pass log messages
 * <li>...                      References must match configured dependencies.
 * </ul>
 */
public class Component implements IConfigurable, IReferenceable {
	
	protected DependencyResolver _dependencyResolver = new DependencyResolver();
    protected CompositeLogger _logger = new CompositeLogger();
    protected CompositeCounters _counters = new CompositeCounters();

    /**
     * Configures component by passing configuration parameters.
     * 
     * @param config    configuration parameters to be set.
     */
	public void configure(ConfigParams config) throws ConfigException {
		_dependencyResolver.configure(config);
        _logger.configure(config);	
	}
    
	/**
	 * Sets references to dependent components.
	 * 
	 * @param references 	references to locate the component dependencies. 
     */
	public void setReferences(IReferences references) throws ReferenceException {
		_dependencyResolver.setReferences(references);
        _logger.setReferences(references);
        _counters.setReferences(references);	
	}

	

}
