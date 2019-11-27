package io.devxchange.logmetrics.interceptor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.devxchange.logmetrics.types.LogMetricsBuilder;
import io.devxchange.logmetrics.util.LogUtil;
import io.devxchange.logmetrics.writer.LogWriterManager;

/**
 * Created by devxchange.io on 2/10/17.
 */
@Component
public class FrontendRestInterceptor extends HandlerInterceptorAdapter {

	Logger log = LoggerFactory.getLogger(FrontendRestInterceptor.class);

	private LogWriterManager logWriter;

	@Autowired
	public FrontendRestInterceptor(@Qualifier("manager.logwriter") LogWriterManager logWriter) {

		this.logWriter = logWriter;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("STARTTIME", System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		request.setAttribute("ENDTIME", System.currentTimeMillis());
		logTransactions(request, response, ex);
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	private void logTransactions(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Exception ex)
			throws IOException {

		String hostname = InetAddress.getLocalHost().getHostName();
		String node = InetAddress.getLocalHost().getHostAddress();
		String requestPayload = LogUtil.getRequestPayload(servletRequest);
		String responsePayload = LogUtil.getResponsePayload(servletResponse);
		Date startDateTime = new Date((Long) servletRequest.getAttribute("STARTTIME"));
		Date endDateTime = new Date((Long) servletRequest.getAttribute("ENDTIME"));
		long duration = ((Long) servletRequest.getAttribute("ENDTIME")
				- (Long) servletRequest.getAttribute("STARTTIME"));
		LogMetricsBuilder messageBuilder = new LogMetricsBuilder(hostname, node, null, getServiceOperation(),
				servletRequest.getMethod(), duration);
		messageBuilder.aspects(null).query(servletRequest.getQueryString()).startDateTime(startDateTime)
				.endDateTime(endDateTime).requestBody(requestPayload).contentType(servletRequest.getContentType())
				.responseBody(responsePayload);
		logWriter.writeTransactionlog(messageBuilder.createTransactionMessage());
	}

	private String getServiceOperation() {
		HttpServletRequest hsr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String bestServiceAddress = (String) hsr.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return bestServiceAddress;
	}
}
