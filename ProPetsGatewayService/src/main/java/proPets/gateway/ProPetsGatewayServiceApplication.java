package proPets.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import proPets.gateway.filters.pre.SimpleFilter;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication

public class ProPetsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsGatewayServiceApplication.class, args);
	}

	@Bean
	public SimpleFilter simpleFilter() {
		return new SimpleFilter();
	}

}
