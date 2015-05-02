package cz.miko.tabor.web.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Configuration
public class SpringDBConfig {

	@Setter
	@Value("#{db.url}")
	private String dbUrl;

	@Bean
	public DataSource dataSource() {
		return new DriverManagerDataSource(dbUrl);
	}
}
