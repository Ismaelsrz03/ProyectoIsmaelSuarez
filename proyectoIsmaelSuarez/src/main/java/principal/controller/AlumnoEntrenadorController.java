package principal.controller;

import java.util.ArrayList;
import java.util.Optional;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Nota;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.NotaServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/alumnoEntrenador")
@Controller
public class AlumnoEntrenadorController {

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
	
	@Autowired
	private NotaServiceImpl notaService;
	
	private Usuario usuarioLog;
	
	private Alumno alumnoUsuario;
	
	@GetMapping(value= {"","/"})
	String homealumnosEntrenador(Model model) {
		
		// Salir a buscar a la BBDD
		usuarioLog= obtenerLog();
		alumnoUsuario = obtenerAlumnoDeUsuario();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		ArrayList<Usuario> misusuarios = (ArrayList<Usuario>) usuarioService.listarUsuarios();
		ArrayList<Usuario> listUsu = new ArrayList<Usuario>();
		
		if(alumnoUsuario.getEntrenadores() !=null) {
		Entrenador en = alumnoUsuario.getEntrenadores();
			Usuario u = usuarioService.obtenerUsuarioPorID(en.getUsuarios().getId()).get();
			listUsu.add(u);
			if(en!=null) {
		model.addAttribute("usuEntre", en.getUsuarios());
		}
		}
		
		model.addAttribute("usu",listUsu);
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("listaUsuarios",misusuarios);
		model.addAttribute("alumnoaEditar", new Alumno());
		model.addAttribute("alumnoNuevo", new Alumno());
		model.addAttribute("miEntrenador",alumnoUsuario.getEntrenadores());
		model.addAttribute("miAlumno",alumnoUsuario);
		
		model.addAttribute("miUsuario",usuarioLog);
		
		return "alumnoEntrenador";
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
	
	
	
	@PostMapping("/add")
	public String buscarEntrenador(@ModelAttribute("alumnoNuevo") Alumno alumnoNew, BindingResult bindingResult, Integer id) {
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		if (alumnoUsuario.getEntrenadores() == null) { 
		Entrenador en = entrenadorService.obtenerEntrenadorPorID(alumnoNew.getEntrenadores().getId()).get();
		en.getAlumnos().add(alumnoUsuario);
		alumnoUsuario.setEntrenadores(en);
		}
		
		alumnoService.insertarAlumno(alumnoUsuario);
		return "redirect:/alumnoEntrenador";
		
	}
	
	
	@GetMapping("/delete/{id}")
	String deleteAlumno(Model model, @PathVariable Integer id) {
		
		alumnoService.eliminarAlumnoPorId(id);

		
		return "redirect:/alumnos";
	}
	
	@GetMapping({"/{id}"})
	String idNota(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		usuarioLog= obtenerLog();
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		Optional<Nota> notaMostrar = notaService.obtenerNotaPorId(id);
		
		if (notaMostrar.isPresent()
				&& alumnoUsuario.getNotas().contains(notaMostrar.get())) {
			model.addAttribute("notaMostrar", notaMostrar.get());
			
			model.addAttribute("miUsuario", usuarioLog);

			return "notaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe nota con id " + id);
		}

		

		return "redirect:/alumnoEjercicio";

	}
}
