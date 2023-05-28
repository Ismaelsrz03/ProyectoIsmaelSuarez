package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
		Ejercicio e = new Ejercicio();
		e.setNombre(ejercicioEditado.getNombre());
		e.setReps(ejercicioEditado.getReps());
		e.setSeries(ejercicioEditado.getSeries());
		e.setDescripcion(ejercicioEditado.getDescripcion());
		ArrayList<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		for(Ejercicio ee: entrenadorUsuario.getEjercicios()) {
			lista.add(ee);
		}
		Ejercicio aBorrar = new Ejercicio();
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
		
		if(!file.isEmpty()) {

			 try {
				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 e.setImagen(encodedString);
				 e.setMimeType(file.getContentType());
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		entrenadorUsuario.getEjercicios().remove(aBorrar);
		
//		ejercicioService.insertarEjercicio(ejercicioEditado);
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
	        Alumno alumno = alumnoService.obtenerAlumnoPorID(alumnoId);

	        for (Integer ejercicioId : ejerciciosIds) {
	            Ejercicio ejercicio = ejercicioService.obtenerEjercicioPorID(ejercicioId);
	            
	            if (!alumno.getEjercicios().contains(ejercicio)) {
	                alumno.getEjercicios().add(ejercicio);
	            }
	        }

	        alumnoService.insertarAlumno(alumno);
	    }

	    return "redirect:/entrenadorEjercicio";
	}


	
	@GetMapping({"/{id}"})
	String idEjercicio(Model model, @PathVariable Integer id) {
		usuarioLog= obtenerLog();
		Ejercicio ejerMostrar = ejercicioService.obtenerEjercicioPorID(id);
		model.addAttribute("ejerMostrar",ejerMostrar);
		model.addAttribute("miUsuario",usuarioLog);
		
		return "ejercicioVer";
	}
	
	@GetMapping("/delete/{id}")
	String deleteEjercicio(Model model, @PathVariable Integer id) {
		
Ejercicio ejer = ejercicioService.obtenerEjercicioPorID(id);

Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		for(Entrenador e: ejer.getEntrenadores()) {
			e.getEjercicios().remove(ejer);
			e.getEjercicios().add(null);
		}
		
		for(Rutina r: entrenadorUsuario.getRutinas()) {
			r.getEjercicios().remove(ejer);
			r.getEjercicios().add(null);
		}
		
		ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/entrenadorEjercicio";
	}
	
}
