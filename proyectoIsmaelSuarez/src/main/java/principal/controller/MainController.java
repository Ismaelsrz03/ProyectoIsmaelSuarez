package principal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Rutina;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;

@Controller
public class MainController {

	@GetMapping("/")
	String home() {
//		crearTablas();
		return "index";
		
	}
	
	@GetMapping("/entrenadores")
	String homeentrenadores() {
		
		return "entrenadores";
		
	}
	
	@GetMapping("/rutinas")
	String homerutinas() {
		
		return "rutinas";
		
	}
	
	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;
	
	@Autowired
	private RutinaServiceImpl rutinaService;

	public void crearTablas() {
		
		Alumno a1 = new Alumno("Ismael");
		Alumno a2 = new Alumno("Juan");
		Alumno a3 = new Alumno("Ana");
		
		Ejercicio ej1 = new Ejercicio("Press banca");
		Ejercicio ej2 = new Ejercicio("Jalón al pecho");
		Ejercicio ej3 = new Ejercicio("Sentadillas");
		
		Entrenador en1 = new Entrenador("Mario");
		Entrenador en2 = new Entrenador("Luis");
		
		Rutina r1 = new Rutina("Full body");
		Rutina r2 = new Rutina("Torso pierna");
		
		en1.getAlumnos().add(a1);
		en1.getAlumnos().add(a2);
		en2.getAlumnos().add(a3);
		
		a1.setEntrenadores(en1);
		a2.setEntrenadores(en1);
		a3.setEntrenadores(en2);
		
		ej1.getAlumnos().add(a1);
		ej1.getAlumnos().add(a3);
		ej2.getAlumnos().add(a1);
		ej2.getAlumnos().add(a2);
		ej3.getAlumnos().add(a1);
		ej3.getAlumnos().add(a2);
		
		a1.getEjercicios().add(ej1);
		a1.getEjercicios().add(ej2);
		a1.getEjercicios().add(ej3);
		a2.getEjercicios().add(ej2);
		a2.getEjercicios().add(ej3);
		a3.getEjercicios().add(ej1);
		
		ej1.getRutinas().add(r1);
		ej2.getRutinas().add(r1);
		ej3.getRutinas().add(r1);
		ej1.getRutinas().add(r2);
		ej3.getRutinas().add(r2);
		
		r1.getEjercicios().add(ej1);
		r1.getEjercicios().add(ej2);
		r1.getEjercicios().add(ej3);
		r2.getEjercicios().add(ej1);
		r2.getEjercicios().add(ej3);
		
		r1.getAlumnos().add(a1);
		
		a1.getRutinas().add(r1);
		
		r1.getEntrenadores().add(en1);
		
		en1.getRutinas().add(r1);
		
		en1.getEjercicios().add(ej1);
		en1.getEjercicios().add(ej2);
		en1.getEjercicios().add(ej3);
		en2.getEjercicios().add(ej1);
		
		ej1.getEntrenadores().add(en1);
		ej2.getEntrenadores().add(en1);
		ej3.getEntrenadores().add(en1);
		ej1.getEntrenadores().add(en2);
		
		entrenadorService.insertarEntrenador(en1);
		entrenadorService.insertarEntrenador(en2);
		
		alumnoService.insertarAlumno(a1);
		alumnoService.insertarAlumno(a2);
		alumnoService.insertarAlumno(a3);
		
		rutinaService.insertarRutina(r1);
		rutinaService.insertarRutina(r2);
		
		ejercicioService.insertarEjercicio(ej1);
		ejercicioService.insertarEjercicio(ej2);
		ejercicioService.insertarEjercicio(ej3);
		
		
	}
	
}
