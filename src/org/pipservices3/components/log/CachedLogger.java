package org.pipservices3.components.log;

import java.util.*;

import org.pipservices3.commons.config.*;
import org.pipservices3.commons.errors.*;

/**
 * Abstract logger that caches captured log messages in memory and periodically dumps them.
 * Child classes implement saving cached messages to their specified destinations.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>level:             maximum log level to capture
 * <li>source:            source (context) name
 * <li>options:
 *   <ul>
 *   <li>interval:        interval in milliseconds to save log messages (default: 10 seconds)
 *   <li>max_cache_size:  maximum number of messages stored in this cache (default: 100)
 *   </ul>        
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:context-info:*:*:1.0     (optional) {@link org.pipservices3.components.info.ContextInfo} to detect the context id and specify counters source
 * </ul>
 * 
 * @see ILogger
 * @see Logger
 * @see LogMessage
 */
public abstract class CachedLogger extends Logger implements IReconfigurable {
	private final static long _defaultInterval = 60000;

	protected List<LogMessage> _cache = new ArrayList<LogMessage>();
	protected boolean _updated = false;
	protected long _lastDumpTime = System.currentTimeMillis();
	protected long _interval = _defaultInterval;
	protected Object _lock = new Object();

	/**
	 * Writes a log message to the logger destination.
	 * 
	 * @param level         a log level.
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param ex            an error object associated with this message.
	 * @param message       a human-readable message to log.
	 */
	@Override
	protected void write(LogLevel level, String correlationId, Exception ex, String message) {
		ErrorDescription error = ex != null ? ErrorDescriptionFactory.create(ex, correlationId) : null;
		String source = getComputerName(); // Todo: add jar/exe name
		LogMessage logMessage = new LogMessage(level, source, correlationId, error, message);

		synchronized (_lock) {
			_cache.add(logMessage);
		}

		update();
	}

	private String getComputerName() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
			return env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return env.get("HOSTNAME");
		else
			return "Unknown Computer";
	}

	/**
	 * Saves log messages from the cache.
	 * 
	 * @param messages a list with log messages
	 * @throws InvocationException when error occured.
	 */
	protected abstract void save(List<LogMessage> messages) throws InvocationException;

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	public void configure(ConfigParams config) {
		_interval = config.getAsLongWithDefault("interval", _interval);
	}

	/**
	 * Clears (removes) all cached log messages.
	 */
	public void clear() {
		synchronized (_lock) {
			_cache.clear();
			_updated = false;
		}
	}

	/**
	 * Dumps (writes) the currently cached log messages.
	 * 
	 * @throws InvocationException when error occured.
	 * 
	 * @see #write(LogLevel, String, Exception, String)
	 */
	public void dump() throws InvocationException {
		if (_updated) {
			synchronized (_lock) {
				if (!_updated)
					return;

				List<LogMessage> messages = _cache;
				_cache = new ArrayList<LogMessage>();

				save(messages);

				_updated = false;
				_lastDumpTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Makes message cache as updated and dumps it when timeout expires.
	 * 
	 * @see #dump()
	 */
	protected void update() {
		_updated = true;

		if (System.currentTimeMillis() > _lastDumpTime + _interval) {
			try {
				dump();
			} catch (InvocationException ex) {
				// Todo: decide what to do
			}
		}
	}
}
