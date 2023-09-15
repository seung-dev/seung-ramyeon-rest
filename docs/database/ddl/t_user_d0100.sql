--DROP TABLE IF EXISTS restful.t_user_d0100 CASCADE;
CREATE TABLE restful.t_user_d0100 (
	user_no VARCHAR(10) NOT NULL
	, date_inst TIMESTAMP NOT NULL DEFAULT NOW()
	, date_updt TIMESTAMP NOT NULL DEFAULT NOW()
	, user_state VARCHAR(1) DEFAULT 'H'
	, date_signin TIMESTAMP
	, date_close TIMESTAMP
	, email VARCHAR(64) DEFAULT ''
	, phone_number VARCHAR(16) DEFAULT ''
	, name_full VARCHAR(64) DEFAULT ''
	, name_nick VARCHAR(16) DEFAULT ''
	, avatar VARCHAR(128) DEFAULT ''
	, CONSTRAINT pk_t_user_d0100 PRIMARY KEY (user_no)
);


CREATE INDEX idx_0_t_user_d0100 ON restful.t_user_d0100 USING GIN (email);
CREATE INDEX idx_1_t_user_d0100 ON restful.t_user_d0100 USING GIN (name_full);


COMMENT ON TABLE restful.t_user_d0100 IS '회원 기본정보';

COMMENT ON COLUMN restful.t_user_d0100.user_no       IS '사용자번호';
COMMENT ON COLUMN restful.t_user_d0100.date_inst     IS '등록일시';
COMMENT ON COLUMN restful.t_user_d0100.date_updt     IS '수정일시';
COMMENT ON COLUMN restful.t_user_d0100.user_state    IS '회원상태 - Active: 사용, Disabled: 잠김, Closed: 탈퇴, Holding: 대기';
COMMENT ON COLUMN restful.t_user_d0100.date_signin   IS '로그인일시';
COMMENT ON COLUMN restful.t_user_d0100.date_close    IS '탈퇴일시';
COMMENT ON COLUMN restful.t_user_d0100.email         IS '이메일';
COMMENT ON COLUMN restful.t_user_d0100.phone_number  IS '휴대전화번호';
COMMENT ON COLUMN restful.t_user_d0100.name_full     IS '이름';
COMMENT ON COLUMN restful.t_user_d0100.name_nick     IS '별명';
COMMENT ON COLUMN restful.t_user_d0100.avatar        IS '아바타주소';


--ALTER TABLE restful.t_user_d0100 OWNER TO restful;


SELECT * FROM restful.t_user_d0100;










