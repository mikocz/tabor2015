package cz.miko.tabor.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2014
 * @version $Id: $
 */
public class WebAppInitializerImpl implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
			dispatcherServlet.setContextConfigLocation("cz.miko.tabor.config.WebConfig");
		dispatcherServlet.setContextClass(org.springframework.web.context.support.AnnotationConfigWebApplicationContext.class);
		ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
		registration.setLoadOnStartup(1);
		registration.addMapping("/*");

		FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter",new CharacterEncodingFilter());
		fr.setInitParameter("encoding", "UTF-8");
		fr.setInitParameter("forceEncoding", "true");
		fr.addMappingForUrlPatterns(null, true, "/*");
	}
}
