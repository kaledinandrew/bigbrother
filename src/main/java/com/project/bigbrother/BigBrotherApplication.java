package com.project.bigbrother;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"models"})
@ComponentScan({"rest", "repositories", "config", "dto", "models", "security", "service"})
@EnableJpaRepositories(basePackages = {"repositories"})
public class BigBrotherApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigBrotherApplication.class, args);
	}

}
