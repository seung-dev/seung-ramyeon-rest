package app.configurations.datasources;

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
import org.springframework.context.annotation.Primary;
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
public class SConfDatasource0 {

	private final String _DATASOURCE_PREFIX_0 = "app.datasource.0";
	
	@Bean(name = "datasource0Properties")
	public Properties datasource0Properties(Environment environment) {
		Properties properties = new Properties();
		Binder.get(environment).bind(_DATASOURCE_PREFIX_0, Bindable.of(Properties.class)).get().forEach((key, value) -> {
			properties.put(CaseUtils.toCamelCase(key.toString(), false, '-'), value);
		});
		return properties;
	}
	
	@Primary
	@Bean(name = "dataSource0", destroyMethod = "close")
	public DataSource dataSource0(
			@Qualifier("datasource0Properties") Properties datasource0Properties
			) throws JsonProcessingException {
		log.debug("run");
		HikariConfig hikariConfig = new HikariConfig(datasource0Properties);
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}// end of dataSource0
	
	@Primary
	@Bean(name = "sqlSessionFactory0")
	public SqlSessionFactory sqlSessionFactory0(
			@Qualifier("dataSource0") DataSource dataSource0
			) throws Exception {
		log.debug("run");
		
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.VARCHAR);
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource0);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:sql/datasource0/*.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}// end of sqlSessionFactory0
	
	@Primary
	@Bean(name = "sqlSessionTemplate0")
	public SqlSessionTemplate sqlSessionTemplate0(
			@Qualifier("sqlSessionFactory0") SqlSessionFactory sqlSessionFactory0
			) {
		log.debug("run");
		return new SqlSessionTemplate(sqlSessionFactory0);
	}// end of sqlSessionTemplate0
	
	@Primary
	@Bean(name = "platformTransactionManager0")
	public PlatformTransactionManager platformTransactionManager0(
			@Qualifier("dataSource0") DataSource dataSource0
			) {
		log.debug("run");
//		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource0);
//		dataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(false);
		return new DataSourceTransactionManager(dataSource0);
	}// end of platformTransactionManager0
	
}
