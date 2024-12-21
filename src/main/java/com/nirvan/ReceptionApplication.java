package com.nirvan;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ReceptionApplication {


	public static void main(String[] args) {
		// Run Spring Boot application and get the ApplicationContext
		ApplicationContext context = SpringApplication.run(ReceptionApplication.class, args);

		// Set the Spring ApplicationContext for JavaFxApplication
		JavaFxApplication.setSpringContext(context);

		Application.launch(JavaFxApplication.class, args);
	}

}
