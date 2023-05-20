package principal;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import principal.Main;
import principal.controller.MainController;

@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {
//		MainController m = new MainController();
//        m.crearTablas();
		SpringApplication.run(Main.class, args);
	}

}
