package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
	
	@Autowired
	private ProgresoServiceImpl progresoService;
	
	@Autowired
	private NotaServiceImpl notaService;
	
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
					
					Usuario u = usuarioService.obtenerUsuarioPorID(a.getUsuarios().getId()).get();
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
	String idAlumno(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
		usuarioLog= obtenerLog();
		entrenadorUsuario = obtenerEntrenadorDeUsuario();
		Optional<Alumno> alumnoMostrar = alumnoService.obtenerAlumnoPorID(id);
		if(alumnoMostrar.isPresent()) {
		Alumno al = alumnoService.obtenerAlumnoPorID(id).get();
		model.addAttribute("alumno",alumnoMostrar.get());
		model.addAttribute("miUsuario",usuarioLog);
		model.addAttribute("ejercicioaEditar", new Ejercicio());
		model.addAttribute("ejercicioNuevo", new Ejercicio());
		model.addAttribute("notaaEditar", new Nota());
		model.addAttribute("notaNuevo", new Nota());
		model.addAttribute("rutinaaEditar", new Rutina());
		model.addAttribute("rutinaNuevo", new Rutina());
		model.addAttribute("misEjercicios",al.getEjercicios());
		model.addAttribute("misRutinas",al.getRutinas());
		model.addAttribute("miEntrenador",entrenadorUsuario);
		model.addAttribute("id", id);
		ArrayList<Usuario> listUsu = new ArrayList<Usuario>();
		Alumno alu = null;
		for(Alumno a: entrenadorUsuario.getAlumnos()) {
			alu = a;
			Usuario u = usuarioService.obtenerUsuarioPorID(a.getUsuarios().getId()).get();
			listUsu.add(u);
			
		}
		
		model.addAttribute("usu",listUsu);
		model.addAttribute("usuAlum",alu);
		
		return "alumnoVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
		return "redirect:/entrenadorAlumno";
	}
	
	@PostMapping("{id}/editEjer/{ejerId}")
	public RedirectView editarejercicio(@PathVariable Integer id, @PathVariable Integer ejerId,
	                                    @ModelAttribute("ejercicioaEditar") Ejercicio ejercicioEditado,
	                                    BindingResult bindingresult, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

	    Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);

	    if(alumno.isPresent()) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
	    
	    Ejercicio ejercicioaEditar = null;

	    for (Ejercicio ej : a.getEjercicios()) {
	        if (ej.getId().equals(ejerId)) {
	            ejercicioaEditar = ej;
	            break;
	        }
	    }

	    if (ejercicioaEditar == null) {
	        // Manejar el caso en el que no se encuentre el ejercicio
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return new RedirectView("/entrenadorAlumno/{id}", true);
	    }

	    ejercicioaEditar.setNombre(ejercicioEditado.getNombre());
	    ejercicioaEditar.setSeries(ejercicioEditado.getSeries());
	    ejercicioaEditar.setReps(ejercicioEditado.getReps());
	    ejercicioaEditar.setDescripcion(ejercicioEditado.getDescripcion());

	    if (!file.isEmpty()) {
	        try {
	            byte[] imageBytes = file.getBytes();
	            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
	            ejercicioaEditar.setImagen(encodedString);
	            ejercicioaEditar.setMimeType(file.getContentType());
	        } catch (IOException e) {
	            // Manejar la excepción de lectura del archivo
	            e.printStackTrace();
	        }
	    }

	    ArrayList<Rutina> rutinasActualizadas = new ArrayList<>();

	    for (Rutina r : a.getRutinas()) {
	        if (r.getEjercicios().contains(ejercicioaEditar)) {
	            r.getEjercicios().remove(ejercicioaEditar);
	            r.getEjercicios().add(ejercicioaEditar);
	            rutinasActualizadas.add(r);
	        }
	    }

	    a.getEjercicios().remove(ejercicioaEditar);
	    a.getEjercicios().add(ejercicioaEditar);

	    alumnoService.insertarAlumno(a);

	    return new RedirectView("/entrenadorAlumno/{id}", true);
	    
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}

	
	@PostMapping("/{id}/editRutina/{rutinaId}")
	public RedirectView editarRutina(@PathVariable Integer id, @PathVariable Integer rutinaId,
	                                 @ModelAttribute("rutinaaEditar") Rutina rutinaEditado,
	                                 BindingResult bindingresult, RedirectAttributes redirectAttributes) {
	    Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);

	    if(alum.isPresent()) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
	    Rutina r = null;

	    for (Rutina ru : a.getRutinas()) {
	        if (ru.getId().equals(rutinaId)) {
	            r = ru;
	            break;
	        }
	    }

	    if (r == null) {
	        // Manejar el caso en el que no se encuentre la rutina
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return new RedirectView("/entrenadorAlumno/{id}", true);
	    }

	    r.setNombre(rutinaEditado.getNombre());
	    r.getEjercicios().clear(); // Limpiar la lista de ejercicios existente en la rutina

	    for (Ejercicio e : rutinaEditado.getEjercicios()) {
	    	Ejercicio copiaEjercicio = new Ejercicio();
	    	copiaEjercicio.setNombre(e.getNombre());
            copiaEjercicio.setDescripcion(e.getDescripcion());
            copiaEjercicio.setSeries(e.getSeries());
            copiaEjercicio.setReps(e.getSeries());
            copiaEjercicio.setImagen(e.getImagen());
            copiaEjercicio.setMimeType(e.getMimeType());
            // Copiar otros atributos relevantes del ejercicioç
            ejercicioService.insertarEjercicio(copiaEjercicio);
	    	r.getEjercicios().add(copiaEjercicio);
	    }

	    a.getRutinas().remove(r);
	    a.getRutinas().add(r);

	    alumnoService.insertarAlumno(a);
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	    } else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}


	@PostMapping("/{id}/addEjer")
	public RedirectView addEjercicio(Model model, @ModelAttribute("ejercicioNuevo") Ejercicio ejercicioNew,
	                                 BindingResult bindingresult, @PathVariable Integer id,
	                                 @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
	    Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);

	    if(alum.isPresent()) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
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
	    } else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}

	
	@PostMapping("/{id}/addRutina")
	public RedirectView addRutina(@PathVariable Integer id, @ModelAttribute("rutinaNuevo") Rutina rutinaNew, 
			BindingResult bindingresult, RedirectAttributes redirectAttributes) {
	   
		Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);
		
		if(alum.isPresent()) {
		Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
		Rutina rutinaNueva = new Rutina();
		rutinaNueva.setNombre(rutinaNew.getNombre());
		
	    for(Ejercicio e: rutinaNew.getEjercicios()) {
	    	Ejercicio copiaEjercicio = new Ejercicio();
	    	copiaEjercicio.setNombre(e.getNombre());
            copiaEjercicio.setDescripcion(e.getDescripcion());
            copiaEjercicio.setSeries(e.getSeries());
            copiaEjercicio.setReps(e.getSeries());
            copiaEjercicio.setImagen(e.getImagen());
            copiaEjercicio.setMimeType(e.getMimeType());
            // Copiar otros atributos relevantes del ejercicioç
            ejercicioService.insertarEjercicio(copiaEjercicio);
	    	rutinaNueva.getEjercicios().add(copiaEjercicio);
	    }
	
	    a.getRutinas().add(rutinaNueva);
	    alumnoService.insertarAlumno(a);
	
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping("/delete/{id}")
	String deleteAlumno(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);
	    Entrenador en = entrenadorService.obtenerEntrenadorPorID(entrenadorUsuario.getId()).get();
	    if (alum.isPresent()) {
	        Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
	        if (en.getAlumnos().contains(alum.get())) {

	            Iterator<Ejercicio> ejIterator = a.getEjercicios().iterator();
	            while (ejIterator.hasNext()) {
	                Ejercicio ej = ejIterator.next();
	                ejIterator.remove();
	            }

	            Iterator<Rutina> rutinaIterator = a.getRutinas().iterator();
	            while (rutinaIterator.hasNext()) {
	                Rutina r = rutinaIterator.next();
	                rutinaIterator.remove();
	            }
	            
	            Iterator<Nota> notaIterator = a.getNotas().iterator();
	            while (notaIterator.hasNext()) {
	                Nota n = notaIterator.next();
	                notaIterator.remove();
	            }


	            a.setEntrenadores(null);
	            alumnoService.insertarAlumno(a);
	            entrenadorService.insertarEntrenador(entrenadorUsuario);

	            return "redirect:/entrenadorAlumno";
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
	    }

	    return "redirect:/entrenadorAlumno";
	}

	
	@GetMapping("{id}/deleteEjer/{ejerId}")
	RedirectView deleteejercicio(Model model, @PathVariable Integer id, @PathVariable Integer ejerId, RedirectAttributes redirectAttributes) {
		
		Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(ejerId);
		Optional<Alumno> al = alumnoService.obtenerAlumnoPorID(id);
		if(ejer.isPresent() && al.isPresent()) {
			Alumno alum = alumnoService.obtenerAlumnoPorID(id).get();
		if(alum.getEjercicios().contains(ejer.get())) {
		Ejercicio e = ejercicioService.obtenerEjercicioPorID(ejerId).get();
		for(Alumno a: e.getAlumnos()) {
			a = alumnoService.obtenerAlumnoPorID(id).get();
			a.getEjercicios().remove(e);
		
		}

		
		for(Rutina r: alum.getRutinas()) {
			r.getEjercicios().remove(e);
		}
		
		ejercicioService.eliminarEjercicioPorId(ejerId);

		
		return new RedirectView("/entrenadorAlumno/{id}", true);
		}
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + ejerId);
		}
		
		return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping("{id}/deleteRutina/{rutinaId}")
	RedirectView deleteRutina(Model model, @PathVariable Integer id, @PathVariable Integer rutinaId, RedirectAttributes redirectAttributes) {
		Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(rutinaId);
		Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);
		if(rut.isPresent() && alum.isPresent()) {
		Rutina r = rutinaService.obtenerRutinaPorID(rutinaId).get();
		Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
		if(a.getRutinas().contains(rut.get())) {
		
		for(Alumno al: r.getAlumnos()) {
			al = alumnoService.obtenerAlumnoPorID(id).get();
			al.getRutinas().remove(r);
			al.getRutinas().add(null);
		}
		
		rutinaService.eliminarRutinaPorId(rutinaId);
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	} else {
		redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe rutina con id " + rutinaId);
	}
	
	return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping({"/{id}/{ejerId}"})
	String verEjercicioAlumno(Model model, @PathVariable Integer id, @PathVariable Integer ejerId, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id).get();
		
		Optional<Ejercicio> ejerMostrar = ejercicioService.obtenerEjercicioPorID(ejerId);
		model.addAttribute("alumno",alumnoMostrar);
		if (ejerMostrar.isPresent()
				&& alumnoMostrar.getEjercicios().contains(ejerMostrar.get())) {
			model.addAttribute("ejerMostrar", ejerMostrar.get());
			String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorAlumnoUrl = urlActual.startsWith("/entrenadorAlumno/" + id + "/" + ejerId);
	       
	        model.addAttribute("isEntrenadorAlumnoUrl", isEntrenadorAlumnoUrl);
	        
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "El alumno no tiene un ejercicio con id " + id);
		}

		

		return "redirect:/entrenadorAlumno/" +id;

	}
	
	@GetMapping({"/{id}/{progresoId}/verProgreso"})
	String verProgresoAlumno(Model model, @PathVariable Integer id, @PathVariable Integer progresoId, 
			RedirectAttributes redirectAttributes) {
		usuarioLog= obtenerLog();
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id).get();
		
		Optional<Progreso> progresoMostrar = progresoService.obtenerProgresoPorId(progresoId);
		model.addAttribute("alumno",alumnoMostrar);
		if (progresoMostrar.isPresent()
				&& alumnoMostrar.getProgresos().contains(progresoMostrar.get())) {
			model.addAttribute("progresoMostrar", progresoMostrar.get());
			model.addAttribute("miUsuario", usuarioLog);

			return "progresoVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "El alumno no tiene un progreso con id " + id);
		}

		

		return "redirect:/entrenadorAlumno/" +id;

	}
	
	@GetMapping({"/{id}/{rutinaId}/verRutina"})
	String verRutinaAlumno(Model model, @PathVariable Integer id, @PathVariable Integer rutinaId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
		
		Optional<Rutina> rutinaMostrar = rutinaService.obtenerRutinaPorID(rutinaId);
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id).get();
		
		model.addAttribute("alumno",alumnoMostrar);
		if (rutinaMostrar.isPresent() && alumnoMostrar.getRutinas().contains(rutinaMostrar.get())) {
			model.addAttribute("rutinaMostrar", rutinaMostrar.get());
			 String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorAlumnoRutinaUrl = urlActual.startsWith("/entrenadorAlumno/" + id + "/" + rutinaId + "/verRutina");
	        model.addAttribute("isEntrenadorAlumnoRutinaUrl", isEntrenadorAlumnoRutinaUrl);
			model.addAttribute("miUsuario", usuarioLog);
			model.addAttribute("miEntrenador",entrenadorUsuario);
			return "rutinaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "El alumno no tiene una rutina con id " + id);
		}

		

		return "redirect:/entrenadorAlumno/" +id;

	}
	
	@GetMapping({"/{id}/{rutinaId}/verRutina/{ejerId}"})
	String verEjercicioRutinaAlumno(Model model, @PathVariable Integer id, @PathVariable Integer ejerId, @PathVariable Integer rutinaId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Rutina rutinaMostrar = rutinaService.obtenerRutinaPorID(rutinaId).get();
		
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id).get();
		model.addAttribute("alumno",alumnoMostrar);
		
		Optional<Ejercicio> ejerMostrar = ejercicioService.obtenerEjercicioPorID(ejerId);
		model.addAttribute("rutinaMostrar",rutinaMostrar);
		if (ejerMostrar.isPresent()
				&& rutinaMostrar.getEjercicios().contains(ejerMostrar.get()) && alumnoMostrar.getRutinas().contains(rutinaMostrar)) {
			model.addAttribute("ejerMostrar", ejerMostrar.get());
			String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorAlumnoRutinaEjercicioUrl = urlActual.startsWith("/entrenadorAlumno/" + id + "/" + rutinaId + "/verRutina/" +ejerId);
	       
	        model.addAttribute("isEntrenadorAlumnoRutinaEjercicioUrl", isEntrenadorAlumnoRutinaEjercicioUrl);
	        
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "ejercicioVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "La rutina no tiene un ejercicio con id " + id);
		}

		

		return "redirect:/entrenadorRutina/" +id;

	}
	
	@GetMapping("/{id}/{rutinaId}/verRutina/quitar/{ejerId}")
	RedirectView quitarEjercicioRutina(Model model, @PathVariable Integer id, @PathVariable Integer ejerId, @PathVariable Integer rutinaId,
	                                  RedirectAttributes redirectAttributes) {
	    Optional<Rutina> rut = rutinaService.obtenerRutinaPorID(rutinaId);
	    Optional<Ejercicio> ejer = ejercicioService.obtenerEjercicioPorID(ejerId);
	    Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);
	    
	    if (rut.isPresent() && ejer.isPresent() && alum.get().getRutinas().contains(rut.get())
	    		&& rut.get().getEjercicios().contains(ejer.get())) {
	        Rutina r = rut.get();
	        Ejercicio ejercicio = ejer.get();
	        
	        if (r.getEjercicios().contains(ejercicio)) {
	            r.getEjercicios().remove(ejercicio);
	            ejercicio.getRutinas().remove(r);
	            
	            rutinaService.insertarRutina(r);
	            return new RedirectView("/entrenadorAlumno/{id}/{rutinaId}/verRutina", true);
	        }
	    }  else {
	            redirectAttributes.addFlashAttribute("fallo", "La rutina no contiene un ejercicio con ID " + ejerId);
	        }
	    
	    return new RedirectView("/entrenadorAlumno/{id}/{rutinaId}/verRutina", true);
	}
	
	@PostMapping("/{id}/{rutinaId}/verRutina/agregarEjercicio")
	public String agregarEjercicio(Model model, @PathVariable Integer id, @PathVariable Integer rutinaId, @RequestParam("ejerciciosId") Integer[] ejerciciosId) {
	    Optional<Rutina> rutina = rutinaService.obtenerRutinaPorID(rutinaId);
	    Entrenador entrenadorUsuario = obtenerEntrenadorDeUsuario();
	    Alumno alumno = alumnoService.obtenerAlumnoPorID(id).get();
	    model.addAttribute("miEntrenador", entrenadorUsuario);
	    model.addAttribute("rutinaMostrar",rutina);
	    
	    Entrenador entrenador = entrenadorService.obtenerEntrenadorPorID(entrenadorUsuario.getId()).get();
	    if (rutina.isPresent() && alumno.getRutinas().contains(rutina.get())) {
	        Rutina rutinaActualizada = rutina.get();
	        for(Integer ejercicioId: ejerciciosId) {
	        Ejercicio ejercicio = ejercicioService.obtenerEjercicioPorID(ejercicioId).get(); // Obtener el ejercicio seleccionado
	        
	            rutinaActualizada.getEjercicios().add(ejercicio);

	        }
	        rutinaService.insertarRutina(rutinaActualizada);
	    }

	    return "redirect:/entrenadorAlumno/" + id + "/" + rutinaId + "/verRutina";
	}
	
	@PostMapping("{id}/editNota/{notaId}")
	public RedirectView editarNota(@PathVariable Integer id, @PathVariable Integer notaId,
	                                    @ModelAttribute("notaaEditar") Nota notaEditado, RedirectAttributes redirectAttributes) {

	    Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorID(id);

	    if(alumno.isPresent()) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
	    
	    Nota notaaEditar = null;

	    for (Nota n : a.getNotas()) {
	        if (n.getId().equals(notaId)) {
	            notaaEditar = n;
	            break;
	        }
	    }

	    if (notaaEditar == null) {
	        // Manejar el caso en el que no se encuentre el ejercicio
	        // Puedes lanzar una excepción, mostrar un mensaje de error, etc.
	        return new RedirectView("/entrenadorAlumno/{id}", true);
	    }

	    notaaEditar.setFecha(new Date());
	    notaaEditar.setTitulo(notaEditado.getTitulo());
	    notaaEditar.setContenido(notaEditado.getContenido());

	    a.getNotas().remove(notaaEditar);
	    a.getNotas().add(notaaEditar);

	    alumnoService.insertarAlumno(a);

	    return new RedirectView("/entrenadorAlumno/{id}", true);
	    
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@PostMapping("/{id}/addNota")
	public RedirectView addNota(Model model, @ModelAttribute("notaNuevo") Nota notaNew,
	                                 BindingResult bindingresult, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
	    Optional<Alumno> alum = alumnoService.obtenerAlumnoPorID(id);

	    if(alum.isPresent()) {
	    Alumno a = alumnoService.obtenerAlumnoPorID(id).get();
	  

	    Nota nuevoNota = new Nota();
	    nuevoNota.setFecha(new Date());
	    nuevoNota.setTitulo(notaNew.getTitulo());
	    nuevoNota.setContenido(notaNew.getContenido());

	    a.getNotas().add(nuevoNota);
	    alumnoService.insertarAlumno(a);

	    return new RedirectView("/entrenadorAlumno/{id}", true);
	    } else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe alumno con id " + id);
		}
	    
	    return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping("{id}/deleteNota/{notaId}")
	RedirectView deletenota(Model model, @PathVariable Integer id, @PathVariable Integer notaId, RedirectAttributes redirectAttributes) {
		
		Optional<Nota> nota = notaService.obtenerNotaPorId(notaId);
		Optional<Alumno> al = alumnoService.obtenerAlumnoPorID(id);
		if(nota.isPresent() && al.isPresent()) {
			Alumno alum = alumnoService.obtenerAlumnoPorID(id).get();
		if(alum.getNotas().contains(nota.get())) {
		Nota n = notaService.obtenerNotaPorId(notaId).get();
		for(Alumno a: n.getAlumnos()) {
			a = alumnoService.obtenerAlumnoPorID(id).get();
			a.getNotas().remove(n);
		
		}

		
		notaService.eliminarNotaPorId(notaId);

		
		return new RedirectView("/entrenadorAlumno/{id}", true);
		}
		} else {
			redirectAttributes.addFlashAttribute("fallo", "En tu cuenta no existe ejercicio con id " + notaId);
		}
		
		return new RedirectView("/entrenadorAlumno/{id}", true);
	}
	
	@GetMapping({"/{id}/{notaId}/verNota"})
	String verNotaAlumno(Model model, @PathVariable Integer id, @PathVariable Integer notaId, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		usuarioLog= obtenerLog();
		Alumno alumnoMostrar = alumnoService.obtenerAlumnoPorID(id).get();
		
		Optional<Nota> nota = notaService.obtenerNotaPorId(notaId);
		model.addAttribute("alumno",alumnoMostrar);
		if (nota.isPresent()
				&& alumnoMostrar.getNotas().contains(nota.get())) {
			model.addAttribute("notaMostrar", nota.get());
			String urlActual = request.getRequestURI();
	        model.addAttribute("urlActual", urlActual);
	        
	        boolean isEntrenadorAlumnoUrl = urlActual.startsWith("/entrenadorAlumno/" + id + "/" + notaId);
	       
	        model.addAttribute("isEntrenadorAlumnoUrl", isEntrenadorAlumnoUrl);
	        
	        
			model.addAttribute("miUsuario", usuarioLog);

			return "notaVer";
		} else {
			redirectAttributes.addFlashAttribute("fallo", "El alumno no tiene una nota con id " + id);
		}

		

		return "redirect:/entrenadorAlumno/" +id;

	}
	
}