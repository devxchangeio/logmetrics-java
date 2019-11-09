/**
 * 
 */
package io.devxchange.logmetrics.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Created by devxchange.io on 2/10/17.
 */
public class LogUtil {

	private static final Logger LOG = LoggerFactory.getLogger(LogUtil.class);

	public static ContentCachingRequestWrapper unwrapRequest(HttpServletRequest request) {
		ServletRequest baseReq = request;
		while (baseReq instanceof HttpServletRequestWrapper) {
			if (baseReq instanceof ContentCachingRequestWrapper) {
				return (ContentCachingRequestWrapper) baseReq;
			} else {
				baseReq = ((HttpServletRequestWrapper) baseReq).getRequest();
			}
		}
		return null;
	}

	public static ContentCachingResponseWrapper unwrapResponse(HttpServletResponse response) {
		ServletResponse baseResp = response;
		while (baseResp instanceof HttpServletResponseWrapper) {
			if (baseResp instanceof ContentCachingResponseWrapper) {
				return (ContentCachingResponseWrapper) baseResp;
			} else {
				baseResp = ((HttpServletResponseWrapper) baseResp).getResponse();
			}
		}
		return null;
	}

	public static String getRequestPayload(HttpServletRequest request) {
		if (HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod())
				|| HttpMethod.DELETE.matches(request.getMethod())) {
			try {
				return IOUtils.toString(request.getInputStream());
			} catch (IOException ioEx) {
				LOG.error("Failed to read {} request body content: URL={}", request.getMethod(),
						request.getRequestURL(), ioEx);
				return "";
			}
		} else {
			return "";
		}
	}

	public static String getResponsePayload(HttpServletResponse response) throws IOException {
		ContentCachingResponseWrapper cachedResp = unwrapResponse(response);
		if (cachedResp != null) {
			return getPayloadFromByteArray(cachedResp.getContentAsByteArray(), cachedResp.getCharacterEncoding());
		}
		return null;
	}

	public static String getPayloadFromByteArray(byte[] buf, String encoding) {
		String payload = null;
		if (buf.length > 0) {
			int length = buf.length;
			try {
				payload = new String(buf, 0, length, encoding);
				payload = payload.replaceAll("[\n\r]", "");
			} catch (UnsupportedEncodingException e) {
				payload = "[unknown]";
			}
		}
		return payload;
	}

}
