package principal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
		model.addAttribute("miUsuario",usuarioLog);
		
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
	public String editarRutina(@PathVariable Integer id, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado,
	                           BindingResult bindingresult) {
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Rutina r = null;

	    for (Rutina ru : entrenadorUsuario.getRutinas()) {
	        if (ru.getId().equals(id)) {
	            r = ru;
	            break;
	        }
	    }

	    if (r == null) {
	        // Manejar el caso en el que no se encuentre la rutina
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return "redirect:/entrenadorRutina";
	    }

	    r.setNombre(rutinaEditado.getNombre());
	    r.getEjercicios().clear(); // Limpiar la lista de ejercicios existente en la rutina

	    for (Ejercicio e : rutinaEditado.getEjercicios()) {
	        r.getEjercicios().add(e);
	    }

	    entrenadorService.insertarEntrenador(entrenadorUsuario);
	    return "redirect:/entrenadorRutina";
	}

	
	@PostMapping("/add")
	public String addRutina(@ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult, Integer id) {
	    
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    
		for(Ejercicio e: entrenadorUsuario.getEjercicios()) {
	    	
			e.getRutinas().add(rutinaNew);
		}
	    
	     entrenadorUsuario.getRutinas().add(rutinaNew);
	    
	    
	    entrenadorService.insertarEntrenador(entrenadorUsuario);
	    
	    return "redirect:/entrenadorRutina";
	}
	
	 @PostMapping("/enviarRutina")
     public String enviarRutina(Model model, @ModelAttribute("rutinaNuevo") Rutina rutinaNew,
                                BindingResult bindingResult, @RequestParam("alumnosSeleccionados") Integer[] alumnosIds,
                                @RequestParam("rutinasSeleccionados") Integer[] rutinasIds) {

         for (Integer alumnoId : alumnosIds) {
             Alumno alumno = alumnoService.obtenerAlumnoPorID(alumnoId).get();

             for (Integer rutinaId : rutinasIds) {
                 Rutina rutina = rutinaService.obtenerRutinaPorID(rutinaId).get();

                 // Crear una copia de la rutina para el alumno
                 Rutina copiaRutina = new Rutina();
                 copiaRutina.setNombre(rutina.getNombre());
                 // Copiar otros atributos relevantes de la rutina

                 // Copiar los ejercicios asociados a la rutina
                 Set<Ejercicio> copiaEjercicios = new HashSet<>();
                 for (Ejercicio ejercicio : rutina.getEjercicios()) {
                     Ejercicio copiaEjercicio = new Ejercicio();
                     copiaEjercicio.setNombre(ejercicio.getNombre());
                     copiaEjercicio.setDescripcion(ejercicio.getDescripcion());
                     copiaEjercicio.setSeries(ejercicio.getSeries());
                     copiaEjercicio.setReps(ejercicio.getSeries());
                     copiaEjercicio.setImagen(ejercicio.getImagen());
                     copiaEjercicio.setMimeType(ejercicio.getMimeType());
                     
                     String enlace = ejercicio.getVideo();
             		if (enlace.contains("youtube.com/watch?v=")) {
             		    int indice = enlace.lastIndexOf("=");
             		    String nuevoEnlace = "https://www.youtube.com/embed/" + enlace.substring(indice + 1);
             		    copiaEjercicio.setVideo(nuevoEnlace);
             		} else {
             		    copiaEjercicio.setVideo(enlace);
             		}
                     // Copiar otros atributos relevantes del ejercicioç
                     ejercicioService.insertarEjercicio(copiaEjercicio);
                     // Agregar el ejercicio a la copia de la rutina
                     copiaRutina.getEjercicios().add(copiaEjercicio);
                 }

                 // Agregar la copia de la rutina al alumno
                 alumno.getRutinas().add(copiaRutina);
             }

             alumnoService.insertarAlumno(alumno);
         }

         return "redirect:/entrenadorRutina";
     }

	
	
	
	@GetMapping("/delete/{id}")
	String deleteRutina(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(id);
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Entrenador entrenador = entrenadorService.obtenerEntrenadorPorID(entrenadorUsuario.getId()).get();
		
		if(rut.isPresent() && entrenador.getRutinas().contains(rut.get())) {
		
		Rutina r = rutinaService.obtenerRutinaPorID(id).get();	
			
		for(Entrenador e: r.getEntrenadores()) {
			e.getRutinas().remove(r);
			e.getRutinas().add(null);
		}
		
		rutinaService.eliminarRutinaPorId(id);
		
		return "redirect:/entrenadorRutina";
	
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe rutina con id " + id);
		}


		return "redirect:/entrenadorRutina"; 
		
		}
	
	@GetMapping({"/{id}"})
	String verRutina(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Optional<Rutina> rutinaMostrar = rutinaService.obtenerRutinaPorID(id);
		
		if (rutinaMostrar.isPresent()
				&& entrenadorUsuario.getRutinas().contains(rutinaMostrar.get())) {
			model.addAttribute("rutinaMostrar", rutinaMostrar.get());
			 String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorRutinaUrl = urlActual.startsWith("/entrenadorRutina/" + id);
	        model.addAttribute("isEntrenadorRutinaUrl", isEntrenadorRutinaUrl);
			model.addAttribute("miUsuario", usuarioLog);
			model.addAttribute("miEntrenador",entrenadorUsuario);
			return "rutinaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe rutina con id " + id);
		}

		

		return "redirect:/entrenadorRutina";

	}
	
	@GetMapping("/{id}/quitar/{ejerId}")
	RedirectView quitarEjercicioRutina(Model model, @PathVariable Integer id, @PathVariable Integer ejerId,
	                                  RedirectAttributes redirectAttributes) {
	    Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(id);
	    Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(ejerId);
	    
	    if (rut.isPresent() && ejer.isPresent()) {
	        Rutina r = rut.get();
	        Ejercicio ejercicio = ejer.get();
	        
	        if (r.getEjercicios().contains(ejercicio)) {
	            r.getEjercicios().remove(ejercicio);
	            ejercicio.getRutinas().remove(r);
	            
	            rutinaService.insertarRutina(r);
	            return new RedirectView("/entrenadorRutina/{id}", true);
	        }
	    }  else {
	            redirectAttributes.addFlashAttribute("fallo", "La rutina no contiene un ejercicio con ID " + ejerId);
	        }
	    
	    return new RedirectView("/entrenadorRutina/{id}", true);
	}
	
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
	        
	        boolean isEntrenadorRutinaEjercicioUrl = urlActual.startsWith("/entrenadorRutina/" + id + "/" + ejerId);
	       
	        model.addAttribute("isEntrenadorRutinaEjercicioUrl", isEntrenadorRutinaEjercicioUrl);
	        
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "La rutina no tiene un ejercicio con id " + id);
		}

		

		return "redirect:/entrenadorRutina/" +id;

	}
	
	@PostMapping("/{id}/agregarEjercicio")
	public String agregarEjercicio(Model model, @PathVariable Integer id, @RequestParam("ejerciciosId") Integer[] ejerciciosId) {
	    Optional<Rutina> rutina = rutinaService.obtenerRutinaPorID(id);
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    model.addAttribute("miEntrenador", entrenadorUsuario);
	    model.addAttribute("rutinaMostrar",rutina);
	    Entrenador entrenador = entrenadorService.obtenerEntrenadorPorID(entrenadorUsuario.getId()).get();
	    if (rutina.isPresent() && entrenador.getRutinas().contains(rutina.get())) {
	        Rutina rutinaActualizada = rutina.get();
	        for(Integer ejercicioId: ejerciciosId) {
	        Ejercicio ejercicio = ejercicioService.obtenerEjercicioPorID(ejercicioId).get(); // Obtener el ejercicio seleccionado
	        
	            rutinaActualizada.getEjercicios().add(ejercicio);

	        }
	        rutinaService.insertarRutina(rutinaActualizada);
	    }

	    return "redirect:/entrenadorRutina/" + id;
	}



	
}
