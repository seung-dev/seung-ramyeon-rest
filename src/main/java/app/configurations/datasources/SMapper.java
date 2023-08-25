package app.configurations.datasources;

import java.util.List;

import seung.util.kimchi.types.SLinkedHashMap;

public interface SMapper {

	public List<SLinkedHashMap> select_rows(String statement);
	
	public List<SLinkedHashMap> select_rows(String statement, Object query);
	
	public SLinkedHashMap select_row(String statement);
	
	public SLinkedHashMap select_row(String statement, Object query);
	
	public String select_text(String statement);
	
	public String select_text(String statement, Object query);
	
	public Integer select_integer(String statement);
	
	public Integer select_integer(String statement, Object query);
	
	public int insert(String statement);
	
	public int insert(String statement, Object query);
	
	public int update(String statement);
	
	public int update(String statement, Object query);
	
	public int delete(String statement);
	
	public int delete(String statement, Object query);
	
}
