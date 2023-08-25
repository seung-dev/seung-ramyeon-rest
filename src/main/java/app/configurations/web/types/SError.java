package app.configurations.web.types;

public enum SError {

	SUCCESS("S000", Category.SUCCESS, "Success")
	, FAIL("F999", Category.EXCEPTION, "요청이 실패하였습니다. 관리자에게 문의하세요.")
	, GROUPNAME_IS_NOT_AVAILABLE("A010", Category.ATHENTICATION, "사용할 수 없는 계정입니다.")
	, USERNAME_IS_NOT_AVAILABLE("A020", Category.ATHENTICATION, "사용할 수 없는 계정입니다.")
	, OAUTH2_IS_NOT_AVAILABLE("A030", Category.ATHENTICATION, "사용할 수 없는 계정입니다.")
	, SIGNIN_FAILED("A100", Category.ATHENTICATION, "인증에 실패하였습니다.")
	, SIGNIN_DISABLED("A110", Category.ATHENTICATION, "계정이 비활성화 되었습니다. 관리자에게 문의하세요.")
	, PASSWORD_EXPIERED("A120", Category.ATHENTICATION, "비밀번호를 변경하세요.")
	, SIGNIN_LOCKED("A130", Category.ATHENTICATION, "5회 이상 인증에 실패하였습니다. 관리자에게 문의하세요.")
	, TOKEN_IS_VALID("A200", Category.SUCCESS, "Token is verified.")
	, TOKEN_IS_NOT_SECURE("A201", Category.ATHENTICATION, "io.jsonwebtoken.security.SecurityException")
	, TOKEN_IS_MALFORMED("A202", Category.ATHENTICATION, "io.jsonwebtoken.MalformedJwtException")
	, TOKEN_IS_EXPIERED("A203", Category.ATHENTICATION, "io.jsonwebtoken.ExpiredJwtException")
	, TOKEN_IS_UNSUPPORTED("A204", Category.ATHENTICATION, "io.jsonwebtoken.UnsupportedJwtException")
	, TOKEN_IS_INVALID("A205", Category.ATHENTICATION, "io.jsonwebtoken.IllegalArgumentException")
	, SIGNUP_FAILED("A300", Category.ATHENTICATION, "회원 등록이 실패하였습니다.")
	, BAD_SQL("D100", Category.DATABASE, "org.springframework.jdbc.BadSqlGrammarException")
	;
	
	private final String code;
	private final Category category;
	private final String message;
	
	SError(String code, Category category, String message) {
		this.code = code;
		this.category = category;
		this.message = message;
	}
	
	public String code() {
		return this.code;
	}
	
	public Category category() {
		return this.category;
	}
	
	public String message() {
		return this.message;
	}
	
	public static SError resolve(String error_code) {
		for(SError s_error : values()) {
			if(s_error.code.equals(error_code)) {
				return s_error;
			}
		}
		return null;
	}
	
	public enum Category {
		
		SUCCESS
		, EXCEPTION
		, ATHENTICATION
		, DATABASE
		;
		
	}
	
}