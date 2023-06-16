package principal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;
import principal.modelo.Rol;
import principal.modelo.Rutina;
import principal.modelo.Usuario;
import principal.modelo.dto.UsuarioDTO;
import principal.servicio.impl.AlumnoServiceImpl;
import principal.servicio.impl.EjercicioServiceImpl;
import principal.servicio.impl.EntrenadorServiceImpl;
import principal.servicio.impl.RolServiceImpl;
import principal.servicio.impl.RutinaServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@Controller
public class MainController {

	@Autowired
	private AlumnoServiceImpl alumnoService;
	
	@Autowired
	private EntrenadorServiceImpl entrenadorService;
	
	@Autowired
	private EjercicioServiceImpl ejercicioService;
	
	@Autowired
	private RutinaServiceImpl rutinaService;
	
	@Autowired
	private RolServiceImpl rolService;
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	private Usuario miUsuario;
	
	@GetMapping("/")
	String home(Model model, Authentication authentication) {
		
		
		if (usuarioService.listarUsuarios().isEmpty()) {
            crearTablas();
        }
		
		miUsuario = obtenerLog();
		model.addAttribute("miUsuario",miUsuario);
		
		 if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
	            return "redirect:/perfilAlumno";
	        } else {
	        	if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ENTRENADOR"))) {
	            return "redirect:/perfilEntrenador";
	        }
	            return "index"; // Cambia "index" por la página predeterminada en tu aplicación
	        }
		
		
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

	public void crearTablas() {
		
		Alumno a1 = new Alumno("Ismael","Suárez Limachi","ismaelsrz03@gmail.com","Hombre",19,175,70,"Llevo tiempo en el gimnasio y me gusta hacer deporte, tengo una operación de brazo pero no tengo problemas para hacer ejercicio","Busco una vida saludable y estar fuerte","@insta","@youtube","@tiktok","@twitter","@facebook");
		Alumno a2 = new Alumno("Daniela","Suárez Limachi","danielasrz03@gmail.com","Mujer",18,170,63,"Soy principiante en el deporte y no tengo mucha idea.","Busco estar saludable y disfrutar del deporte","@insta","@youtube","@tiktok","@twitter","@facebook");
		Alumno a3 = new Alumno("Juan","Menéndez López","juanklk23@gmail.com","Hombre",32,168,95,"Nunca he hecho ejercicio y me cuesta realizar ejercicio","Busco un cambio físico y bajar de peso","@insta","@youtube","@tiktok","@twitter","@facebook");
		
		Ejercicio ej1 = new Ejercicio("Press banca",3,12,"El press banca se trata de un ejercicio multiarticular (implica el movimiento de la articulación del hombro y codo) y es útil para mejorar la fuerza, potencia y resistencia de la musculatura de empuje del tren superior (pectoral, deltoides y tríceps).",null,null,"");
		Ejercicio ej2 = new Ejercicio("Jalón al pecho",3,12,"El jalón entrena principalmente el músculo ancho de la espalda (musculus latissiums dorsi) y las fibras inferiores del músculo del capó (musculus trapezius pars ascendens).",null,null,"");
		Ejercicio ej3 = new Ejercicio("Sentadillas",3,12,"El ejercicio consiste en flexionar las rodillas y bajar el cuerpo manteniendo la verticalidad, para luego regresar a una posición erguida. Por lo general, mientras realiza sentadillas, la persona sostiene algún peso con sus brazos o el cuello.",null,null,"");
		
		Entrenador en1 = new Entrenador("Mario","Fernández Sala","maritofs12@gamil.com","Hombre",26,"Licenciado en Ciencias del Deporte","Entrenador personal","Polideportivo Oñon Mieres, Asturias","Hola soy Mario y recientemente me he convertido en entrenador personal, aprenderemos juntos y tu objetivo también lo será el mío. Adecuaremos el plan a tus gustos y necesidades y remaremos juntos en la misma dirección. Mi objetivo es que aprendas a hacerlo tu solo para que puedas prescindir de mi y sepas hacer las cosas","@insta","@youtube","@tiktok","@twitter","@facebook");
		Entrenador en2 = new Entrenador("Ana","García Puente","anagarpu@gamil.com","Mujer",33,"Técnico Superior en Actividades Físicas y Animación Deportiva","Entrenadora personal","Ginmansio Fit, Madrid","Mi nombre es Ana García y lo que quiero sobre todo es ayudarte a conseguir tus metas y objetivos disfrutando del camino que tu hagas en este aprendizaje.","@insta","@youtube","@tiktok","@twitter","@facebook");
		
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
		
		Rol rol1 = new Rol("ROLE_ADMIN");
		Rol rol2 = new Rol("ROLE_USER");
		Rol rol3 = new Rol("ROLE_ENTRENADOR");
		
		UsuarioDTO user1DTO = new UsuarioDTO("admin","admin","admin123",null,null,null,"ekd@s.cxodf",null);
		
		Usuario admin = new Usuario(user1DTO.getUsername(),user1DTO.getNombre(),passwordEncoder.encode(user1DTO.getPassword()),user1DTO.getImagen(),user1DTO.getMimeType(),null,user1DTO.getCorreo(),null);
		
		UsuarioDTO u1DTO = new UsuarioDTO("Ismael2003",a1.getNombre(),"12345",null,null,a1.getApellidos(),a1.getCorreo(),a1.getSexo());
		UsuarioDTO u2DTO = new UsuarioDTO("Daniela7",a2.getNombre(),"12345",null,null,a2.getApellidos(),a2.getCorreo(),a2.getSexo());
		UsuarioDTO u3DTO = new UsuarioDTO("Juanlolo",a3.getNombre(),"12345",null,null,a3.getApellidos(),a3.getCorreo(),a3.getSexo());
		UsuarioDTO u4DTO = new UsuarioDTO("Mario EP",en1.getNombre(),"12345",null,null,en1.getApellidos(),en1.getCorreo(),en1.getSexo());		
		UsuarioDTO u5DTO = new UsuarioDTO("AnaGP 12",en2.getNombre(),"12345",null,null,en2.getApellidos(),en2.getCorreo(),en2.getSexo());
		
		Usuario u1 = new Usuario(u1DTO.getUsername(),u1DTO.getNombre(),passwordEncoder.encode(u1DTO.getPassword()),u1DTO.getImagen(),u1DTO.getMimeType(),u1DTO.getApellidos(),u1DTO.getCorreo(),u1DTO.getSexo());
		Usuario u2 = new Usuario(u2DTO.getUsername(),u2DTO.getNombre(),passwordEncoder.encode(u2DTO.getPassword()),u2DTO.getImagen(),u2DTO.getMimeType(),u2DTO.getApellidos(),u2DTO.getCorreo(),u2DTO.getSexo());
		Usuario u3 = new Usuario(u3DTO.getUsername(),u3DTO.getNombre(),passwordEncoder.encode(u3DTO.getPassword()),u3DTO.getImagen(),u3DTO.getMimeType(),u3DTO.getApellidos(),u3DTO.getCorreo(),u3DTO.getSexo());
		Usuario u4 = new Usuario(u4DTO.getUsername(),u4DTO.getNombre(),passwordEncoder.encode(u4DTO.getPassword()),u4DTO.getImagen(),u4DTO.getMimeType(),u4DTO.getApellidos(),u4DTO.getCorreo(),u4DTO.getSexo());
		Usuario u5 = new Usuario(u5DTO.getUsername(),u5DTO.getNombre(),passwordEncoder.encode(u5DTO.getPassword()),u5DTO.getImagen(),u5DTO.getMimeType(),u5DTO.getApellidos(),u5DTO.getCorreo(),u5DTO.getSexo());
		
		u1.getAlumnos().add(a1);
		a1.setUsuarios(u1);
		u2.getAlumnos().add(a2);
		a2.setUsuarios(u2);
		u3.getAlumnos().add(a3);
		a3.setUsuarios(u3);
		
		u4.getEntrenadores().add(en1);
		en1.setUsuarios(u4);
		u5.getEntrenadores().add(en2);
		en2.setUsuarios(u5);
		
		rol1.getUsuarios().add(admin);
		admin.getRoles().add(rol1);
		
		rol2.getUsuarios().add(u1);
		u1.getRoles().add(rol2);
		
		rol2.getUsuarios().add(u2);
		u2.getRoles().add(rol2);
		
		rol2.getUsuarios().add(u3);
		u3.getRoles().add(rol2);
		
		rol3.getUsuarios().add(u4);
		u4.getRoles().add(rol3);
		
		rol3.getUsuarios().add(u5);
		u5.getRoles().add(rol3);
		
		usuarioService.insertarUsuario(admin);
		usuarioService.insertarUsuario(u1);
		usuarioService.insertarUsuario(u2);
		usuarioService.insertarUsuario(u3);
		usuarioService.insertarUsuario(u4);
		usuarioService.insertarUsuario(u5);
		
		ejercicioService.insertarEjercicio(ej1);
		ejercicioService.insertarEjercicio(ej2);
		ejercicioService.insertarEjercicio(ej3);
		
		rutinaService.insertarRutina(r1);
		rutinaService.insertarRutina(r2);
		
		entrenadorService.insertarEntrenador(en1);
		entrenadorService.insertarEntrenador(en2);
		
		alumnoService.insertarAlumno(a1);
		alumnoService.insertarAlumno(a2);
		alumnoService.insertarAlumno(a3);

		rolService.insertarRol(rol1);
		rolService.insertarRol(rol2);
		rolService.insertarRol(rol3);
		
		
		
	}
	
}
