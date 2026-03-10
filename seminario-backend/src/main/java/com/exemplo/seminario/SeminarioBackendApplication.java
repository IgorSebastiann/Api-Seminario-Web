package com.exemplo.seminario; 

import org.springframework.boot.SpringApplication; // classe que inicia a aplicação
import org.springframework.boot.autoconfigure.SpringBootApplication; // classe que configura a aplicação

@SpringBootApplication // classe que configura a aplicação
public class SeminarioBackendApplication { // classe principal da aplicação

	public static void main(String[] args) { // método principal da aplicação
		SpringApplication.run(SeminarioBackendApplication.class, args); // inicia a aplicação
	}

}
