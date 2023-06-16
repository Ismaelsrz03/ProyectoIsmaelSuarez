package principal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RolServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/blogs")
@Controller
public class BlogController {
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private Usuario miUsuario;
	
	@GetMapping(value= {"","/"})
	String home(Model model, Authentication authentication) {
		
		
		if (usuarioService.listarUsuarios().isEmpty()) {
            MainController m = new MainController();
			m.crearTablas();
        }
		
		miUsuario = obtenerLog();
		model.addAttribute("miUsuario",miUsuario);
		
		 if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
	            return "redirect:/perfilAlumno";
	        } else {
	        	if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENTRENADOR"))) {
	            return "redirect:/perfilEntrenador";
	        }
	            return "blogs"; // Cambia "index" por la página predeterminada en tu aplicación
	        }
		
		
	}
	
	private Usuario obtenerLog() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario u2 = null;
		UserDetails userDetails = null;
		
		if(principal instanceof UserDetails) {
			
			userDetails = (UserDetails) principal;
			
		 u2 = usuarioService.obtenerUsuarioPorNombre(userDetails.getUsername());
		}
		return u2;
	}
}
