package proPets.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import proPets.gateway.filters.error.ErrorFilter;
import proPets.gateway.filters.post.PostFilter;
import proPets.gateway.filters.pre.PreFilter;
import proPets.gateway.filters.route.RouteFilter;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication

public class ProPetsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsGatewayServiceApplication.class, args);
	}

	@Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }
    @Bean
    public PostFilter postFilter() {
        return new PostFilter();
    }
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }
    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }

}
