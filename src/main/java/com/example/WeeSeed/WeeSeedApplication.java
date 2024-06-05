package com.example.WeeSeed;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.model.ResNet50;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WeeSeedApplication implements CommandLineRunner {
	public static ComputationGraph resNet50;
	//python -m http.server 8000

	public static void main(String[] args) {
		SpringApplication.run(WeeSeedApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		// ResNet50 모델 초기화
		System.out.println("Initializing ResNet50 model...");
		resNet50 = (ComputationGraph) ResNet50.builder().build().initPretrained();
		System.out.println("ResNet50 model initialized.");
	}
}
