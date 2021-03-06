package org.pipservices3.components.config;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates {@link IConfigReader} components by their descriptors.
 * 
 * @see Factory
 * @see MemoryConfigReader
 * @see JsonConfigReader
 * @see YamlConfigReader
 */
public class DefaultConfigReaderFactory extends Factory {
	public static final Descriptor Descriptor = new Descriptor("pip-services3-commons", "factory", "config-reader",
			"default", "1.0");
	public static final Descriptor MemoryConfigReaderDescriptor = new Descriptor("pip-services3", "config-reader",
			"memory", "*", "1.0");
	public static final Descriptor JsonConfigReaderDescriptor = new Descriptor("pip-services3", "config-reader", "json",
			"*", "1.0");
	public static final Descriptor YamlConfigReaderDescriptor = new Descriptor("pip-services3", "config-reader", "yaml",
			"*", "1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultConfigReaderFactory() {
		registerAsType(MemoryConfigReaderDescriptor, MemoryConfigReader.class);
		registerAsType(JsonConfigReaderDescriptor, JsonConfigReader.class);
		registerAsType(YamlConfigReaderDescriptor, YamlConfigReader.class);
	}
}
