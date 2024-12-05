package io.microservice.socialfeedbackservice.socialfeedbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SocialfeedbackserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialfeedbackserviceApplication.class, args);
	}


}
