package principal.controller;

import java.util.ArrayList;
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
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("alumnoNuevo", new Alumno());
		model.addAttribute("miAlumno",alumnoUsuario);
		model.addAttribute("miEntrenador",alumnoUsuario.getEntrenadores());
		model.addAttribute("misEjercicios",alumnoUsuario.getEjercicios());
		model.addAttribute("misRutinas",alumnoUsuario.getRutinas());
		
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

	@PostMapping("/edit/{id}")
	public String editarEjercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado, BindingResult bindingresult) {
		
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		Ejercicio e = new Ejercicio();
		e =ejercicioEditado;
		
		for(Ejercicio ej: alumnoUsuario.getEjercicios()) {
			if(ej.getId()==id) {
				alumnoUsuario.getEjercicios().remove(ej);
				alumnoUsuario.getEjercicios().add(e);
			}
		}
		
//		ejercicioService.insertarEjercicio(ejercicioEditado);
		alumnoService.insertarAlumno(alumnoUsuario);
		return "redirect:/alumnoEjercicio";
	}
	
	@PostMapping("/add")
	public String addEjercicio(@ModelAttribute("alumnoNuevo") Alumno alumnoNew, BindingResult bindingresult,Integer id) {
		
		for(Ejercicio e: alumnoNew.getEjercicios()) {
			Ejercicio e2 = e;
			e2.getAlumnos().add(alumnoNew);
		}
		

		alumnoService.insertarAlumno(alumnoNew);
		
		return "redirect:/alumnoEjercicio";
	}
	
	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id) {
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		
		return "ejercicio";
	}
	
	@GetMapping("/delete/{id}")
	String deleteEjercicio(Model model, @PathVariable Integer id) {
		
		alumnoService.eliminarAlumnoPorId(id);

		
		return "redirect:/alumnoEjercicio";
	}
	
}
