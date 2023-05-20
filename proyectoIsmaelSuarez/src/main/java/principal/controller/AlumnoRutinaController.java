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

@RequestMapping("/alumnoRutina")
@Controller
public class AlumnoRutinaController {

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
	
	private Alumno alumnoUsuario;
	
	private Rutina rutinasAlumno;
	
	@GetMapping(value= {"","/"})
	String homealumnosEjercicios(Model model) {
		
		// Salir a buscar a la BBDD
		usuarioLog= obtenerLog();
		alumnoUsuario = obtenerAlumnoDeUsuario();
		rutinasAlumno = obtenerRutinasDeAlumno();
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
		model.addAttribute("miAlumno",alumnoUsuario);
		model.addAttribute("miEntrenador",alumnoUsuario.getEntrenadores());
		model.addAttribute("misEjercicios",alumnoUsuario.getEjercicios());
		model.addAttribute("misRutinas",alumnoUsuario.getRutinas());
		model.addAttribute("miUsuario",usuarioLog);
		return "alumnoRutina";
	}
	
	private Alumno obtenerAlumnoDeUsuario() {
		Alumno res = null;
		Set<Alumno> alumnos = usuarioLog.getAlumnos();
		for (Alumno alumno : alumnos) {
			if(alumno!=null)
				return alumno;
		} 	
		return res;
	}
	
	private Rutina obtenerRutinasDeAlumno() {
		Rutina res = null;
		Set<Rutina> rutinas = alumnoUsuario.getRutinas();
		for (Rutina rutina : rutinas) {
			if(rutina!=null)
				return rutina;
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
	
	private Ejercicio obtenerEjerciciosDeAlumno() {
		Ejercicio res = null;
		Set<Ejercicio> ejercicios = alumnoUsuario.getEjercicios();
		for (Ejercicio ejercicio : ejercicios) {
			if(ejercicio!=null)
				return ejercicio;
		}
		return res;
	}

	@PostMapping("/edit/{id}")
	public String editarRutina(@PathVariable Integer id, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado, BindingResult bindingresult) {
		
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		Rutina r = new Rutina();
		r.setNombre(rutinaEditado.getNombre());
		Rutina aBorrar = new Rutina();
		
		ArrayList<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		for(Ejercicio ee: rutinaEditado.getEjercicios()) {
			lista.add(ee);
		}
		
		ArrayList<Rutina> lista2 = new ArrayList<Rutina>();
		
		for(Rutina rut: alumnoUsuario.getRutinas()) {
			lista2.add(rut);
		}
		
		
		
		for(Rutina ru: lista2) {
			if(ru.getId()==id) {
				aBorrar = ru;
				alumnoUsuario.getRutinas().add(r);		
				
			}
		}
		
		for(Ejercicio e: lista) {
					r.getEjercicios().add(e);
				}
		
		alumnoUsuario.getRutinas().remove(aBorrar);
		
		alumnoService.insertarAlumno(alumnoUsuario);
		return "redirect:/alumnoRutina";
	}
	
	@PostMapping("/add")
	public String addRutina(@ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult, Integer id) {
	    Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
	    
	    
	    for(Ejercicio e: alumnoUsuario.getEjercicios()) {
	    		e.getRutinas().add(rutinaNew);
	    }
	
	    alumnoUsuario.getRutinas().add(rutinaNew);
	    alumnoService.insertarAlumno(alumnoUsuario);
	
	    
	    return "redirect:/alumnoRutina";
	}


	
	/*@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id) {
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		
		return "ejercicio";
	}*/
	
	@GetMapping("/delete/{id}")
	String deleteRutina(Model model, @PathVariable Integer id) {
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
	    return "redirect:/alumnoRutina";
	}


	
}
