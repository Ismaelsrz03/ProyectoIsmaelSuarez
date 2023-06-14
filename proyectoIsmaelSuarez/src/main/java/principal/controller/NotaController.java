package principal.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Nota;
import principal.modelo.Progreso;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.NotaServiceImpl;
import principal.servicio.impl.ProgresoServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@Controller
@RequestMapping("/notaEntrenador")
public class NotaController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@Autowired
	private NotaServiceImpl notaService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private Usuario miUsuario;
	
	private Usuario usuarioLog;
	
	private Entrenador entrenador;
	
	private Entrenador entrenadorUsuario;
	
	private boolean bienvenida = true;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping(value= {"","/"})
	String homealumnos(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		 Progreso nota = new Progreso();

		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("notaaEditar", new Nota());
		model.addAttribute("notaNuevo", new Nota());
		model.addAttribute("misNotas", entrenadorUsuario.getNotas());
		model.addAttribute("miUsuario",miUsuario);
		model.addAttribute("nota", nota);
		
		if(miUsuario != null) {
			model.addAttribute("bienvenida",bienvenida);
			
			
				bienvenida = false;
				
				for(Entrenador e : miUsuario.getEntrenadores()) {
					entrenador = e;
				}
				
			} 
			
			if(miUsuario==null) {
				bienvenida=true;
			}
			
			
			
			
			model.addAttribute("miEntrenador", entrenador);
		
		return "notaEntrenador";
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
	

	@PostMapping("/crearNota")
	public String crearNota(@ModelAttribute("notaNuevo") Nota notaNew, BindingResult bindingresult,
			Integer id){
		
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		notaNew.setFecha(new Date());
		notaNew.getEntrenadores().add(entrenadorUsuario);
		entrenadorUsuario.getNotas().add(notaNew);
		entrenadorService.insertarEntrenador(entrenadorUsuario);
		
		return "redirect:/notaEntrenador";
	}
	
	@PostMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, @ModelAttribute("notaaEditar") Nota notaEditado,
	                              BindingResult bindingresult) {
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Nota n = null;

	    for (Nota nota : entrenadorUsuario.getNotas()) {
	        if (nota.getId().equals(id)) {
	            n = nota;
	            break;
	        }
	    }

	    if (n == null) {
	        // Manejar el caso en el que no se encuentre el ejercicio
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return "redirect:/notaEntrenador";
	    }
	    
	    n.setFecha(new Date());
	    n.setTitulo(notaEditado.getTitulo());
	    n.setContenido(notaEditado.getContenido());
	    
	    notaService.guardarNota(n);
	    
	    entrenadorUsuario.getNotas().add(n);

	    entrenadorService.insertarEntrenador(entrenadorUsuario);
	    return "redirect:/notaEntrenador";
	}
	
	@GetMapping("/delete/{id}")
	String delete(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Nota> nota = notaService.obtenerNotaPorId(id);

		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		
		if(nota.isPresent()) {
		
		Nota not = notaService.obtenerNotaPorId(id).get();
		for(Entrenador en: not.getEntrenadores()) {
			en.getNotas().remove(not);
		}
		
		notaService.eliminarNotaPorId(id);

		
		return "redirect:/notaEntrenador";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe nota con id " + id);
		}

		

		return "redirect:/notaEntrenador"; 
	
		}
	
	@GetMapping({"/{id}"})
	String idNota(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Optional<Nota> notaMostrar = notaService.obtenerNotaPorId(id);
		
		if (notaMostrar.isPresent()
				&& entrenadorUsuario.getNotas().contains(notaMostrar.get())) {
			model.addAttribute("notaMostrar", notaMostrar.get());
			 String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorNotaoUrl = urlActual.startsWith("/notaEntrenador/" + id);
	        model.addAttribute("isEntrenadorNotaUrl", isEntrenadorNotaoUrl);
			model.addAttribute("miUsuario", usuarioLog);

			return "notaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe nota con id " + id);
		}

		

		return "redirect:/notaEntrenador";

	}
	
	@PostMapping("/enviarNota")
	public String enviarNotas(Model model, @ModelAttribute("notaNuevo") Nota notaNew,
	                              BindingResult bindingResult, @RequestParam("alumnosSeleccionados") Integer[] alumnosIds,
	                              @RequestParam("notasSeleccionados") Integer[] notasIds) {

	    for (Integer alumnoId : alumnosIds) {
	        Alumno alumno = alumnoService.obtenerAlumnoPorID(alumnoId).get();

	        for (Integer notaId : notasIds) {
	            Nota nota = notaService.obtenerNotaPorId(notaId).get();

	            Nota copiaNota = new Nota();
	            copiaNota.setFecha(new Date());
	            copiaNota.setTitulo(nota.getTitulo());
                copiaNota.setContenido(nota.getContenido());


	            notaService.guardarNota(copiaNota);

	            alumno.getNotas().add(copiaNota);
	        }

	        alumnoService.insertarAlumno(alumno);
	    }

	    return "redirect:/notaEntrenador";
	}

}