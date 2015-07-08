package cz.miko.tabor.core.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		dataSource.setDriverClassName("org.h2.Driver");
		// TODO MKO load from config
		dataSource.setUrl("jdbc:h2:file:" + getDataDir() + "taborDb");
		dataSource.setUsername("tabor");
		dataSource.setPassword("tabor");
		return dataSource;
	}

	@NotNull
	private String getDataDir() {
		return getWorkingDir() + "/data/";
	}

	private String getWorkingDir() {
		return System.getProperty("user.dir");
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

		List<Integer> list = jdbcTemplate.query("select version from dbVersion where id = 1", (rs, rowNum) -> {
			return rs.getInt(1);
		});
		Integer dbVersion = 0;
		if (list.isEmpty()) {
			jdbcTemplate.update("insert into dbVersion values (1,0)");
		} else {
			dbVersion = list.get(0);
		}

		try {
			PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();

			Resource[] sqlPatches = resolver.getResources("classpath:/META-INF/db_path/patch_*.sql");

			for(Resource sqlPatch : sqlPatches) {
				Integer patchVersion = getVersionFromPatch(sqlPatch);
				if (patchVersion>dbVersion) {
					ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),sqlPatch);
					jdbcTemplate.update("update dbVersion set version = ? where id = 1", patchVersion);
				}
			}

			Integer usersCount = jdbcTemplate.queryForObject("select count(id) from USERS", Integer.class);
			if (usersCount < 1) {
				FileSystemResource resource = new FileSystemResource(getDataDir() + "initData.sql");
				if (resource.exists()) {
					ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
				}
			}
		}
		catch(Exception e) {
			if(log.isFatalEnabled()) log.fatal("Cannot create DB structure!", e);
		}
	}

	private Integer getVersionFromPatch(Resource sqlPatch) {

		Pattern pattern = Pattern.compile("patch_(.+?).sql");
		Matcher matcher = pattern.matcher(sqlPatch.getFilename());
		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}

		return -1;
	}
}
