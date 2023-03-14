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
import principal.modelo.Usuario;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/alumnos")
@Controller
public class AlumnoController {

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
	
	@GetMapping(value= {"","/"})
	String homealumnos(Model model) {
		
		// Salir a buscar a la BBDD
		
		ArrayList<Alumno> misalumnos = (ArrayList<Alumno>) alumnoService.listarAlumnos();
		ArrayList<Entrenador> misentrenadores = (ArrayList<Entrenador>) entrenadorService.listarEntrenadors();
		ArrayList<Ejercicio> misejercicios = (ArrayList<Ejercicio>) ejercicioService.listarEjercicios();
		ArrayList<Rutina> misrutinas = (ArrayList<Rutina>) rutinaService.listarRutinas();
		
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("alumnoaEditar", new Alumno());
		model.addAttribute("alumnoNuevo", new Alumno());
		
		
		return "alumnos";
	}
	
	@PostMapping("/edit/{id}")
	public String editarAlumno(@PathVariable Integer id, @ModelAttribute("alumnoaEditar") Alumno alumnoEditado, BindingResult bindingresult) {
		
		Alumno alumnoaEditar = alumnoService.obtenerAlumnoPorID(id);
		
		alumnoaEditar.setNombre(alumnoEditado.getNombre());
		
		if (alumnoEditado.getEntrenadores() != null) {
			Entrenador e = entrenadorService.obtenerEntrenadorPorID(alumnoEditado.getEntrenadores().getId());
			alumnoEditado.setEntrenadores(e);
		} else {
			alumnoEditado.setEntrenadores(null);
		}
		
		if (alumnoaEditar.getUsuarios() != null) {
			Usuario u = usuarioService.obtenerUsuarioPorID(alumnoaEditar.getUsuarios().getId());
			alumnoEditado.setUsuarios(u);
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
	public String addAlumno(@ModelAttribute("alumnoNuevo") Alumno alumnoNew, BindingResult bindingresult,Integer id) {
		
		  if (alumnoNew.getEntrenadores() != null) { 
		        Entrenador entrenadorNuevo = entrenadorService.obtenerEntrenadorPorID(alumnoNew.getEntrenadores().getId());
		        entrenadorNuevo.getAlumnos().add(alumnoNew);
		        alumnoNew.setEntrenadores(entrenadorNuevo);
		    }
		
		for(Ejercicio e: alumnoNew.getEjercicios()) {
			Ejercicio e2 = e;
			e2.getAlumnos().add(alumnoNew);
		}
		
		for(Rutina r: alumnoNew.getRutinas()) {
			Rutina r2 = r;
			r2.getAlumnos().add(alumnoNew);
		}

		alumnoService.insertarAlumno(alumnoNew);
		
		return "redirect:/alumnos";
	}
	
	@GetMapping({"/{id}"})
	String idAlumno(Model model, @PathVariable Integer id) {
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumnoMostrar",alumnoMostrar);
		
		
		return "alumno";
	}
	
	@GetMapping("/delete/{id}")
	String deleteAlumno(Model model, @PathVariable Integer id) {
		
		alumnoService.eliminarAlumnoPorId(id);

		
		return "redirect:/alumnos";
	}
	
}
