--DROP TABLE IF EXISTS restful.t_user_d0101 CASCADE;
CREATE TABLE restful.t_user_d0101 (
	user_no VARCHAR(10) NOT NULL
	, user_role VARCHAR(1) NOT NULL
	, date_inst TIMESTAMP NOT NULL DEFAULT NOW()
	, date_updt TIMESTAMP NOT NULL DEFAULT NOW()
	, user_no_updt VARCHAR(10) NOT NULL
	, CONSTRAINT pk_t_user_d0101 PRIMARY KEY (user_no, user_role)
);


COMMENT ON TABLE restful.t_user_d0101 IS '회원 기본정보';

COMMENT ON COLUMN restful.t_user_d0101.user_no       IS '사용자번호 - restful.t_user_d010';
COMMENT ON COLUMN restful.t_user_d0101.user_role       IS '회원번호';
COMMENT ON COLUMN restful.t_user_d0101.date_inst     IS '등록일시';
COMMENT ON COLUMN restful.t_user_d0101.date_updt     IS '수정일시';
COMMENT ON COLUMN restful.t_user_d0101.user_state    IS '회원상태 - Active: 사용, Disabled: 잠김, Closed: 탈퇴, Holding: 대기';
COMMENT ON COLUMN restful.t_user_d0101.date_signin   IS '로그인일시';
COMMENT ON COLUMN restful.t_user_d0101.date_close    IS '탈퇴일시';
COMMENT ON COLUMN restful.t_user_d0101.email         IS '이메일';
COMMENT ON COLUMN restful.t_user_d0101.phone_number  IS '휴대전화번호';
COMMENT ON COLUMN restful.t_user_d0101.name_full     IS '이름';
COMMENT ON COLUMN restful.t_user_d0101.name_nick     IS '별명';
COMMENT ON COLUMN restful.t_user_d0101.avatar        IS '아바타주소';


--ALTER TABLE restful.t_user_d0101 OWNER TO restful;


SELECT * FROM restful.t_user_d0101;










