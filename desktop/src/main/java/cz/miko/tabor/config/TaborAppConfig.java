package cz.miko.tabor.config;

import cz.miko.tabor.core.config.TaborCoreConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Configuration
@Import(TaborCoreConfig.class)
@ComponentScan({"cz.miko.tabor.controller."})
public class TaborAppConfig {
}
