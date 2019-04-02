package gpk.practice.spring.bootmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
