package com.project.bigbrother;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EntityScan({"entities"})
@ComponentScan({"controllers", "repositories"})
@EnableJpaRepositories(basePackages = {"repositories"})
public class BigBrotherApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigBrotherApplication.class, args);
	}

}
