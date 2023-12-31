package app.boot.configuration.web.rest.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.annotation.Resource;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.boot.configuration.datasource.SMapper0;
import app.boot.configuration.web.rest.service.SRestS;
import app.boot.configuration.web.rest.types.SRestHmac;
import app.boot.configuration.web.types.SRequestAttribute;
import app.boot.configuration.web.types.SRequestBody;
import app.boot.configuration.web.types.SResponseBody;
import app.boot.configuration.web.types.SResponseEntity;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SConvert;
import seung.util.kimchi.SSecurity;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sRestS")
@Slf4j
public class SRestSI implements SRestS {

	@Resource(name = "sMapper0")
	private SMapper0 sMapper0;
	
	@Override
	public ResponseEntity<SResponseBody> reflect_get(
			SRequestAttribute request_attribute
			, Map<String, Object> request_param
			, SLinkedHashMap request_header
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		try {
			
			while(true) {
				
				response_body.add("request_header", request_header);
				response_body.add("request_attribute", request_attribute);
				response_body.add("request_param", request_param);
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
		return SResponseEntity.build(request_attribute, response_body);
	}// end of reflect_get
	
	@Override
	public ResponseEntity<SResponseBody> reflect_post(
			SRequestAttribute request_attribute
			, SRequestBody request_body
			, SLinkedHashMap request_header
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		try {
			
			while(true) {
				
				response_body.add("request_header", request_header);
				response_body.add("request_attribute", request_attribute);
				response_body.add("request_body", request_body);
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
		return SResponseEntity.build(request_attribute, response_body);
	}// end of reflect_post
	
	@Override
	public ResponseEntity<SResponseBody> reflect_hmac(
			SRequestAttribute request_attribute
			, SRestHmac request_body
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		long timestamp = 0;
		String access_key = "";
		String signature = "";
		
		try {
			
			access_key = request_body.getAccess_key();
			timestamp = System.currentTimeMillis();
			
			while(true) {
				
				String message = String.join(" ", request_body.getUrl_path(), Long.toString(timestamp), request_body.getAccess_key());
				Charset charset = StandardCharsets.UTF_8;
				
				signature = SConvert.encode_hex(
						SSecurity.mac(
								request_body.getAlgorithm()//algorithm
								, BouncyCastleProvider.PROVIDER_NAME//provider
								, request_body.getSecret_key()//key
								, message
								, charset
								)
						);
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		} finally {
			response_body.add("timestamp", timestamp);
			response_body.add("access_key", access_key);
			response_body.add("signature", signature);
		}// end of try
		
		return SResponseEntity.build(request_attribute, response_body);
	}// end of reflect_hmac
	
	@Override
	public int add_rest_hist(
			SRequestAttribute request_attribute
			, int http_status
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		int add_rest_hist = 0;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			query.merge(request_attribute);
			query.add("roles", String.join(",", request_attribute.getRoles()));
			query.add("http_status", http_status);
			
			add_rest_hist = sMapper0.insert("add_rest_hist", query);
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
		return add_rest_hist;
	}// end of add_rest_hist
	
}
