package org.pipservices.components.count;

import org.pipservices.components.build.*;
import org.pipservices.commons.refer.*;

/**
 * Creates ICounters components by their descriptors.
 * 
 * @see Factory
 * @see NullCounters
 * @see LogCounters
 * @see CompositeCounters
 */
public class DefaultCountersFactory extends Factory {
	public final static Descriptor Descriptor = new Descriptor("pip-services-commons", "factory", "counters", "default",
			"1.0");
	public final static Descriptor LogCountersDescriptor = new Descriptor("pip-services", "counters", "log", "*",
			"1.0");
	public final static Descriptor CompositeCountersDescriptor = new Descriptor("pip-services", "counters", "composite",
			"*", "1.0");
	public final static Descriptor NullCountersDescriptor = new Descriptor("pip-services", "counters", "null", "*",
			"1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultCountersFactory() {
		registerAsType(NullCountersDescriptor, NullCounters.class);
		registerAsType(LogCountersDescriptor, LogCounters.class);
		registerAsType(CompositeCountersDescriptor, CompositeCounters.class);
	}
}
