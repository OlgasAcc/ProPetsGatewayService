package proPets.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import proPets.gateway.configuration.DefaultRibbonConfig;
import proPets.gateway.configuration.ExcludeFromComponentScan;

@EnableZuulProxy
@EnableDiscoveryClient
@ComponentScan(value = "com.cloud", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value=ExcludeFromComponentScan.class)})

@SpringBootApplication
@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)

public class ProPetsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsGatewayServiceApplication.class, args);
	}
}
