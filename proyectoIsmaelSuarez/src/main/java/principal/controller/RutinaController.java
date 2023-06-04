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

import principal.modelo.Ejercicio;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.modelo.Alumno;
import principal.modelo.Entrenador;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.AlumnoServiceImpl;

@RequestMapping("/rutinas")
@Controller
public class RutinaController {

	@Autowired
	private EjercicioServiceImpl ejercicioService;
	
	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	private Usuario miUsuario;
		
		@GetMapping(value= {"","/"})
		String homerutinas(Model model) {
			
			// Salir a buscar a la BBDD
			
			ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
			ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
			ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
			ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
			
			model.addAttribute("listaEjercicios", misejercicios);
			model.addAttribute("listaRutinas",misrutinas);
			model.addAttribute("listaEntrenadores", misentrenadores);
			model.addAttribute("listaalumnos",misalumnos);
			model.addAttribute("rutinaaEditar", new Rutina());
			model.addAttribute("rutinaNuevo", new Rutina());
			miUsuario = obtenerLog();
			model.addAttribute("miUsuario",miUsuario);
			
			return "rutinas";
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
		public String editarRutina(@PathVariable Integer id, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado, BindingResult bindingresult) {
			
			Rutina rutinaaEditar = rutinaService.obtenerRutinaPorID(id).get();
			
			rutinaaEditar.setNombre(rutinaEditado.getNombre());
			

			for(Ejercicio ej:rutinaaEditar.getEjercicios()) {
				if(!rutinaEditado.getEjercicios().contains(ej)) {
					ej.getRutinas().remove(rutinaaEditar);
				}
			}
			
			for(Ejercicio ej:rutinaEditado.getEjercicios()) {
				if(!rutinaaEditar.getEjercicios().contains(ej)) {
					ej.getRutinas().add(rutinaEditado);
				}
			}
			
			rutinaService.insertarRutina(rutinaEditado);
			
			return "redirect:/rutinas";
		}
		
		@PostMapping("/add")
		public String addRutina(@ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult,Integer id) {
			
			for(Ejercicio e: rutinaNew.getEjercicios()) {
				Ejercicio e2 = e;
				e2.getRutinas().add(rutinaNew);
			}
			
			rutinaService.insertarRutina(rutinaNew);
			
			return "redirect:/rutinas";
		}
		
		@GetMapping({"/{id}"})
		String idUsuario(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
			
			Optional<Rutina> rutinaMostrar = rutinaService.obtenerRutinaPorID(id);
			
			if(rutinaMostrar.isPresent()) {
			
			model.addAttribute("rutinaMostrar",rutinaMostrar.get());
			
			
			return "rutina";
			}  else {
				redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
			}
		return "redirect:/rutinas";
		}
		
		@GetMapping("/delete/{id}")
		String deleteRutina(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
			
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
				redirectAttributes.addFlashAttribute("fallo", "No existe ejercicio con id " + id);
			}
		return "redirect:/rutinas";
		}

}
