package com.rssecurity.storemanager;

import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.repository.FornecedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
