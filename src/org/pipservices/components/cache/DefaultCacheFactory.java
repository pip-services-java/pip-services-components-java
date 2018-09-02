package org.pipservices.components.cache;

import org.pipservices.components.build.*;
import org.pipservices.commons.refer.*;

public class DefaultCacheFactory extends Factory {
	public final static Descriptor Descriptor = new Descriptor("pip-services-commons", "factory", "cache", "*", "1.0");
	public final static Descriptor MemoryCacheDescriptor = new Descriptor("pip-services", "cache", "memory", "*", "1.0");
	public final static Descriptor NullCacheDescriptor = new Descriptor("pip-services", "cache", "null", "*", "1.0");
	

	public DefaultCacheFactory() {
		registerAsType(MemoryCacheDescriptor, MemoryCache.class);
		registerAsType(NullCacheDescriptor, NullCache.class);
	}
}