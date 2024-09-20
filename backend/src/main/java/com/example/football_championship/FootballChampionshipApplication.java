package com.example.football_championship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class FootballChampionshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballChampionshipApplication.class, args);
	}

}
