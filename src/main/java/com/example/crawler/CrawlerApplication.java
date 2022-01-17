package com.example.crawler;

import com.example.crawler.common.LaunchParse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(CrawlerApplication.class, args);
	}

}
