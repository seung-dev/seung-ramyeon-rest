package app.boot.configuration.web.rest.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import app.boot.configuration.web.rest.types.SRestHmac;
import app.boot.configuration.web.types.SRequestAttribute;
import app.boot.configuration.web.types.SRequestBody;
import app.boot.configuration.web.types.SResponseBody;
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
	 * @param {@link Map}
	 * @param {@link SLinkedHashMap}
	 * @return {@link SResponseBody}
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
	 * @return {@link SResponseBody}
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
	 * @return {@link SResponseBody}
	 */
	ResponseEntity<SResponseBody> reflect_hmac(SRequestAttribute request_attribute, SRestHmac request_body) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 요청내역 등록
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link SRequestAttribute}
	 * @return {@link Authentication}
	 */
	int add_rest_hist(SRequestAttribute request_attribute, int http_status) throws Exception;
	
}
