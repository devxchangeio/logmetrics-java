/**
 * 
 */
package io.devxchange.logmetrics.writer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.devxchange.logmetrics.types.PayloadMessage;

/**
 * Created by devxchange.io on 2/10/17.
 */
@Component("manager.logwriter")
@Order(Ordered.LOWEST_PRECEDENCE)
public class LogWriterManager {

	private static final Logger LOG = LoggerFactory.getLogger(LogWriterManager.class);

	private List<LogWriter> writerList;

	@Autowired
	public LogWriterManager(List<LogWriter> writerList) {
		this.writerList = writerList;
		if (this.writerList == null) {
			LOG.error("No log writers loaded.  Only console logging will be used.");
		} else {
			this.writerList.stream().forEach(writer -> LOG.info("Log Writer {} is loaded and set to {}",
					writer.getClass().getSimpleName(), (writer.isEnabled() ? "enabled" : "disabled")));
		}
	}

	public List<LogWriter> getWriters() {
		return Collections.unmodifiableList(this.writerList);
	}

	public void writeTransactionlog(PayloadMessage transactionMessage) {
		this.stream().filter(writer -> writer.isEnabled())
				.forEach(writer -> writer.writeTransactionlog(transactionMessage));
	}

	public Stream<LogWriter> stream() {
		return this.getWriters().stream();
	}

}
