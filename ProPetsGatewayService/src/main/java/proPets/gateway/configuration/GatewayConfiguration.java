package proPets.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RefreshScope

public class GatewayConfiguration {
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Value("${base.jwt.url}")
	String baseJWTUrl;
	
	@RefreshScope
	public String getBaseJWTUrl() {
		return baseJWTUrl;
	}
	
	
}	
	
	
	

//	 @Bean
//	    @Primary
//	    public CorsFilter corsFilter() {
//	        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        final CorsConfiguration config = new CorsConfiguration();
//	        config.setAllowCredentials(true);
//	        config.addAllowedOrigin("*");
//	        config.addAllowedHeader("*");
//	        config.addAllowedMethod("OPTIONS");
//	        config.addAllowedMethod("HEAD");
//	        config.addAllowedMethod("GET");
//	        config.addAllowedMethod("PUT");
//	        config.addAllowedMethod("POST");
//	        config.addAllowedMethod("DELETE");
//	        config.addAllowedMethod("PATCH");
//	        source.registerCorsConfiguration("/**", config);
//	        return new CorsFilter(source);
//	    }
	
//	 @Bean
//	 public FilterRegistrationBean<CorsFilter> corsFilter() {
//	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	     CorsConfiguration config = new CorsConfiguration();
//	     config.setAllowCredentials(true);
//	     config.addAllowedOrigin("http://localhost:3000");
//	     config.addAllowedHeader("Authorization");
//	     config.addAllowedHeader("Content-Type");
//	     config.addAllowedMethod("GET");
//	     config.setMaxAge((long) 3600);
//	     source.registerCorsConfiguration("/**", config);
//	     FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
//	     bean.setOrder(0);
//	     return bean;
//	 }
