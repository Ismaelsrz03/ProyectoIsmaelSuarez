package principal.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import principal.modelo.Alumno;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/login")
@Controller
public class LoginController {
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	
	@GetMapping(value= {"","/"})
	String homelogin(Model model) {
		
		if(usuarioService.listarUsuarios().isEmpty()) {
			MainController m = new MainController();
			m.crearTablas();
		}
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		
		
		return "redirect:/";
	}
}