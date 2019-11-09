/**
 * 
 */
package io.devxchange.logmetrics.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.devxchange.logmetrics.util.CachedRequestWrapper;

/**
 * Created by devxchange.io on 2/10/17.
 */
@Component
@Order(-1000)
public class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!(request instanceof ContentCachingRequestWrapper)) {
			request = new CachedRequestWrapper(request);
		}

		if (!(response instanceof ContentCachingResponseWrapper)) {
			response = new ContentCachingResponseWrapper(response);
		}

		try {
			filterChain.doFilter(request, response);
		} finally {
			
			if (response != null) {
				((ContentCachingResponseWrapper) response).copyBodyToResponse();
			}
		}

	}

}
