package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

@RequestMapping("/ejercicios")
@Controller
public class EjercicioController {

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
	
	@GetMapping(value= {"","/"})
	String homeejercicios(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("miUsuario",miUsuario);
		
		return "ejercicios";
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
	
	@PostMapping("/edit/{id}")
	public String editarejercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado,
			BindingResult bindingresult, @RequestParam("file") MultipartFile file) {
		
		Ejercicio ejercicioaEditar = ejercicioService.obtenerEjercicioPorID(id).get();
		
		ejercicioaEditar.setNombre(ejercicioEditado.getNombre());
		ejercicioaEditar.setImagen(ejercicioEditado.getImagen());
		ejercicioaEditar.setMimeType(ejercicioEditado.getMimeType());
		ejercicioaEditar.setSeries(ejercicioEditado.getSeries());
		ejercicioaEditar.setReps(ejercicioEditado.getReps());
		ejercicioaEditar.setDescripcion(ejercicioEditado.getDescripcion());
		if(!file.isEmpty()) {
			 
			 try {
				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 ejercicioEditado.setImagen(encodedString);
				 ejercicioEditado.setMimeType(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ejercicioService.insertarEjercicio(ejercicioEditado);
		
		return "redirect:/ejercicios";
	}
	
	@PostMapping("/add")
	public String addejercicio(Model model,@ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew, BindingResult bindingresult,
			Integer id, @RequestParam("file") MultipartFile file) {
			
		
		if(!file.isEmpty()) {
			 
			 try {
				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 ejercicioNew.setImagen(encodedString);
				 ejercicioNew.setMimeType(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ejercicioService.insertarEjercicio(ejercicioNew);
		return "redirect:/ejercicios";
	}
	

	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejercicioMostrar = ejercicioService.obtenerEjercicioPorID(id);
		
		if(ejercicioMostrar.isPresent()) {
		
		model.addAttribute("ejercicioMostrar",ejercicioMostrar.get());
		
		
		return "ejercicio";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
		}
	return "redirect:/ejercicios";
	}
	
	@GetMapping("/delete/{id}")
	String deleteejercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(id);
		
		if(ejer.isPresent()) {
		Ejercicio e = ejercicioService.obtenerEjercicioPorID(id).get();
		for(Alumno a: e.getAlumnos()) {
			a.getEjercicios().remove(e);
		}
		
		for(Entrenador ej: e.getEntrenadores()) {
			ej.getEjercicios().remove(e);
		}
		
		for(Rutina r: e.getRutinas()) {
			r.getEjercicios().remove(e);
		}
		
		ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/ejercicios";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
		}
	return "redirect:/ejercicios";
	}
	
}
