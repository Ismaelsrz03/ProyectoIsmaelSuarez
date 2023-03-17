package principal.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Rutina;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;

@RequestMapping("/ejercicios")
@Controller
public class EjercicioController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;

	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@GetMapping(value= {"","/"})
	String homeejercicios(Model model) {
		
		// Salir a buscar a la BBDD
		
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		
		
		return "ejercicios";
	}
	
	@PostMapping("/edit/{id}")
	public String editarejercicio(@PathVariable Integer id, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado, BindingResult bindingresult) {
		
		Ejercicio ejercicioaEditar = ejercicioService.obtenerEjercicioPorID(id);
		
		ejercicioaEditar.setNombre(ejercicioEditado.getNombre());
		
		ejercicioService.insertarEjercicio(ejercicioEditado);
		
		return "redirect:/ejercicios";
	}
	
	@PostMapping("/add")
	public String addejercicio(@ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew, BindingResult bindingresult,Integer id) {
		
		ejercicioService.insertarEjercicio(ejercicioNew);
		
		return "redirect:/ejercicios";
	}
	
	@GetMapping("/delete/{id}")
	String deleteejercicio(Model model, @PathVariable Integer id) {
		
		Ejercicio ejer = ejercicioService.obtenerEjercicioPorID(id);
		
		for(Alumno a: ejer.getAlumnos()) {
			a.getEjercicios().remove(ejer);
			a.getEjercicios().add(null);
		}
		
		for(Entrenador e: ejer.getEntrenadores()) {
			e.getEjercicios().remove(ejer);
			e.getEjercicios().add(null);
		}
		
		for(Rutina r: ejer.getRutinas()) {
			r.getEjercicios().remove(ejer);
			r.getEjercicios().add(null);
		}
		
		ejercicioService.eliminarEjercicioPorId(id);

		
		return "redirect:/ejercicios";
	}
	
}
