package org.pipservices.components.build;

import org.pipservices.commons.errors.*;

/**
 * Error raised when factory is not able to create requested component.
 * 
 * @see ApplicationException
 * @see InternalException
 */
public class CreateException extends InternalException {
	private static final long serialVersionUID = 2506495188126378894L;

	/**
	 * Creates an error instance.
	 */
	public CreateException() {
		this(null, (String)null);
	}

	/**
	 * Creates an error instance and assigns its values.
	 * 
     * @param correlationId    (optional) a unique transaction id to trace execution through call chain.
     * @param locator  locator of the component that cannot be created.
     */
	public CreateException(String correlationId, Object locator) {
		super(correlationId, "CANNOT_CREATE", "Requested component " + locator + " cannot be created");
		this.withDetails("locator", locator);
	}

	/**
	 * Creates an error instance and assigns its values.
	 * 
     * @param correlationId    (optional) a unique transaction id to trace execution through call chain.
     * @param message  human-readable error.
     */
	public CreateException(String correlationId, String message) {
		super(correlationId, "CANNOT_CREATE", message);
	}
}
