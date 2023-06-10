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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

@RequestMapping("/alumnos")
@Controller
public class AlumnoController {

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
	String homealumnos(Model model) {
		
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
		model.addAttribute("alumnoaEditar", new Alumno());
		model.addAttribute("alumnoNuevo", new Alumno());
		model.addAttribute("miUsuario",miUsuario);
		
		return "alumnos";
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
	public String editarAlumno(@PathVariable Integer id, @ModelAttribute("alumnoaEditar") Alumno alumnoEditado, BindingResult bindingresult) {
		
		Alumno alumnoaEditar = alumnoService.obtenerAlumnoPorID(id).get();
		
		alumnoaEditar.setNombre(alumnoEditado.getNombre());
		
		if (alumnoEditado.getEntrenadores() != null) {
			Entrenador e = entrenadorService.obtenerEntrenadorPorID(alumnoEditado.getEntrenadores().getId()).get();
			alumnoEditado.setEntrenadores(e);
		} else {
			alumnoEditado.setEntrenadores(null);
		}
		
		if (alumnoaEditar.getUsuarios() != null) {
			Usuario u = usuarioService.obtenerUsuarioPorID(alumnoaEditar.getUsuarios().getId()).get();
			alumnoEditado.setUsuarios(u);
		}
		
		for(Ejercicio ej:alumnoaEditar.getEjercicios()) {
			if(!alumnoEditado.getEjercicios().contains(ej)) {
				ej.getAlumnos().remove(alumnoaEditar);
			}
		}
		
		for(Ejercicio ej:alumnoEditado.getEjercicios()) {
			if(!alumnoaEditar.getEjercicios().contains(ej)) {
				ej.getAlumnos().add(alumnoEditado);
			}
		}
		
		for(Rutina r:alumnoaEditar.getRutinas()) {
			if(!alumnoEditado.getRutinas().contains(r)) {
				r.getAlumnos().remove(alumnoaEditar);
			}
		}
		
		for(Rutina r:alumnoEditado.getRutinas()) {
			if(!alumnoaEditar.getRutinas().contains(r)) {
				r.getAlumnos().add(alumnoEditado);
			}
		}
		
		alumnoService.insertarAlumno(alumnoEditado);
		
		return "redirect:/alumnos";
	}
	
//	@PostMapping("/add")
//	public String addAlumno(@ModelAttribute("alumnoNuevo") Alumno alumnoNew, BindingResult bindingresult,Integer id) {
//		
//		  if (alumnoNew.getEntrenadores() != null) { 
//		        Entrenador entrenadorNuevo = entrenadorService.obtenerEntrenadorPorID(alumnoNew.getEntrenadores().getId());
//		        entrenadorNuevo.getAlumnos().add(alumnoNew);
//		        alumnoNew.setEntrenadores(entrenadorNuevo);
//		    }
//		
//		for(Ejercicio e: alumnoNew.getEjercicios()) {
//			Ejercicio e2 = e;
//			e2.getAlumnos().add(alumnoNew);
//		}
//		
//		for(Rutina r: alumnoNew.getRutinas()) {
//			Rutina r2 = r;
//			r2.getAlumnos().add(alumnoNew);
//		}
//
//		alumnoService.insertarAlumno(alumnoNew);
//		
//		return "redirect:/alumnos";
//	}
	
	@GetMapping({"/{id}"})
	String idAlumno(Model model, @ModelAttribute("miUsuario") Usuario miUsuario, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		if(alumnoMostrar.isPresent()) {
		model.addAttribute("alumnoMostrar",alumnoMostrar.get());
		model.addAttribute("miUsuario",miUsuario);
		
		return "alumno";
		}
		 else {
				redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
			}
		return "redirect:/alumnos";
	}
	
	@GetMapping("/delete/{id}")
	String deleteAlumno(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);
		
		if(alumno.isPresent()) {
		
		alumnoService.eliminarAlumnoPorId(id);
		
		return "redirect:/alumnos";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
		}
	return "redirect:/alumnos";
		
		
	}
	
}
