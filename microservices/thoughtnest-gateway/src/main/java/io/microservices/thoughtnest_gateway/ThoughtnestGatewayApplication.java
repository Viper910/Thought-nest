package io.microservices.thoughtnest_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ThoughtnestGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThoughtnestGatewayApplication.class, args);
	}

}
