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

@RequestMapping("/entrenadorRutina")
@Controller
public class EntrenadorRutinaController {

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
	
	private Ejercicio ejerciciosRutinas;
	
	@GetMapping(value= {"","/"})
	String homeEntrenadorAlumnos(Model model) {
		
		// Salir a buscar a la BBDD
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		ejerciciosRutinas= ejercicioRutinas();
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
		model.addAttribute("rutinaaEditar", new Rutina());
		model.addAttribute("rutinaNuevo", new Rutina());
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("misAlumnos", entrenadorUsuario.getAlumnos());
		model.addAttribute("misRutinas",entrenadorUsuario.getRutinas());
		model.addAttribute("rutnasEjercicios",ejerciciosRutinas);
		
		return "entrenadorRutina";
	}
	private Ejercicio ejercicioRutinas() {
		Ejercicio res = null;
		Entrenador entrenadorUsuario= obtenerEntrenadorDeUsuario();
		for(Rutina rut: entrenadorUsuario.getRutinas()) {
		for(Ejercicio ej: rut.getEjercicios()) {
			return ej;
		}
		}
		return res;
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
	public String editarRutina(@PathVariable Integer id, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado, BindingResult bindingresult) {
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		Rutina r = new Rutina();
		r.setNombre(rutinaEditado.getNombre());
		Rutina aBorrar = new Rutina();
		for(Rutina ru: entrenadorUsuario.getRutinas()) {
			if(ru.getId()==id) {
				aBorrar = ru;
				entrenadorUsuario.getRutinas().add(r);
			}
		}
		entrenadorUsuario.getRutinas().remove(aBorrar);
		
		entrenadorService.insertarEntrenador(entrenadorUsuario);
		return "redirect:/entrenadorRutina";
	}
	
	@PostMapping("/add")
	public String addRutina(@ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult, Integer id) {
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Rutina r = new Rutina();
	    for(Ejercicio e: rutinaNew.getEjercicios()) {
			rutinaNew.getEjercicios().add(e);
			e.getRutinas().add(rutinaNew);
			rutinaService.insertarRutina(rutinaNew);
		}
	    
	    r.setNombre(rutinaNew.getNombre());

	    r.setEntrenadores(Collections.singleton(entrenadorUsuario));
	     entrenadorUsuario.getRutinas().add(r);
	    
	    
	    entrenadorService.insertarEntrenador(entrenadorUsuario);
	    
	    return "redirect:/entrenadorRutina";
	}
	
	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id) {
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		
		return "ejercicio";
	}
	
	@GetMapping("/delete/{id}")
	String deleteEjercicio(Model model, @PathVariable Integer id) {
Rutina rut = rutinaService.obtenerRutinaPorID(id);
		
		for(Alumno a: rut.getAlumnos()) {
			a.getRutinas().remove(rut);
			a.getRutinas().add(null);
		}
		
		for(Entrenador e: rut.getEntrenadores()) {
			e.getRutinas().remove(rut);
			e.getRutinas().add(null);
		}
		
		rutinaService.eliminarRutinaPorId(id);
		
		return "redirect:/entrenadorRutina";
	}
	
}
