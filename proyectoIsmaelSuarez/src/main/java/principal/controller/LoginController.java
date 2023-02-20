package principal.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import principal.modelo.Alumno;

@RequestMapping("/login")
@Controller
public class LoginController {
	
	@GetMapping(value= {"","/"})
	String homelogin(Model model) {
		
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		
		
		return "login";
	}
}