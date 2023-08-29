package app.boot.configuration.web.rest.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import app.boot.configuration.web.rest.type.SRestHmac;
import app.boot.configuration.web.type.SRequestAttribute;
import app.boot.configuration.web.type.SRequestBody;
import app.boot.configuration.web.type.SResponseBody;
import seung.util.kimchi.types.SLinkedHashMap;

public interface SRestS {

	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 요청 반사
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link SRequestAttribute}
	 * @param {@link HttpServletRequest}
	 * @param {@link SLinkedHashMap}
	 * @return {@link SRequestBody}
	 */
	ResponseEntity<SResponseBody> reflect_get(SRequestAttribute request_attribute, Map<String, Object> request_param, SLinkedHashMap request_header) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 요청 반사
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link SRequestAttribute}
	 * @param {@link SRequestBody}
	 * @param {@link SLinkedHashMap}
	 * @return {@link SRequestBody}
	 */
	ResponseEntity<SResponseBody> reflect_post(SRequestAttribute request_attribute, SRequestBody request_body, SLinkedHashMap request_header) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Hmac
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link SRequestAttribute}
	 * @param {@link SRestHmac}
	 * @return {@link SRequestBody}
	 */
	ResponseEntity<SResponseBody> hmac(SRequestAttribute request_attribute, SRestHmac request_body) throws Exception;
	
}
