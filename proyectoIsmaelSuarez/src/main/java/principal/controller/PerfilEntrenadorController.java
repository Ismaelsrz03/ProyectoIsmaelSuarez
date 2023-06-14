package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/perfilEntrenador")
@Controller
public class PerfilEntrenadorController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private Usuario miUsuario;
	
	private Usuario usuarioLog;
	
	private Entrenador entrenador;
	
	private Entrenador entrenadorUsuario;
	
	private boolean bienvenida = true;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping(value= {"","/"})
	String homealumnos(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("perfilaEditar", new Entrenador());
		model.addAttribute("usuarioaEditar", new Usuario());
		model.addAttribute("contraaEditar", new Usuario());
		model.addAttribute("miUsuario",miUsuario);
		
		if(miUsuario != null) {
			model.addAttribute("bienvenida",bienvenida);
			
			
				bienvenida = false;
				
				for(Entrenador e: miUsuario.getEntrenadores()) {
					entrenador = e;
				}
			} 
			
			if(miUsuario==null) {
				bienvenida=true;
			}
			
			
			model.addAttribute("miEntrenador", entrenador);
		
		return "perfilEntrenador";
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
	
	private Entrenador obtenerEntrenadorDeUsuario() {
		Entrenador res = null;
		Set<Entrenador> entrenadores = usuarioLog.getEntrenadores();
		for (Entrenador entrenador : entrenadores) {
			if(entrenador!=null)
				return entrenador;
		} 	
		return res;
	}
	
	@PostMapping("/editarPerfil/{id}")
	private String editarPerfil(@PathVariable Integer id, @ModelAttribute("perfilaEditar") Entrenador entrenadorEditado, 
			BindingResult bindingresult) {
		
		Entrenador usuarioEntrenador = obtenerEntrenadorDeUsuario();
		
		Entrenador entrenadoraEditar = entrenadorService.obtenerEntrenadorPorID(usuarioEntrenador.getId()).get();
		
		entrenadoraEditar.setNombre(entrenadorEditado.getNombre());
		entrenadoraEditar.setApellidos(entrenadorEditado.getApellidos());
		entrenadoraEditar.setCorreo(entrenadorEditado.getCorreo());
		entrenadoraEditar.setSexo(entrenadorEditado.getSexo());
		entrenadoraEditar.setEdad(entrenadorEditado.getEdad());
		entrenadoraEditar.setTitulo(entrenadorEditado.getTitulo());
		entrenadoraEditar.setDescripcion(entrenadorEditado.getDescripcion());
		entrenadoraEditar.setProfesion(entrenadorEditado.getProfesion());
		entrenadoraEditar.setUbicacion(entrenadorEditado.getUbicacion());
		entrenadoraEditar.setInsta(entrenadorEditado.getInsta());
		entrenadoraEditar.setYoutube(entrenadorEditado.getYoutube());
		entrenadoraEditar.setTiktok(entrenadorEditado.getTiktok());
		entrenadoraEditar.setTwitter(entrenadorEditado.getTwitter());
		entrenadoraEditar.setFacebook(entrenadorEditado.getFacebook());
	
		
		entrenadorService.insertarEntrenador(entrenadoraEditar);
		
		return "redirect:/perfilEntrenador";
	}
	
	@PostMapping("/editarUsuario/{id}")
	public String editarUsuario(@PathVariable Integer id, @ModelAttribute("usuarioaEditar") Usuario usuarioEditado, 
			BindingResult bindingresult, @RequestParam("file") MultipartFile file) {
		
		Usuario usu = miUsuario;
		
		Usuario usuarioaEditar= usuarioService.obtenerUsuarioPorID(usu.getId()).get();
		
		
		if(!file.isEmpty()) {

			 
			 try {

				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 usuarioaEditar.setImagen(encodedString);
				 usuarioaEditar.setMimeType(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		usuarioService.insertarUsuario(usuarioaEditar);
		
		return "redirect:/perfilEntrenador";
	}
	
	@PostMapping("/cambiarContrasena/{id}")
	private String cambiarContra(@PathVariable Integer id, @ModelAttribute("contraaEditar") Usuario usuarioEditado, BindingResult bindingresult) {
		
		Usuario usuarioaeditar= usuarioService.obtenerUsuarioPorID(id).get();
		
		usuarioaeditar.setPassword(passwordEncoder.encode(usuarioEditado.getPassword()));
		
		
		usuarioService.insertarUsuario(usuarioaeditar);
		
		return "redirect:/perfilEntrenador";
	}
	 
	
}