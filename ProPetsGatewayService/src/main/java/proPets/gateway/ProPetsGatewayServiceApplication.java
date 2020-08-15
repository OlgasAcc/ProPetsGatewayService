package proPets.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication

//@ComponentScan(value = "com.cloud", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value=ExcludeFromComponentScan.class)})
//@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)

public class ProPetsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsGatewayServiceApplication.class, args);
	}
}
