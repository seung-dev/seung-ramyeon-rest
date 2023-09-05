package app.boot.configuration.web.types;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import seung.util.kimchi.SText;
import seung.util.kimchi.types.SLinkedHashMap;
import seung.util.kimchi.types.SType;

@Builder(builderMethodName = "defaultBuilder")
@Getter
@Setter
public class SResponseBody extends SType {

	private String trace_id;
	
	@Builder.Default
	private long request_time = -1L;
	
	@Builder.Default
	private long response_time = -1L;
	
	@Builder.Default
	private long elapsed_time = -1L;
	
	@Builder.Default
	private String error_code = SError.FAIL.code();
	
	@Builder.Default
	private String error_message = "";
	
	@Builder.Default
	private SLinkedHashMap data = new SLinkedHashMap();
	
	public static SResponseBodyBuilder builder(SRequestAttribute request_attribute) {
		return defaultBuilder()
				.trace_id(request_attribute.getTrace_id())
				.request_time(request_attribute.getRequest_time())
				;
	}
	
	@SuppressWarnings("unchecked")
	public SResponseBody add(Object key, Object value) {
		this.data.put(key, value);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SResponseBody merge(Map map) {
		if(map != null) {
			this.data.putAll(map);
		}
		return this;
	}
	
	public SResponseBody parse(String json_format_string) {
		SLinkedHashMap response = new SLinkedHashMap(json_format_string);
		this.error_code = response.get_text("error_code");
		this.error_message = response.get_text("error_message");
		this.data = response.get_slinkedhashmap("data");
		return this;
	}
	
	public void error_code(String error_code) {
		this.error_code = error_code;
	}
	
	public void error_message(String error_message) {
		this.error_message = error_message;
	}
	
	public void error(SError s_error) {
		this.error_code(s_error.code());
		this.error_message(s_error.message());
	}
	
	public void success() {
		this.error(SError.SUCCESS);
	}
	
	public void exception(Exception exception) {
		this.error_message(SText.exception(exception));
	}
	
	public boolean has_error() {
		return !SError.SUCCESS.code().equals(this.error_code);
	}
	
	public void data(SLinkedHashMap data) {
		this.data = data;
	}
	
	public SResponseBody done() {
		this.response_time = System.currentTimeMillis();
		this.elapsed_time = response_time - request_time;
		return this;
	}
	
	public SResponseBody done(SError s_error) {
		this.error(s_error);
		return this.done();
	}
	
	public String get_text(String key, String default_value) {
		return this.data.get_text(key, default_value);
	}
	
	public Integer get_int(String key, Integer default_value) {
		return this.data.get_int(key, default_value);
	}
	
	public Long get_long(String key, Long default_value) {
		return this.data.get_long(key, default_value);
	}
	
	public BigInteger get_bigint(String key, BigInteger default_value) {
		return this.data.get_bigint(key, default_value);
	}
	
	public Double get_double(String key, Double default_value) {
		return this.data.get_double(key, default_value);
	}
	
	public SLinkedHashMap get_slinkedhashmap(String key) {
		return this.data.get_slinkedhashmap(key);
	}
	
	public List<SLinkedHashMap> get_list_slinkedhashmap(String key) {
		return this.data.get_list_slinkedhashmap(key);
	}
	
	public String[] get_array_string(String key) {
		return this.data.get_array_string(key);
	}
	
	public List<String> get_list_string(String key) {
		return this.data.get_list_string(key);
	}
	
	public byte[] get_byte_array(String key) {
		return this.data.get_byte_array(key);
	}
	
}
