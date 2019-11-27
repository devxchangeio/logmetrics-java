/**
 *
 */
package io.devxchange.logmetrics.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.devxchange.logmetrics.types.LogMetrics;

import java.util.*;

/**
 * Created by devxchange.io on 2/10/17.
 */
@Component
public class ConsoleLogWriter implements LogWriter {

    public static final String LOGMETRICS_LOGGING = "io.devxchange.logmetrics";

    @Value("${logmetrics.logging.enabled:true}")
    public boolean LOGGING_ENABLED;

    @Value("${logmetrics.logging.request.enabled:true}")
    public boolean LOGGING_REQUEST_ENABLED;

    @Value("${logmetrics.logging.response.enabled:true}")
    public boolean LOGGING_RESPONSE_ENABLED;

    @Value("${logmetrics.logging.obfuscate.enabled:false}")
    public boolean OBFUSCATE_ENABLED;

    @Value("${logmetrics.logging.obfuscate.md5.fields:}")
    public String OBFUSCATE_MD5_FIELDS;

    @Value("${logmetrics.logging.obfuscate.sha256.fields:}")
    public String OBFUSCATE_SHA256_FIELDS;

    @Value("${logmetrics.logging.obfuscate.matchedmask.fields:}")
    public String OBFUSCATE_MATCHEDMASK_FIELDS;

    @Value("${logmetrics.logging.obfuscate.fields:}")
    public String OBFUSCATE_FIELDS;

    private static final Logger LOG = LoggerFactory.getLogger(LOGMETRICS_LOGGING);

    @Override
    public void writeTransactionlog(LogMetrics message) {

        if (OBFUSCATE_ENABLED) {
            LOG.info("[logmetrics] {}", message.toJson(LOGGING_REQUEST_ENABLED, LOGGING_RESPONSE_ENABLED, OBFUSCATE_ENABLED, getObfuscateMap()));
        } else {
            LOG.info("[logmetrics] {}", message.toJson(LOGGING_REQUEST_ENABLED, LOGGING_RESPONSE_ENABLED));
        }
    }

    @Override
    public boolean isEnabled() {
        return LOGGING_ENABLED;
    }

    public Map<String, Set<String>> getObfuscateMap(){

        Map<String, Set<String>> obfuscateMap = new HashMap<String, Set<String>>();
        List<String> items = Arrays.asList(OBFUSCATE_FIELDS.split("\\s*,\\s*"));
        Set fields = new HashSet(items);
        if(!fields.isEmpty())
            obfuscateMap.put("any", fields);

        items = Arrays.asList(OBFUSCATE_MD5_FIELDS.split("\\s*,\\s*"));
        fields = new HashSet(items);
        if(!fields.isEmpty())
            obfuscateMap.put("md5", fields);

        items = Arrays.asList(OBFUSCATE_SHA256_FIELDS.split("\\s*,\\s*"));
        fields = new HashSet(items);
        if(!fields.isEmpty())
            obfuscateMap.put("sha256", fields);

        items = Arrays.asList(OBFUSCATE_MATCHEDMASK_FIELDS.split("\\s*,\\s*"));
        fields = new HashSet(items);
        if(!fields.isEmpty())
            obfuscateMap.put("matchedMask", fields);

        return obfuscateMap;
    }
}
