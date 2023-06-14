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
import principal.modelo.Progreso;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.ProgresoServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@Controller
@RequestMapping("/progresoAlumno")
public class ProgresoController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@Autowired
	private ProgresoServiceImpl progresoService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private Usuario miUsuario;
	
	private Usuario usuarioLog;
	
	private Alumno alumno;
	
	private Alumno alumnoUsuario;
	
	private boolean bienvenida = true;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping(value= {"","/"})
	String homealumnos(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		usuarioLog= obtenerLog();
		alumnoUsuario = obtenerAlumnoDeUsuario();
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		 Progreso progreso = new Progreso();

		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("progresoaEditar", new Progreso());
		model.addAttribute("progresoNuevo", new Progreso());
		model.addAttribute("misProgresos", alumnoUsuario.getProgresos());
		model.addAttribute("miUsuario",miUsuario);
		model.addAttribute("progreso", progreso);
		
		if(miUsuario != null) {
			model.addAttribute("bienvenida",bienvenida);
			
			
				bienvenida = false;
				
				for(Alumno a : miUsuario.getAlumnos()) {
					alumno = a;
				}
				
			} 
			
			if(miUsuario==null) {
				bienvenida=true;
			}
			
			
			
			
			model.addAttribute("miAlumno", alumno);
			model.addAttribute("misRutinas",alumnoUsuario.getRutinas());
			Rutina rut = new Rutina();
			for(Rutina r : alumnoUsuario.getRutinas()) {
				rut = r;
			}
			model.addAttribute("miRutina",rut);
		
		return "progresoAlumno";
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
	

	@PostMapping("/crearProgreso")
	public String crearProgreso(@ModelAttribute("progresoNuevo") Progreso progresoNew, BindingResult bindingresult,
			Integer id){
		
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		progresoNew.setFecha(new Date());
		progresoNew.setAlumno(alumnoUsuario);
		alumnoUsuario.getProgresos().add(progresoNew);
		
		alumnoService.insertarAlumno(alumnoUsuario);
		
		return "redirect:/progresoAlumno";
	}
	
	@PostMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, @ModelAttribute("progresoaEditar") Progreso progresoEditado,
	                              BindingResult bindingresult) {
	    Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
	    Progreso p = null;

	    for (Progreso pro : alumnoUsuario.getProgresos()) {
	        if (pro.getId().equals(id)) {
	            p = pro;
	            break;
	        }
	    }

	    if (p == null) {
	        // Manejar el caso en el que no se encuentre el ejercicio
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return "redirect:/progresoAlumno";
	    }
	    
	    p.setFecha(new Date());
	    p.setEjercicio(progresoEditado.getEjercicio());
	    p.setRepeticiones(progresoEditado.getRepeticiones());
	    p.setSeries(progresoEditado.getSeries());
	    p.setPeso(progresoEditado.getPeso());
	    p.setDescripcion(progresoEditado.getDescripcion());
	    
	    progresoService.guardarProgreso(p);
	    
	    alumnoUsuario.getProgresos().add(p);

	    alumnoService.insertarAlumno(alumnoUsuario);
	    return "redirect:/progresoAlumno";
	}
	
	@GetMapping("/delete/{id}")
	String delete(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Optional<Progreso> progreso = progresoService.obtenerProgresoPorId(id);

		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		
		if(progreso.isPresent()) {
		
		Progreso pro = progresoService.obtenerProgresoPorId(id).get();
		
		alumnoUsuario.getProgresos().remove(pro);
		pro.setAlumno(null);
		progresoService.eliminarProgresoPorId(id);

		
		return "redirect:/progresoAlumno";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe progreso con id " + id);
		}

		

		return "redirect:/progresoAlumno"; 
	
		}
	
	@GetMapping({"/{id}"})
	String idProgreso(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Alumno alumnoUsuario = obtenerAlumnoDeUsuario();
		
		Optional<Progreso> progresoMostrar = progresoService.obtenerProgresoPorId(id);
		
		if (progresoMostrar.isPresent()
				&& alumnoUsuario.getProgresos().contains(progresoMostrar.get())) {
			model.addAttribute("progresoMostrar", progresoMostrar.get());
			 String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isAlumnoProgresoUrl = urlActual.startsWith("/progresoAlumno/" + id);
	        model.addAttribute("isAlumnoProgresoUrl", isAlumnoProgresoUrl);
			model.addAttribute("miUsuario", usuarioLog);

			return "progresoVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe progreso con id " + id);
		}

		

		return "redirect:/progresoAlumno";

	}

}