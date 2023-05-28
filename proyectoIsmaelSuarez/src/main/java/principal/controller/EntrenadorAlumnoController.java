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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@RequestMapping("/entrenadorAlumno")
@Controller
public class EntrenadorAlumnoController {

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
	
	private Alumno alumnoUsuario;
	
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
		ArrayList<Usuario> listUsu = new ArrayList<Usuario>();
				ArrayList<Alumno> libres = new ArrayList<Alumno>();
				
				for(Alumno al : misalumnos) {
					if(al.getEntrenadores()==null) {
					libres.add(al);
					}
				
				
				
			
		}
				model.addAttribute("libre",libres);
				
				for(Alumno a: entrenadorUsuario.getAlumnos()) {
					
					Usuario u = usuarioService.obtenerUsuarioPorID(a.getUsuarios().getId());
					listUsu.add(u);
					
				}
				
				model.addAttribute("usu",listUsu);
	
		model.addAttribute("listaalumnos", misalumnos);
		model.addAttribute("listaEntrenadores",misentrenadores);
		model.addAttribute("listaEjercicios",misejercicios);
		model.addAttribute("listaRutinas",misrutinas);
		model.addAttribute("listaUsuarios",misusuarios);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("rutinaaEditar", new Rutina());
		model.addAttribute("rutinaNuevo", new Rutina());
		model.addAttribute("entrenadorNuevo", new Entrenador());
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("misAlumnos", entrenadorUsuario.getAlumnos());
		model.addAttribute("miUsuario",usuarioLog);
		return "entrenadorAlumno";
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
	
	@PostMapping("/add")
	public String buscarAlumnos(@ModelAttribute("entrenadorNuevo") Entrenador entrenadorNew, BindingResult bindingresult,Integer id) {
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		for(Alumno a: entrenadorNew.getAlumnos()) {
			if(a.getEntrenadores()==null) {
			a.setEntrenadores(entrenadorUsuario);
			alumnoService.insertarAlumno(a);
			}
		}
		
		
		entrenadorService.insertarEntrenador(entrenadorUsuario);
		
		return "redirect:/entrenadorAlumno";
	}
	

	@RequestMapping(value = "/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	String idAlumno(Model model, @PathVariable Integer id) {
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		model.addAttribute("alumno",alumnoMostrar);
		model.addAttribute("miUsuario",usuarioLog);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("rutinaaEditar", new Rutina());
		model.addAttribute("rutinaNuevo", new Rutina());
		model.addAttribute("misEjercicios",alumnoMostrar.getEjercicios());
		model.addAttribute("misRutinas",alumnoMostrar.getRutinas());
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("id", id);
		return "alumnoVer";
	}
	
	@PostMapping("{id}/editEjer/{ejerId}")
	public RedirectView editarejercicio(@PathVariable Integer id,@PathVariable Integer ejerId, @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado,
			BindingResult bindingresult, @RequestParam("file") MultipartFile file) {
		
		Ejercicio ejercicioaEditar = new Ejercicio();
		
		Alumno a = alumnoService.obtenerAlumnoPorID(id);
		
		ejercicioaEditar.setNombre(ejercicioEditado.getNombre());
		
		ejercicioaEditar.setSeries(ejercicioEditado.getSeries());
		ejercicioaEditar.setReps(ejercicioEditado.getReps());
		ejercicioaEditar.setDescripcion(ejercicioEditado.getDescripcion());
		if(!file.isEmpty()) {
			 
			 try {
				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 ejercicioEditado.setImagen(encodedString);
				 ejercicioEditado.setMimeType(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ejercicioaEditar.setImagen(ejercicioEditado.getImagen());
		ejercicioaEditar.setMimeType(ejercicioEditado.getMimeType());
		
		ArrayList<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		for(Ejercicio ee: a.getEjercicios()) {
			lista.add(ee);
		}
		Ejercicio aBorrar = new Ejercicio();
		for(Ejercicio ej: lista) {
			if(ej.getId()==ejerId) {
				aBorrar = ej;
				a.getEjercicios().add(ejercicioaEditar);
				
				for(Rutina r: a.getRutinas()) {
					r.getEjercicios().remove(aBorrar);
					r.getEjercicios().add(ejercicioaEditar);
				}
			}
		}
		
		a.getEjercicios().remove(aBorrar);
		
		
		alumnoService.insertarAlumno(a);
		
		return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@PostMapping("/{id}/editRutina/{rutinaId}")
	public RedirectView editarRutina(@PathVariable Integer id, @PathVariable Integer rutinaId, @ModelAttribute("rutinaaEditar") Rutina rutinaEditado, BindingResult bindingresult) {
		
		
		Alumno a = alumnoService.obtenerAlumnoPorID(id);
		
		Rutina r = new Rutina();
		r.setNombre(rutinaEditado.getNombre());
		Rutina aBorrar = new Rutina();
		
		ArrayList<Ejercicio> lista = new ArrayList<Ejercicio>();
		
		for(Ejercicio ee: rutinaEditado.getEjercicios()) {
			lista.add(ee);
		}
		
		ArrayList<Rutina> lista2 = new ArrayList<Rutina>();
		
		for(Rutina rut: a.getRutinas()) {
			lista2.add(rut);
		}
		
		
		
		for(Rutina ru: lista2) {
			if(ru.getId()==rutinaId) {
				aBorrar = ru;
				a.getRutinas().add(r);		
				
			}
		}
		
		for(Ejercicio e: lista) {
					r.getEjercicios().add(e);
				}
		
		a.getRutinas().remove(aBorrar);
		
		alumnoService.insertarAlumno(a);
		return new RedirectView("/entrenadorAlumno/{id}", true);
	}

	@PostMapping("/{id}/addEjer")
	public RedirectView addEjercicio(Model model, @ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew,
	                                 BindingResult bindingresult, @PathVariable Integer id,
	                                 @RequestParam("file") MultipartFile file) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id);

	    if (!file.isEmpty()) {
	        try {
	            byte[] imageBytes = file.getBytes();
	            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
	            ejercicioNew.setImagen(encodedString);
	            ejercicioNew.setMimeType(file.getContentType());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    Ejercicio nuevoEjercicio = new Ejercicio();
	    nuevoEjercicio.setNombre(ejercicioNew.getNombre());
	    nuevoEjercicio.setImagen(ejercicioNew.getImagen());
	    nuevoEjercicio.setMimeType(ejercicioNew.getMimeType());
	    nuevoEjercicio.setSeries(ejercicioNew.getSeries());
	    nuevoEjercicio.setReps(ejercicioNew.getReps());
	    nuevoEjercicio.setDescripcion(ejercicioNew.getDescripcion());

	    a.getEjercicios().add(nuevoEjercicio);
	    alumnoService.insertarAlumno(a);

	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}

	
	@PostMapping("/{id}/addRutina")
	public RedirectView addRutina(@PathVariable Integer id, @ModelAttribute("rutinaNuevo") Rutina rutinaNew, BindingResult bindingresult) {
	   
		Alumno a = alumnoService.obtenerAlumnoPorID(id);
		
		Rutina rutinaNueva = new Rutina();
		rutinaNueva.setNombre(rutinaNew.getNombre());
		
	    for(Ejercicio e: rutinaNew.getEjercicios()) {
	    	rutinaNueva.getEjercicios().add(e);
	    }
	
	    a.getRutinas().add(rutinaNueva);
	    alumnoService.insertarAlumno(a);
	
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping("/delete/{id}")
	String deleteAlumno(Model model, @PathVariable Integer id) {
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		Alumno al = alumnoService.obtenerAlumnoPorID(id);

		al.setEntrenadores(null);
		entrenadorService.insertarEntrenador(entrenadorUsuario);
		
		return "redirect:/entrenadorAlumno";
	}
	
	@GetMapping("{id}/deleteEjer/{ejerId}")
	RedirectView deleteejercicio(Model model, @PathVariable Integer id, @PathVariable Integer ejerId) {
		
		Ejercicio ejer = ejercicioService.obtenerEjercicioPorID(ejerId);
		Alumno al = alumnoService.obtenerAlumnoPorID(id);
		
		for(Alumno a: ejer.getAlumnos()) {
			a = alumnoService.obtenerAlumnoPorID(id);
			a.getEjercicios().remove(ejer);
			a.getEjercicios().add(null);
		}

		
		for(Rutina r: al.getRutinas()) {
			r.getEjercicios().remove(ejer);
			r.getEjercicios().add(null);
		}
		
		ejercicioService.eliminarEjercicioPorId(ejerId);

		
		return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping("{id}/deleteRutina/{rutinaId}")
	RedirectView deleteRutina(Model model, @PathVariable Integer id, @PathVariable Integer rutinaId) {
		Rutina rut = rutinaService.obtenerRutinaPorID(rutinaId);
		
		for(Alumno a: rut.getAlumnos()) {
			a = alumnoService.obtenerAlumnoPorID(id);
			a.getRutinas().remove(rut);
			a.getRutinas().add(null);
		}
		
		rutinaService.eliminarRutinaPorId(rutinaId);
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
}