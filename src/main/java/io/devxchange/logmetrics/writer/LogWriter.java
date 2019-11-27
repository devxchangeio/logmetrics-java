/**
 * 
 */
package io.devxchange.logmetrics.writer;

import io.devxchange.logmetrics.types.LogMetrics;

/**
 * Created by devxchange.io on 2/10/17.
 */
public interface LogWriter {
	
    void writeTransactionlog(LogMetrics message);
    boolean isEnabled();
}
