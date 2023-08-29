package app.boot.configuration.datasource;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Repository(value = "sMapper0")
@Slf4j
public class SMapper0 implements SMapper {

	@Resource(name = "sqlSessionTemplate0")
	private SqlSession sqlSession;
	
	public List<SLinkedHashMap> select_rows(String statement) {
		log.debug("run");
		return sqlSession.selectList(statement);
	}// end of select_rows
	
	public List<SLinkedHashMap> select_rows(String statement, Object query) {
		log.debug("run");
		return sqlSession.selectList(statement, query);
	}// end of select_rows
	
	public SLinkedHashMap select_row(String statement) {
		log.debug("run");
		return sqlSession.selectOne(statement);
	}// end of select_row
	
	public SLinkedHashMap select_row(String statement, Object query) {
		log.debug("run");
		return sqlSession.selectOne(statement, query);
	}// end of select_row
	
	public String select_text(String statement) {
		log.debug("run");
		SLinkedHashMap row = this.select_row(statement);
		if(row == null) {
			return null;
		}
		return row.get_text(statement);
	}// end of select_text
	
	public String select_text(String statement, Object query) {
		log.debug("run");
		SLinkedHashMap row = this.select_row(statement, query);
		if(row == null) {
			return null;
		}
		return row.get_text(statement);
	}// end of select_text
	
	public Integer select_integer(String statement) {
		log.debug("run");
		SLinkedHashMap row = this.select_row(statement);
		if(row == null) {
			return null;
		}
		return row.get_int(statement);
	}// end of select_integer
	
	public Integer select_integer(String statement, Object query) {
		log.debug("run");
		SLinkedHashMap row = this.select_row(statement, query);
		if(row == null) {
			return null;
		}
		return row.get_int(statement);
	}// end of select_integer
	
	public int insert(String statement) {
		log.debug("run");
		return sqlSession.insert(statement);
	}// end of insert
	
	public int insert(String statement, Object query) {
		log.debug("run");
		return sqlSession.insert(statement, query);
	}// end of insert
	
	public int update(String statement) {
		log.debug("run");
		return sqlSession.update(statement);
	}// end of update
	
	public int update(String statement, Object query) {
		log.debug("run");
		return sqlSession.update(statement, query);
	}// end of update
	
	public int delete(String statement) {
		log.debug("run");
		return sqlSession.delete(statement);
	}// end of delete
	
	public int delete(String statement, Object query) {
		log.debug("run");
		return sqlSession.delete(statement, query);
	}// end of delete
	
}
