/**
 * 
 */
package io.devxchange.logmetrics.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Created by devxchange.io on 2/10/17.
 */
public class CachedRequestWrapper extends ContentCachingRequestWrapper {

    public CachedRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(ArrayUtils.isEmpty(this.getContentAsByteArray())) {
            return super.getInputStream();
        } else {
            return new CustomServletInputStream(this.getContentAsByteArray());
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if(ArrayUtils.isEmpty(this.getContentAsByteArray())) {
            return super.getReader();
        } else {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.getContentAsByteArray())));
        }
    }

    private static class CustomServletInputStream extends ServletInputStream {

        private ByteArrayInputStream buffer;

        public CustomServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new RuntimeException("Not implemented");
        }
    }
}

