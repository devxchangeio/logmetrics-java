package io.devxchange.logmetrics.types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

/**
 * Created by devxchange.io on 2/10/17.
 */
public class PayloadMessage {

	public enum params {
		messageType("message_type"), duration("Duration"), host("Host"), level("Level"), method("Method"), node(
				"Node"), service("Service"), system("System"), serviceVersion("ServiceVersion"), startDateTime(
						"StartDateTime"), endDateTime("EndDateTime"), error("Fault"), errorCode(
								"FaultCode"), errorMessage(
										"FaultString"), aspects("Aspects"), requestBody("RequestBody"), responseBody(
												"ResponseBody"), applicationName("ApplicationName"), httpMethod(
														"HttpMethod"), query("Query"), contentType("ContentType");

		private String keyName;

		params(String keyName) {
			this.keyName = keyName;
		}

		public String getKeyName() {
			return this.keyName;
		}
	}

	private final String message_type;
	private final Long duration;
	private final String host;
	private final String level;
	private final String method;
	private final String node;
	private final String service;
	private final String system;
	private final String serviceVersion;
	private final Date startDateTime;
	private final Date endDateTime;
	private final Boolean error;
	private final String errorCode;
	private final String errorMessage;
	private final Map<String, String> aspects;
	private final String requestBody;
	private final String responseBody;
	private final String applicationName;
	private final String httpMethod;
	private final String query;
	private final String contentType;

	public PayloadMessage(String message_type, Long duration, String host, String level, String method, String node,
			String service, String system,

			String serviceVersion, Date startDateTime, Date endDateTime, Boolean error, String errorCode,
			String errorMessage, Map<String, String> aspects, String requestBody, String responseBody,
			String applicationName, String httpMethod, String query, String contentType) {
		this.message_type = message_type;
		this.duration = duration;
		this.host = host;
		this.level = level;
		this.method = method;
		this.node = node;
		this.service = service;
		this.system = system;
		this.serviceVersion = serviceVersion;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.error = error;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.aspects = aspects;
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.applicationName = applicationName;
		this.httpMethod = httpMethod;
		this.query = query;
		this.contentType = contentType;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> elements = new HashMap<>();

		elements.put(params.messageType.keyName, message_type);
		elements.put(params.duration.keyName, duration);
		elements.put(params.host.keyName, host);
		elements.put(params.level.keyName, level);
		elements.put(params.method.keyName, method);
		elements.put(params.node.keyName, node);
		elements.put(params.service.keyName, service);
		elements.put(params.system.keyName, system);
		elements.put(params.serviceVersion.keyName, serviceVersion);
		elements.put(params.startDateTime.keyName, startDateTime);
		elements.put(params.endDateTime.keyName, endDateTime);
		elements.put(params.error.keyName, error);
		elements.put(params.errorCode.keyName, errorCode);
		elements.put(params.errorMessage.keyName, errorMessage);
		elements.put(params.aspects.keyName, aspects);
		elements.put(params.requestBody.keyName, requestBody);
		elements.put(params.responseBody.keyName, responseBody);
		elements.put(params.applicationName.keyName, applicationName);
		elements.put(params.httpMethod.keyName, httpMethod);
		elements.put(params.query.keyName, query);
		elements.put(params.contentType.keyName, contentType);

		return elements;
	}

	public String toJson(boolean includeReq, boolean includeResp) {
		Map<String, Object> map = this.toMap();
		if (!includeReq)
			map.remove(params.requestBody.keyName);
		if (!includeResp)
			map.remove(params.responseBody.keyName);
		return this.toJson(map);
	}

	public String toJson(Map<String, Object> map) {
		return new GsonBuilder().disableHtmlEscaping().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create()
				.toJson(map);
	}

	public String toJson() {
		return this.toJson(false, false);
	}
}
