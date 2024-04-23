package com.hrimch.inventoryservice;

import com.hrimch.inventoryservice.entities.Product;
import com.hrimch.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner (ProductRepository productRepository){

		return args -> {
		productRepository.save(Product.builder()
				.id(UUID.randomUUID().toString())
				.name("Laptop")
				.price(3999.90)
				.quantity(15)
				.build());
			productRepository.save(Product.builder()
					.id(UUID.randomUUID().toString())
					.name("Monitor")
					.price(999.90)
					.quantity(5)
					.build());
			productRepository.save(Product.builder()
					.id(UUID.randomUUID().toString())
					.name("Drucker")
					.price(399.90)
					.quantity(1)
					.build());
		};

	}
}
