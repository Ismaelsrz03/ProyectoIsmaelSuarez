package principal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

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

@RequestMapping("/entrenadorEjercicio")
@Controller
public class EntrenadorEjercicioController {

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
	
	private Usuario usuarioLog;
	
	private Entrenador entrenadorUsuario;
	
	private Ejercicio ejerciciosAlumno;
	
	@GetMapping(value= {"","/"})
	String homeEntrenadorAlumnos(Model model) {
		
		// Salir a buscar a la BBDD
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		ArrayList<Usuario> misusuarios = (ArrayList<Usuario>) usuarioService.listarUsuarios();
	
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("listaUsuarios",misusuarios);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("misAlumnos", entrenadorUsuario.getAlumnos());
		model.addAttribute("misEjercicios",entrenadorUsuario.getEjercicios());
		
		return "entrenadorEjercicio";
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
	public String editarEjercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado, BindingResult bindingresult) {
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		Ejercicio e = new Ejercicio();
		e.setNombre(ejercicioEditado.getNombre());
		Ejercicio aBorrar = new Ejercicio();
		
		ArrayList<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		for(Ejercicio ee: entrenadorUsuario.getEjercicios()) {
			lista.add(ee);
		}
		
		for(Ejercicio ej: lista) {
			if(ej.getId()==id) {
				aBorrar = ej;
				entrenadorUsuario.getEjercicios().add(e);
				
				for(Rutina r: entrenadorUsuario.getRutinas()) {
					r.getEjercicios().remove(aBorrar);
					r.getEjercicios().add(e);
				}
			}
		}
		entrenadorUsuario.getEjercicios().remove(aBorrar);
		
//		ejercicioService.insertarEjercicio(ejercicioEditado);
		entrenadorService.insertarEntrenador(entrenadorUsuario);
		return "redirect:/entrenadorEjercicio";
	}
	
	@PostMapping("/add")
	public String addEjercicio(@ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew, BindingResult bindingresult,Integer id) {
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    
	    ejercicioNew.setEntrenadores(Collections.singleton(entrenadorUsuario));
	    
	    entrenadorUsuario.getEjercicios().add(ejercicioNew);
	    
	    entrenadorService.insertarEntrenador(entrenadorUsuario);
		
		return "redirect:/entrenadorEjercicio";
	}
	
	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id) {
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		
		return "ejercicio";
	}
	
	@GetMapping("/delete/{id}")
	String deleteEjercicio(Model model, @PathVariable Integer id) {
		
Ejercicio ejer = ejercicioService.obtenerEjercicioPorID(id);
		
		for(Alumno a: ejer.getAlumnos()) {
			a.getEjercicios().remove(ejer);
			a.getEjercicios().add(null);
		}
		
		for(Entrenador e: ejer.getEntrenadores()) {
			e.getEjercicios().remove(ejer);
			e.getEjercicios().add(null);
		}
		
		for(Rutina r: ejer.getRutinas()) {
			r.getEjercicios().remove(ejer);
			r.getEjercicios().add(null);
		}
		
		ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/entrenadorEjercicio";
	}
	
}
