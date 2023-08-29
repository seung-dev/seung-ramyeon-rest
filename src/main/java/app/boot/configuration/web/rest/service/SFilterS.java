package app.boot.configuration.web.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface SFilterS {

	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Permit All 체크
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link HttpServletRequest}
	 * @return {@link Boolean}
	 */
	boolean filter_permitall(HttpServletRequest request) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Access Key 검증
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 * @param {@link HttpServletRequest}
	 * @return {@link Authentication}
	 */
	Authentication filter_authentication(HttpServletRequest request) throws Exception;
	
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
	 * @param {@link HttpServletRequest}
	 * @return {@link Authentication}
	 */
	int add_rest_hist(HttpServletRequest request, int http_status) throws Exception;
	
}
