package app.boot.configuration.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.text.CaseUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@EnableTransactionManagement
@Configuration
@Slf4j
public class SConfDatasource1 {

	private final String _DATASOURCE_PREFIX_1 = "app.datasource.1";
	
	@Bean(name = "datasource1Properties")
	public Properties datasource0Properties(Environment environment) {
		Properties properties = new Properties();
		Binder.get(environment).bind(_DATASOURCE_PREFIX_1, Bindable.of(Properties.class)).get().forEach((key, value) -> {
			properties.put(CaseUtils.toCamelCase(key.toString(), false, '-'), value);
		});
		return properties;
	}
	
	@Bean(name = "dataSource1", destroyMethod = "close")
	public DataSource dataSource1(
			@Qualifier("datasource1Properties") Properties datasource1Properties
			) throws JsonProcessingException {
		log.debug("run");
		HikariConfig hikariConfig = new HikariConfig(datasource1Properties);
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}// end of dataSource1
	
	@Bean(name = "sqlSessionFactory1")
	public SqlSessionFactory sqlSessionFactory1(
			@Qualifier("dataSource1") DataSource dataSource1
			) throws Exception {
		log.debug("run");
		
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.VARCHAR);
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource1);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:sql/datasource1/*.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}// end of sqlSessionFactory1
	
	@Bean(name = "sqlSessionTemplate1")
	public SqlSessionTemplate sqlSessionTemplate1(
			@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1
			) {
		log.debug("run");
		return new SqlSessionTemplate(sqlSessionFactory1);
	}// end of sqlSessionTemplate1
	
	@Bean(name = "platformTransactionManager1")
	public PlatformTransactionManager platformTransactionManager1(
			@Qualifier("dataSource1") DataSource dataSource1
			) {
		log.debug("run");
		return new DataSourceTransactionManager(dataSource1);
	}// end of platformTransactionManager1
	
}
