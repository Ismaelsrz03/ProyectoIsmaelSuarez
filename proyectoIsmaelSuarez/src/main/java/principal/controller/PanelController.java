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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@RequestMapping("/panel")
@Controller
public class PanelController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	private Usuario miUsuario;
	
	@GetMapping(value= {"","/"})
	String homepanel(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		ArrayList<Usuario> misusuarios = (ArrayList<Usuario>) usuarioService.listarUsuarios();
		
		
		Alumno alumno = new Alumno();
		for(Alumno a: misalumnos) {
			alumno = a;
		}
		
		Entrenador entrenador = new Entrenador();
		
		for(Entrenador e: misentrenadores) {
			entrenador = e;
		}
		
		Usuario usuario = new Usuario();
		
		for(Usuario u : misusuarios) {
			usuario = u;
		}
		
		Ejercicio ejercicio = new Ejercicio();
		
		for(Ejercicio e : misejercicios) {
			ejercicio = e;
		}
		
		Rutina rutina = new Rutina();
		
		for(Rutina r: misrutinas) {
			rutina = r;
		}
		
		model.addAttribute("alumno",alumno);
		model.addAttribute("entrenador",entrenador);
		model.addAttribute("usuario",usuario);
		model.addAttribute("ejercicio", ejercicio);
		model.addAttribute("rutina", rutina);
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("alumnoaEditar", new Alumno());
		model.addAttribute("alumnoNuevo", new Alumno());
		model.addAttribute("miUsuario",miUsuario);
		
		return "panel";
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
	
	@GetMapping("/buscarAlumnoId")
	String buscarAlumnoPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		
		if(alumnoMostrar.isPresent()) {
			model.addAttribute("alumnoMostrar",alumnoMostrar.get());
			return "redirect:/alumnos/" +id;
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
		}
		return "redirect:/alumnos";
	}
	
	
	@GetMapping("/buscarEntrenadorId")
	String buscarEntrenadorPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Entrenador> entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(id);
		
		if(entrenadorMostrar.isPresent()) {

			model.addAttribute("entrenadorMostrar",entrenadorMostrar.get());
			return "redirect:/entrenadores/" +id;
			
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con id " + id);
		}
		
		return "redirect:/entrenadores/" +id;
	}
	
	@GetMapping("/buscarAlumnoNombre")
	String buscarAlumnoPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Alumno> alumnos = alumnoService.obtenerAlumnosPorNombre(nombre);
		
		if(!alumnos.isEmpty()) {
		
//		Alumno alumnoMostrar = new Alumno();
//		for(Alumno a: alumno) {
//			alumnoMostrar = alumnoService.obtenerAlumnoPorID(a.getId()).get();
//
//		}
//		
//		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		model.addAttribute("alumnos",alumnos);
		model.addAttribute("miUsuario",miUsuario);
		
//		return "redirect:/alumnos/" +alumnoMostrar.getId();
		return "alumnosNombre";
		
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con nombre " + nombre);
		}
		return "redirect:/alumnos";
		
	}
	
	@GetMapping("/borrarAlumnoId/{id}")
	String deleteAlumno(Model model, @RequestParam("idAlumno") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);
		
		if(alumno.isPresent()) {
		
		alumnoService.eliminarAlumnoPorId(id);
		
		return "redirect:/alumnos";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe alumno con id " + id);
		}
	return "redirect:/alumnos";
		
		
	}
	
	@GetMapping("/borrarEntrenadorId/{id}")
	String deleteEntrenador(Model model, @RequestParam("idEntrenador") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Entrenador> entrenador = entrenadorService.obtenerEntrenadorPorID(id);
		
		if(entrenador.isPresent()) {
			Entrenador en = entrenadorService.obtenerEntrenadorPorID(id).get();
			for(Alumno a : en.getAlumnos()) {
				a.setEntrenadores(null);
			}
			entrenadorService.eliminarEntrenadorPorId(id);

		return "redirect:/entrenadores";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con id " + id);
		}
	return "redirect:/entrenadores";
		
		
	}

	
	
	@GetMapping("/buscarEntrenadorNombre")
	String buscarEntrenadorPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Entrenador> entrenadores = entrenadorService.obtenerEntrenadoresPorNombre(nombre);
		
		if(!entrenadores.isEmpty()) {
		
//		Entrenador entrenadorMostrar = new Entrenador();
//		
//		for(Entrenador e: entrenadores) {
//			entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(e.getId()).get();
//			
//		}
//		
//		model.addAttribute("entrenadorMostrar",entrenadorMostrar);
//		
//		
//		return "redirect:/entrenadores/" +entrenadorMostrar.getId();
			
			model.addAttribute("entrenadores", entrenadores);
			model.addAttribute("miUsuario",miUsuario);
			
			return "entrenadoresNombre";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe entrenador con nombre " + nombre);
		}
	return "redirect:/entrenadores";
		
	}
	
	@GetMapping("/buscarUsuarioId")
	String buscarUsuarioPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Usuario> usuarioMostrar = usuarioService.obtenerUsuarioPorID(id);
		
		if(usuarioMostrar.isPresent()) {

			model.addAttribute("usuarioMostrar",usuarioMostrar.get());
			return "redirect:/usuarios/" +id;
			
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe usuario con id " + id);
		}
		
		return "redirect:/usuarios/" +id;
	}
	
	@GetMapping("/buscarUsuarioNombre")
	String buscarUsuarioPorUserName(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		Optional<Usuario> usuario = usuarioService.obtenerUsuariosPorNombre2(nombre);
		
		if(!usuario.isEmpty()) {
		
		Usuario usuarioMostrar = new Usuario();
		
		usuarioMostrar = usuarioService.obtenerUsuarioPorID(usuario.get().getId()).get();

		
		
		model.addAttribute("usuarioMostrar",usuarioMostrar);
		return "redirect:/usuarios/" +usuarioMostrar.getId();
		
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe usuario con nombre " + nombre);
		}
		return "redirect:/usuarios";
		
	}
	
	@GetMapping("/borrarUsuarioId/{id}")
	String deleteUsuario(Model model, @RequestParam("idUsuario") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorID(id);
		
		if(usuario.isPresent()) {
			Usuario usu = usuarioService.obtenerUsuarioPorID(id).get();
			for(Entrenador e : usu.getEntrenadores()) {
				for(Alumno a: e.getAlumnos()) {
					a.setEntrenadores(null);
				}
			}
		usuarioService.eliminarUsuarioPorId(id);
		
		return "redirect:/usuarios";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe usuario con id " + id);
		}
	return "redirect:/usuarios";
		
		
	}
	
	@GetMapping("/buscarEjercicioId")
	String buscarEjercicioPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejercicioMostrar = ejercicioService.obtenerEjercicioPorID(id);
		
		if(ejercicioMostrar.isPresent()) {

			model.addAttribute("ejercicioMostrar",ejercicioMostrar.get());
			return "redirect:/ejercicios/" +id;
			
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
		}
		
		return "redirect:/ejercicios/" +id;
	}
	
	@GetMapping("/buscarEjercicioNombre")
	String buscarEjercicioPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Ejercicio> ejercicios = ejercicioService.obtenerEjerciciosPorNombre(nombre);
//		
		if(!ejercicios.isEmpty()) {
//		
//		Ejercicio ejercicioMostrar = new Ejercicio();
//		for(Ejercicio e: ejercicio) {
//			ejercicioMostrar = ejercicioService.obtenerEjercicioPorID(e.getId()).get();
//
//		}
//		
//		model.addAttribute("ejercicioMostrar",ejercicioMostrar);
//		return "redirect:/ejercicios/" +ejercicioMostrar.getId();
		
		model.addAttribute("ejercicios",ejercicios);
		model.addAttribute("miUsuario",miUsuario);
		
		return "ejerciciosNombre";
		
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con nombre " + nombre);
		}
		return "redirect:/ejercicios";
		
	}
	
	@GetMapping("/borrarEjercicioId/{id}")
	String deleteEjercicio(Model model, @RequestParam("idEjercicio") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejercicio = ejercicioService.obtenerEjercicioPorID(id);
		
		if(ejercicio.isPresent()) {
			
			Ejercicio e = ejercicioService.obtenerEjercicioPorID(id).get();
			for(Alumno a: e.getAlumnos()) {
				a.getEjercicios().remove(e);
			}
			
			for(Entrenador ej: e.getEntrenadores()) {
				ej.getEjercicios().remove(e);
			}
			
			for(Rutina r: e.getRutinas()) {
				r.getEjercicios().remove(e);
			}
			
			ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/ejercicios";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
		}
	return "redirect:/ejercicios";
		
		
	}
	
	@GetMapping("/buscarRutinaId")
	String buscarRutinaPorId(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Rutina> rutinaMostrar = rutinaService.obtenerRutinaPorID(id);
		
		if(rutinaMostrar.isPresent()) {

			model.addAttribute("rutinaMostrar",rutinaMostrar.get());
			return "redirect:/rutinas/" +id;
			
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe rutina con id " + id);
		}
		
		return "redirect:/rutinas/" +id;
	}
	
	@GetMapping("/buscarRutinaNombre")
	String buscarRutinaPorNombre(Model model, @RequestParam("nombre") String nombre, RedirectAttributes redirectAttributes) {
		
		ArrayList<Rutina> rutinas = rutinaService.obtenerRutinasPorNombre(nombre);
		
		if(!rutinas.isEmpty()) {
		
//		Rutina rutinaMostrar = new Rutina();
//		for(Rutina r: rutina) {
//			rutinaMostrar = rutinaService.obtenerRutinaPorID(r.getId()).get();
//
//		}
//		
//		model.addAttribute("rutinaMostrar",rutinaMostrar);
//		return "redirect:/rutinas/" +rutinaMostrar.getId();
			
			model.addAttribute("rutinas",rutinas);
			model.addAttribute("miUsuario",miUsuario);
			
			return "rutinasNombre";
		
		} else {
			redirectAttributes.addFlashAttribute("fallo", "No existe rutina con nombre " + nombre);
		}
		return "redirect:/rutinas";
		
	}
	
	@GetMapping("/borrarRutinaId/{id}")
	String deleteRutina(Model model, @RequestParam("idRutina") Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Rutina> rutina = rutinaService.obtenerRutinaPorID(id);
		
		if(rutina.isPresent()) {
			
			Rutina r = rutinaService.obtenerRutinaPorID(id).get();
			
			for(Alumno a: r.getAlumnos()) {
				a.getRutinas().remove(r);
			}
			
			for(Entrenador e: r.getEntrenadores()) {
				e.getRutinas().remove(r);
			}
			   
			rutinaService.eliminarRutinaPorId(id);

			
			return "redirect:/rutinas";
			} else {
				redirectAttributes.addFlashAttribute("fallo", "No existe rutina con id " + id);
			}
		return "redirect:/rutinas";
		}
	
}
