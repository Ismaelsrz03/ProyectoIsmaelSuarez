package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
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
		model.addAttribute("miUsuario",usuarioLog);
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
	public String editarEjercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado,
	                              BindingResult bindingresult, @RequestParam("file") MultipartFile file) {
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Ejercicio e = null;

	    for (Ejercicio ej : entrenadorUsuario.getEjercicios()) {
	        if (ej.getId().equals(id)) {
	            e = ej;
	            break;
	        }
	    }

	    if (e == null) {
	        // Manejar el caso en el que no se encuentre el ejercicio
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return "redirect:/entrenadorEjercicio";
	    }

	    e.setNombre(ejercicioEditado.getNombre());
	    e.setReps(ejercicioEditado.getReps());
	    e.setSeries(ejercicioEditado.getSeries());
	    e.setDescripcion(ejercicioEditado.getDescripcion());
	    
	    String enlace = ejercicioEditado.getVideo();
		if (enlace.contains("youtube.com/watch?v=")) {
		    int indice = enlace.lastIndexOf("=");
		    String nuevoEnlace = "https://www.youtube.com/embed/" + enlace.substring(indice + 1);
		    e.setVideo(nuevoEnlace);
		} else {
		    e.setVideo(enlace);
		}

	    if (!file.isEmpty()) {
	        try {
	            byte[] imageBytes = file.getBytes();
	            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
	            e.setImagen(encodedString);
	            e.setMimeType(file.getContentType());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }

	    entrenadorService.insertarEntrenador(entrenadorUsuario);
	    return "redirect:/entrenadorEjercicio";
	}

	
	@PostMapping("/add")
	public String addEjercicio(@ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew, BindingResult bindingresult,
			Integer id, @RequestParam("file") MultipartFile file) {
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
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
		
		String enlace = ejercicioNew.getVideo();
		if (enlace.contains("youtube.com/watch?v=")) {
		    int indice = enlace.lastIndexOf("=");
		    String nuevoEnlace = "https://www.youtube.com/embed/" + enlace.substring(indice + 1);
		    ejercicioNew.setVideo(nuevoEnlace);
		} else {
		    ejercicioNew.setVideo(enlace);
		}

	    
	    ejercicioNew.setEntrenadores(Collections.singleton(entrenadorUsuario));
	    
	    entrenadorUsuario.getEjercicios().add(ejercicioNew);
	    
	    entrenadorService.insertarEntrenador(entrenadorUsuario);
		
		return "redirect:/entrenadorEjercicio";
	}
	
	@PostMapping("/enviarEjercicio")
	public String enviarEjercicio(Model model, @ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew,
	                              BindingResult bindingResult, @RequestParam("alumnosSeleccionados") Integer[] alumnosIds,
	                              @RequestParam("ejerciciosSeleccionados") Integer[] ejerciciosIds) {

	    for (Integer alumnoId : alumnosIds) {
	        Alumno alumno = alumnoService.obtenerAlumnoPorID(alumnoId).get();

	        for (Integer ejercicioId : ejerciciosIds) {
	            Ejercicio ejercicio = ejercicioService.obtenerEjercicioPorID(ejercicioId).get();

	            // Crear una copia del ejercicio para el alumno
	            Ejercicio copiaEjercicio = new Ejercicio();
	            copiaEjercicio.setNombre(ejercicio.getNombre());
                copiaEjercicio.setDescripcion(ejercicio.getDescripcion());
                copiaEjercicio.setSeries(ejercicio.getSeries());
                copiaEjercicio.setReps(ejercicio.getSeries());
                copiaEjercicio.setImagen(ejercicio.getImagen());
                copiaEjercicio.setMimeType(ejercicio.getMimeType());
	            // Copiar otros atributos relevantes del ejercicio
                String enlace = ejercicio.getVideo();
        		if (enlace.contains("youtube.com/watch?v=")) {
        		    int indice = enlace.lastIndexOf("=");
        		    String nuevoEnlace = "https://www.youtube.com/embed/" + enlace.substring(indice + 1);
        		    copiaEjercicio.setVideo(nuevoEnlace);
        		} else {
        		    copiaEjercicio.setVideo(enlace);
        		}
	            // Guardar el ejercicio en la base de datos y obtener su ID
	            ejercicioService.insertarEjercicio(copiaEjercicio);

	            // Agregar la copia del ejercicio al alumno
	            alumno.getEjercicios().add(copiaEjercicio);
	        }

	        alumnoService.insertarAlumno(alumno);
	    }

	    return "redirect:/entrenadorEjercicio";
	}



	
	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Optional<Ejercicio> ejerMostrar = ejercicioService.obtenerEjercicioPorID(id);
		
		if (ejerMostrar.isPresent()
				&& entrenadorUsuario.getEjercicios().contains(ejerMostrar.get())) {
			model.addAttribute("ejerMostrar", ejerMostrar.get());
			 String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorEjercicioUrl = urlActual.startsWith("/entrenadorEjercicio/" + id);
	        model.addAttribute("isEntrenadorEjercicioUrl", isEntrenadorEjercicioUrl);
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + id);
		}

		

		return "redirect:/entrenadorEjercicio";

	}
	
	@GetMapping("/delete/{id}")
	String deleteEjercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(id);

		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Entrenador entrenador = entrenadorService.obtenerEntrenadorPorID(entrenadorUsuario.getId()).get();
		
		if(ejer.isPresent() && entrenador.getEjercicios().contains(ejer.get())) {
		
			Ejercicio ej = ejercicioService.obtenerEjercicioPorID(id).get();
			
		for(Entrenador e: ej.getEntrenadores()) {
			e.getEjercicios().remove(ej);
		}
		
		for(Alumno a: entrenador.getAlumnos()) {
			for(Rutina r: a.getRutinas()) {
				r.getEjercicios().remove(ej);
			}
		}
		
		for(Rutina r: entrenador.getRutinas()) {
			r.getEjercicios().remove(ej);
		}
		
		ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/entrenadorEjercicio";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + id);
		}

		

		return "redirect:/entrenadorEjercicio"; 
	
		}
	
}
