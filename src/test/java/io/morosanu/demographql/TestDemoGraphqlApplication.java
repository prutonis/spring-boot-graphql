package io.morosanu.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDemoGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoGraphqlApplication::main).with(TestDemoGraphqlApplication.class).run(args);
	}

}
