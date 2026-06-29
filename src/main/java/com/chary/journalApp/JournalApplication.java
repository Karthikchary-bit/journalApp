package com.chary.journalApp;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebEnvironment;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement// 1. Turns on transaction support
public class JournalApplication {

	public static void main(String[] args) {
//		ConfigurableApplicationContext context =
	SpringApplication.run(JournalApplication.class, args);
//		ConfigurableEnvironment environment = context.getEnvironment();
//		System.out.println(environment.getActiveProfiles()[0]);
	}
	@Bean// 2. CRITICAL: Tells Spring to use this as a managed component
	public PlatformTransactionManager fl(MongoDatabaseFactory databaseFactory){
		// 3. Enables multi-document transactions for MongoDB
		return new MongoTransactionManager(databaseFactory);

	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}


}
