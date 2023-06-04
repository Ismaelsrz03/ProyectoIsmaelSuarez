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

@RequestMapping("/alumnoEjercicio")
@Controller
public class AlumnoEjercicioController {

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
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("miAlumno",alumnoUsuario);
		model.addAttribute("miEntrenador",alumnoUsuario.getEntrenadores());
		model.addAttribute("misEjercicios",alumnoUsuario.getEjercicios());
		model.addAttribute("miUsuario",usuarioLog);
		
		return "alumnoEjercicio";
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
//	public String editarEjercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado, 
//			BindingResult bindingresult, @RequestParam("file") MultipartFile file) {
//
//		  Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//		    Ejercicio e = null;
//
//		    for (Ejercicio ej : alumnoUsuario.getEjercicios()) {
//		        if (ej.getId().equals(id)) {
//		            e = ej;
//		            break;
//		        }
//		    }
//
//		    if (e == null) {
//		        // Manejar el caso en el que no se encuentre el ejercicio
//		        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
//		        return "redirect:/alumnoEjercicio";
//		    }
//
//		    e.setNombre(ejercicioEditado.getNombre());
//		    e.setReps(ejercicioEditado.getReps());
//		    e.setSeries(ejercicioEditado.getSeries());
//		    e.setDescripcion(ejercicioEditado.getDescripcion());
//
//		    if (!file.isEmpty()) {
//		        try {
//		            byte[] imageBytes = file.getBytes();
//		            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
//		            e.setImagen(encodedString);
//		            e.setMimeType(file.getContentType());
//		        } catch (IOException ex) {
//		            ex.printStackTrace();
//		        }
//		    }
//
//		    alumnoService.insertarAlumno(alumnoUsuario);
//		    return "redirect:/alumnoEjercicio";
//	}

//	@PostMapping("/add")
//	public String addEjercicio(Model model,@ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew, BindingResult bindingresult,
//			Integer id, @RequestParam("file") MultipartFile file) {
//	    Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//	    
//	    if(!file.isEmpty()) {
//
//			 
//			 try {
//
//				 byte[] imageBytes =file.getBytes();
//				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
//				 ejercicioNew.setImagen(encodedString);
//				 ejercicioNew.setMimeType(file.getContentType());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	    
//	    ejercicioNew.setAlumnos(Collections.singleton(alumnoUsuario));
//	    
//	    alumnoUsuario.getEjercicios().add(ejercicioNew);
//	    
//	    alumnoService.insertarAlumno(alumnoUsuario);
//	    
//	    return "redirect:/alumnoEjercicio";
//	}
	

	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes, 
			HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		Optional<Ejercicio> ejerMostrar = ejercicioService.obtenerEjercicioPorID(id);
		
		if (ejerMostrar.isPresent()
				&& alumnoUsuario.getEjercicios().contains(ejerMostrar.get())) {
			model.addAttribute("ejerMostrar", ejerMostrar.get());
			
			String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isAlumnoEjercicioUrl = urlActual.startsWith("/alumnoEjercicio");
	       
	        model.addAttribute("isAlumnoEjercicioUrl", isAlumnoEjercicioUrl);
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + id);
		}

		

		return "redirect:/alumnoEjercicio";

	}
	
//	@GetMapping("/delete/{id}")
//	String deleteEjercicio(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
//		Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(id);
//		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
//		Alumno al = alumnoService.obtenerAlumnoPorID(alumnoUsuario.getId()).get();
//		if(ejer.isPresent() && al.getEjercicios().contains(ejer.get())) {
//		Ejercicio e = ejercicioService.obtenerEjercicioPorID(id).get();	
//		for(Alumno a: e.getAlumnos()) {
//			a.getEjercicios().remove(e);
//		}
//	
//		for(Rutina r: al.getRutinas()) {
//			r.getEjercicios().remove(e);
//		}
//		
//		ejercicioService.eliminarEjercicioPorId(id);
//	    return "redirect:/alumnoEjercicio";
//		} else {
//			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + id);
//		}
//		
//		return "redirect:/alumnoEjercicio";
//	}


	
}
