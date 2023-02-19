package principal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Alumno;
import principal.modelo.Rutina;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;

@RequestMapping("/entrenadores")
@Controller
public class EntrenadorController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;
	
	@Autowired
	private RutinaServiceImpl rutinaService;
		
		@GetMapping(value= {"","/"})
		String homeentrenadores(Model model) {
			
			// Salir a buscar a la BBDD
			
			ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
			ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
			ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
			ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
			
			model.addAttribute("listaalumnos", misalumnos);
			model.addAttribute("listaEntrenadores",misentrenadores);
			model.addAttribute("listaEjercicios",misejercicios);
			model.addAttribute("listaRutinas",misrutinas);
			model.addAttribute("entrenadoraEditar", new Entrenador());
			model.addAttribute("entrenadorNuevo", new Entrenador());
			
			
			return "entrenadores";
		}
		
		@PostMapping("/edit/{id}")
		public String editarEntrenador(@PathVariable Integer id, @ModelAttribute("entrenadoraEditar") Entrenador entrenadorEditado, BindingResult bindingresult) {
			
			Entrenador entrenadoraEditar = entrenadorService.obtenerEntrenadorPorID(id);
			
			entrenadoraEditar.setNombre(entrenadorEditado.getNombre());
			
			for(Alumno a:entrenadoraEditar.getAlumnos()) {
				if(!entrenadorEditado.getAlumnos().contains(a)) {
					a.setEntrenadores(null);
				}
			}
			for(Alumno a:entrenadorEditado.getAlumnos()) {
				if(!entrenadoraEditar.getAlumnos().contains(a)) {
					if(a.getEntrenadores()==null) {
					a.setEntrenadores(entrenadorEditado);
					}
				}
			}
			
			for(Ejercicio ej:entrenadoraEditar.getEjercicios()) {
				if(!entrenadorEditado.getEjercicios().contains(ej)) {
					ej.getEntrenadores().remove(entrenadoraEditar);
				}
			}
			
			for(Ejercicio ej:entrenadorEditado.getEjercicios()) {
				if(!entrenadoraEditar.getEjercicios().contains(ej)) {
					ej.getEntrenadores().add(entrenadorEditado);
				}
			}
			
			for(Rutina r:entrenadoraEditar.getRutinas()) {
				if(!entrenadorEditado.getRutinas().contains(r)) {
					r.getEntrenadores().remove(entrenadoraEditar);
				}
			}
			
			for(Rutina r:entrenadorEditado.getRutinas()) {
				if(!entrenadoraEditar.getRutinas().contains(r)) {
					r.getEntrenadores().add(entrenadorEditado);
				}
			}
			
			entrenadorService.insertarEntrenador(entrenadoraEditar);
			
			return "redirect:/entrenadores";
		}
		
		@PostMapping("/add")
		public String addEntrenador(@ModelAttribute("entrenadorNuevo") Entrenador entrenadorNew, BindingResult bindingresult,Integer id) {
			
			for(Alumno a: entrenadorNew.getAlumnos()) {
				Alumno a2 = a;
				if(a2.getEntrenadores()==null) {
				a2.setEntrenadores(entrenadorNew);
				}
			}
			
			for(Ejercicio e: entrenadorNew.getEjercicios()) {
				Ejercicio e2 = e;
				e2.getEntrenadores().add(entrenadorNew);
			}
			
			for(Rutina r: entrenadorNew.getRutinas()) {
				Rutina r2 = r;
				r2.getEntrenadores().add(entrenadorNew);
			}
			
			entrenadorService.insertarEntrenador(entrenadorNew);
			
			return "redirect:/entrenadores";
		}
		
		@GetMapping({"/{id}"})
		String idEntrenador(Model model, @PathVariable Integer id) {
			
			Entrenador entrenadorMostrar = entrenadorService.obtenerEntrenadorPorID(id);
			model.addAttribute("entrenadorMostrar",entrenadorMostrar);
			
			
			return "entrenador";
		}
		
		@GetMapping("/delete/{id}")
		String deleteEntrenador(Model model, @PathVariable Integer id) {

			Entrenador en = entrenadorService.obtenerEntrenadorPorID(id);
			
			for(Alumno a : en.getAlumnos()) {
				a.setEntrenadores(null);
			};
			
			entrenadorService.eliminarEntrenadorPorId(id);
			
			return "redirect:/entrenadores";
		}

}
