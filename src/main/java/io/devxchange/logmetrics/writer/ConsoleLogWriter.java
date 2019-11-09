/**
 * 
 */
package io.devxchange.logmetrics.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.devxchange.logmetrics.types.PayloadMessage;

/**
 * Created by devxchange.io on 2/10/17.
 */
@Component
public class ConsoleLogWriter implements LogWriter {

	public static final String PAYLOAD_LOGGING = "io.oneclicklabs.logging";

	@Value("${io.oneclicklabs.logging.enabled:true}")
	public boolean LOGGING_ENABLED;

	@Value("${io.oneclicklabs.logging.request.enabled:true}")
	public boolean LOGGING_REQUEST_ENABLED;

	@Value("${io.oneclicklabs.logging.response.enabled:true}")
	public boolean LOGGING_RESPONSE_ENABLED;

	private static final Logger LOG = LoggerFactory.getLogger(PAYLOAD_LOGGING);

	@Override
	public void writeTransactionlog(PayloadMessage message) {
		LOG.info("[payloadmessage] {}", message.toJson(LOGGING_REQUEST_ENABLED, LOGGING_RESPONSE_ENABLED));
	}

	@Override
	public boolean isEnabled() {
		return LOGGING_ENABLED;
	}

}
