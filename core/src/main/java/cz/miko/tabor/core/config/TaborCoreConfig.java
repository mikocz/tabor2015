package cz.miko.tabor.core.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Configuration
@ComponentScan({"cz.miko.tabor.core.service"})
public class TaborCoreConfig {

	private static final Log log = LogFactory.getLog(TaborCoreConfig.class);

	@Bean
	public DataSource dataSource() {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		// TODO MKO load from config
		dataSource.setUrl("jdbc:hsqldb:file://www/mk/tabor2015/data/tabor");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean(name = "taborCoreSqlSessionFactoryBean")
	public SqlSessionFactoryBean getTaborCoreSqlSessionFactoryBean() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("cz.miko.tabor.core.model");
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/META-INF/tabor-core/mybatis/mybatis-config.xml"));
		return sqlSessionFactoryBean;
	}

	@Bean
	public MapperScannerConfigurer getMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("cz.miko.tabor.core.dao");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("taborCoreSqlSessionFactoryBean");
		return mapperScannerConfigurer;
	}

	public void init(ApplicationContext applicationContext) {
		initDb(applicationContext);
	}

	private void initDb(ApplicationContext applicationContext) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(applicationContext.getBean(DataSource.class));
		jdbcTemplate.execute("create table IF NOT EXISTS dbVersion (id INT PRIMARY KEY , version INT DEFAULT 0);");


		try {
			//ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource("/META-INF/db_path/create.sql"));
		}
		catch(Exception e) {
			if(log.isFatalEnabled()) log.fatal("Cannot create DB structure!", e);
		}
	}
}
