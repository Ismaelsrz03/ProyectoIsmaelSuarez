package principal.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@RequestMapping("/panel")
@Controller
public class PanelController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	private Usuario miUsuario;
	
	@GetMapping(value= {"","/"})
	String homepanel(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		
		Alumno alumno = new Alumno();
		for(Alumno a: misalumnos) {
			alumno = a;
		}
		
		Entrenador entrenador = new Entrenador();
		
		for(Entrenador e: misentrenadores) {
			entrenador = e;
		}
		
		model.addAttribute("alumno",alumno);
		model.addAttribute("entrenador",entrenador);
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("alumnoaEditar", new Alumno());
		model.addAttribute("alumnoNuevo", new Alumno());
		model.addAttribute("miUsuario",miUsuario);
		
		return "panel";
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
	
	@GetMapping("/buscarAlumnoId")
	String buscarAlumnoPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		
		if(alumnoMostrar.isPresent()) {
			model.addAttribute("alumnoMostrar",alumnoMostrar.get());
			return "redirect:/alumnos/" +id;
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
		}
		return "redirect:/alumnos";
	}
	
	
	@GetMapping("/buscarEntrenadorId")
	String buscarEntrenadorPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Entrenador> entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(id);
		
		if(entrenadorMostrar.isPresent()) {

			model.addAttribute("entrenadorMostrar",entrenadorMostrar.get());
			return "redirect:/entrenadores/" +id;
			
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con id " + id);
		}
		
		return "redirect:/entrenadores/" +id;
	}
	
	@GetMapping("/buscarAlumnoNombre")
	String buscarAlumnoPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Alumno> alumno = alumnoService.obtenerAlumnoPorNombre(nombre);
		
		if(!alumno.isEmpty()) {
		
		Alumno alumnoMostrar = new Alumno();
		for(Alumno a: alumno) {
			alumnoMostrar = alumnoService.obtenerAlumnoPorID(a.getId()).get();

		}
		
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		return "redirect:/alumnos/" +alumnoMostrar.getId();
		
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con nombre " + nombre);
		}
		return "redirect:/alumnos";
		
	}
	
	@GetMapping("/borrarAlumnoId/{id}")
	String deleteAlumno(Model model, @RequestParam("idAlumno") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);
		
		if(alumno.isPresent()) {
		
		alumnoService.eliminarAlumnoPorId(id);
		
		return "redirect:/alumnos";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
		}
	return "redirect:/alumnos";
		
		
	}
	
	@GetMapping("/borrarEntrenadorId/{id}")
	String deleteEntrenador(Model model, @RequestParam("idEntrenador") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Entrenador> entrenador = entrenadorService.obtenerEntrenadorPorID(id);
		
		if(entrenador.isPresent()) {
			Entrenador en = entrenadorService.obtenerEntrenadorPorID(id).get();
			for(Alumno a : en.getAlumnos()) {
				a.setEntrenadores(null);
			}
			entrenadorService.eliminarEntrenadorPorId(id);

		return "redirect:/entrenadores";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con id " + id);
		}
	return "redirect:/entrenadores";
		
		
	}

	
	
	@GetMapping("/buscarEntrenadorNombre")
	String buscarEntrenadorPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Entrenador> entrenadores = entrenadorService.obtenerEntrenadorPorNombre(nombre);
		
		if(!entrenadores.isEmpty()) {
		
		Entrenador entrenadorMostrar = new Entrenador();
		
		for(Entrenador e: entrenadores) {
			entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(e.getId()).get();
			
		}
		
		model.addAttribute("entrenadorMostrar",entrenadorMostrar);
		
		
		return "redirect:/entrenadores/" +entrenadorMostrar.getId();
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con nombre " + nombre);
		}
	return "redirect:/entrenadores";
		
	}
	
//	@GetMapping("/buscarEntrenadorId")
//	String buscarEntrenadorPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
//		
//		Optional<Entrenador> entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(id);
//		
//		if(entrenadorMostrar.isPresent()) {
//
//			model.addAttribute("entrenadorMostrar",entrenadorMostrar.get());
//			return "redirect:/entrenadores/" +id;
//			
//		} else {
//			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con id " + id);
//		}
//		
//		return "redirect:/entrenadores/" +id;
//	}
//	
//	@GetMapping("/buscarAlumnoNombre")
//	String buscarAlumnoPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
//		
//		ArrayList<Alumno> alumno = alumnoService.obtenerAlumnoPorNombre(nombre);
//		
//		if(!alumno.isEmpty()) {
//		
//		Alumno alumnoMostrar = new Alumno();
//		for(Alumno a: alumno) {
//			alumnoMostrar = alumnoService.obtenerAlumnoPorID(a.getId()).get();
//
//		}
//		
//		model.addAttribute("alumnoMostrar",alumnoMostrar);
//		return "redirect:/alumnos/" +alumnoMostrar.getId();
//		
//		} else {
//			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con nombre " + nombre);
//		}
//		return "redirect:/alumnos";
//		
//	}
//	
//	@GetMapping("/borrarAlumnoId/{id}")
//	String deleteAlumno(Model model, @RequestParam("idAlumno") Integer id, RedirectAttributes redirectAttributes) {
//		
//		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);
//		
//		if(alumno.isPresent()) {
//		
//		alumnoService.eliminarAlumnoPorId(id);
//		
//		return "redirect:/alumnos";
//		} else {
//			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
//		}
//	return "redirect:/alumnos";
//		
//		
//	}
	
}
