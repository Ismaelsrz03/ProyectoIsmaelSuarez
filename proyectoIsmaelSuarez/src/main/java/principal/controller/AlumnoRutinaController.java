package principal.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	
	@GetMapping(value= {"","/"})
	String homealumnosEjercicios(Model model) {
		
		// Salir a buscar a la BBDD
		usuarioLog= obtenerLog();
		alumnoUsuario = obtenerAlumnoDeUsuario();
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

//	@PostMapping("/edit/{id}")
//	public String editarRutina(@PathVariable Integer id, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado, BindingResult bindingresult) {
//		
//		 Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//		    Rutina r = null;
//
//		    for (Rutina ru : alumnoUsuario.getRutinas()) {
//		        if (ru.getId().equals(id)) {
//		            r = ru;
//		            break;
//		        }
//		    }
//
//		    if (r == null) {
//		        // Manejar el caso en el que no se encuentre la rutina
//		        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
//		        return "redirect:/alumnoRutina";
//		    }
//
//		    r.setNombre(rutinaEditado.getNombre());
//		    r.getEjercicios().clear(); // Limpiar la lista de ejercicios existente en la rutina
//
//		    for (Ejercicio e : rutinaEditado.getEjercicios()) {
//		        r.getEjercicios().add(e);
//		    }
//
//		    alumnoService.insertarAlumno(alumnoUsuario);
//		    return "redirect:/alumnoRutina";
//	}
	
//	@PostMapping("/add")
//	public String addRutina(@ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult, Integer id) {
//	    Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//	    
//	    
//	    for(Ejercicio e: alumnoUsuario.getEjercicios()) {
//	    		e.getRutinas().add(rutinaNew);
//	    }
//	
//	    alumnoUsuario.getRutinas().add(rutinaNew);
//	    alumnoService.insertarAlumno(alumnoUsuario);
//	
//	    
//	    return "redirect:/alumnoRutina";
//	}
	
//	@GetMapping("/delete/{id}")
//	String deleteRutina(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
//		Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(id);
//		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//		Alumno alum = alumnoService.obtenerAlumnoPorID(alumnoUsuario.getId()).get();
//		if(rut.isPresent() && alum.getRutinas().contains(rut.get())) {
//		Rutina r = rutinaService.obtenerRutinaPorID(id).get();
//		
//		for(Alumno a: r.getAlumnos()) {
//			a.getRutinas().remove(r);
//			a.getRutinas().add(null);
//		}
//		
//		for(Entrenador e: r.getEntrenadores()) {
//			e.getRutinas().remove(r);
//			e.getRutinas().add(null);
//		}
//		
//		rutinaService.eliminarRutinaPorId(id);
//	    return "redirect:/alumnoRutina";
//		} else {
//			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe rutina con id " + id);
//		}
//		
//		return "redirect:/alumnoRutina";
//	}
	
	@GetMapping({"/{id}"})
	String verRutina(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		usuarioLog= obtenerLog();
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		Optional<Rutina> rutinaMostrar = rutinaService.obtenerRutinaPorID(id);
		
		if (rutinaMostrar.isPresent()
				&& alumnoUsuario.getRutinas().contains(rutinaMostrar.get())) {
			model.addAttribute("rutinaMostrar", rutinaMostrar.get());
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "rutinaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe rutina con id " + id);
		}

		

		return "redirect:/alumnoRutina";

	}
	
//	@GetMapping("/{id}/quitar/{ejerId}")
//	RedirectView quitarEjercicioRutina(Model model, @PathVariable Integer id, @PathVariable Integer ejerId,
//	                                  RedirectAttributes redirectAttributes) {
//	    Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(id);
//	    Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(ejerId);
//	    
//	    if (rut.isPresent() && ejer.isPresent()) {
//	        Rutina r = rut.get();
//	        Ejercicio ejercicio = ejer.get();
//	        
//	        if (r.getEjercicios().contains(ejercicio)) {
//	            r.getEjercicios().remove(ejercicio);
//	            ejercicio.getRutinas().remove(r);
//	            
//	            rutinaService.insertarRutina(r);
//	            return new RedirectView("/alumnoRutina/{id}", true);
//	        }
//	    }  else {
//	            redirectAttributes.addFlashAttribute("fallo", "La rutina no contiene un ejercicio con ID " + ejerId);
//	        }
//	    
//	    return new RedirectView("/alumnoRutina/{id}", true);
//	}
	
	@GetMapping({"/{id}/{ejerId}"})
	String verEjercicioRutina(Model model, @PathVariable Integer id, @PathVariable Integer ejerId, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Rutina rutinaMostrar = rutinaService.obtenerRutinaPorID(id).get();
		
		Optional<Ejercicio> ejerMostrar = ejercicioService.obtenerEjercicioPorID(ejerId);
		model.addAttribute("rutinaMostrar",rutinaMostrar);
		if (ejerMostrar.isPresent()
				&& rutinaMostrar.getEjercicios().contains(ejerMostrar.get())) {
			model.addAttribute("ejerMostrar", ejerMostrar.get());
			String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isAlumnoRutinaUrl = urlActual.startsWith("/alumnoRutina/" + id + "/" + ejerId);
	       
	        model.addAttribute("isAlumnoRutinaUrl", isAlumnoRutinaUrl);
	        
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "La rutina no tiene un ejercicio con id " + ejerId);
		}

		

		return "redirect:/alumnoRutina/" +id;

	}


	
}
