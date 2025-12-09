package br.com.fiap.taykarus.motors.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.fiap.taykarus.motors")
@EnableJpaRepositories(basePackages = "br.com.fiap.taykarus.motors.adapter.out.persistence.repository")
@EntityScan(basePackages = "br.com.fiap.taykarus.motors.adapter.out.persistence.entity")
public class MotorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorsApplication.class, args);
	}

}
