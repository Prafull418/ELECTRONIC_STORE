package com.lcwd.electronic.store.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(getApiInfo());

		//security configuration
		docket.securityContexts(Arrays.asList(getSecurityContext()));
		docket.securitySchemes(Arrays.asList(getSchemes()));

		//which apis we have to expose we wrote below code for that only
		ApiSelectorBuilder select = docket.select();
		select.apis(RequestHandlerSelectors.any());
		select.paths(PathSelectors.any());
		Docket build = select.build();
		return build;
	}

	private ApiInfo getApiInfo() {

		ApiInfo apiInfo = new ApiInfo("Elecronic Store backend : APIS", 
				"This is backend project created by me", 
				"1.0.0V", 
				"https://www.learncodewithdurgesh.com", 
				new Contact("Prafull", "http://www.instagram.com/prafull@143", "prafullvar69@gmail.com") , 
				"License of APIS", 
				"https://www.learncodewithdurgesh.com/aboutus",new ArrayList<>());

		return apiInfo;
	}

	private SecurityContext getSecurityContext() {
        SecurityContext context = SecurityContext
        		                  .builder()
        		                  .securityReferences(getSecurityReferences()).build();
		return context;
	}

	private List<SecurityReference> getSecurityReferences() {
		AuthorizationScope[] scopes = {new AuthorizationScope("Global", "Access Everything")};
		return Arrays.asList(new SecurityReference("jwt", scopes));
	}

	private ApiKey getSchemes() {
        return new ApiKey("jwt", "Authorization", "header");
	}
}
