package proPets.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@RestController

//@ComponentScan(value = "com.cloud", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value=ExcludeFromComponentScan.class)})
//@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)

public class ProPetsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsGatewayServiceApplication.class, args);
	}
	
	/*
	 * @RequestMapping("/timeout") public String timeout() throws
	 * InterruptedException { Thread.sleep(80000); return "timeout"; }
	 */
}
