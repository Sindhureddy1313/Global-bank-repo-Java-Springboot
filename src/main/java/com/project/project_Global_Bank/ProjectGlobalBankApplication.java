package com.project.project_Global_Bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Global Bank",
				description = "Backend Rest APIs for Global bank",
				version = "v1.0",
				contact = @Contact(
						name = "Sindhu Velamala",
						email = "sindhureddyvelumala@gmail.com",
						url = "https://github.com"
				),
				license = @License(
						name = "The Global Bank",
						url = "https://github.com"
				)
),
externalDocs = @ExternalDocumentation(
		description = "The Global Bank Documentation",
		url = "https://github.com"
)
)
public class ProjectGlobalBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectGlobalBankApplication.class, args);
	}

}
