package com.neoway.documento.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrossOriginConfig {
	@Value("${cors.origin:http://localhost:4200}")
	private String corsOrigin;

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		var source = new UrlBasedCorsConfigurationSource();
		var config = new CorsConfiguration();
		var bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		config.setAllowCredentials(true);
		config.addAllowedOrigin(corsOrigin);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return bean;
	}
}
