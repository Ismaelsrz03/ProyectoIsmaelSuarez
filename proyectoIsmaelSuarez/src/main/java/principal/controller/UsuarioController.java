package principal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import principal.modelo.Alumno;
import principal.modelo.Entrenador;
import principal.modelo.Rol;
import principal.modelo.Usuario;
import principal.modelo.dto.UsuarioDTO;
import principal.servicio.impl.RolServiceImpl;
import principal.servicio.impl.UsuarioServiceImpl;

@RequestMapping("/usuarios")
@Controller
public class UsuarioController {

	@Autowired
	UsuarioServiceImpl userDetailsService;
	
	@Autowired
	RolServiceImpl rolService;
	
	private Usuario miUsuario;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping(value= {"","/"})
	String homeusuarios(Model model) {
		
		// Salir a buscar a la BBDD
		miUsuario = obtenerLog();
		ArrayList<Usuario> listaUsuarios = (ArrayList<Usuario>) userDetailsService.listarUsuarios();
		ArrayList<Rol> listaRoles = (ArrayList<Rol>) rolService.listarRoles();
		model.addAttribute("listausuarios", listaUsuarios);
		model.addAttribute("listaroles", listaRoles);
		model.addAttribute("usuarioaEditar", new Usuario());
		model.addAttribute("usuarioNuevo", new UsuarioDTO());
		model.addAttribute("miUsuario",miUsuario);
		
		//
		
		return "usuarios";
	}
	private Usuario obtenerLog() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario u2 = null;
		UserDetails userDetails = null;
		
		if(principal instanceof UserDetails) {
			
			userDetails = (UserDetails) principal;
			
		 u2 = userDetailsService.obtenerUsuarioPorNombre(userDetails.getUsername());
		}
		return u2;
	}
	@PostMapping("/edit/{id}")
	public String editarUsuario(@PathVariable Integer id, @ModelAttribute("usuarioaEditar") Usuario usuarioEditado, BindingResult bindingresult) {
		
		Usuario usuarioaeditar= userDetailsService.obtenerUsuarioPorID(id);
		
		usuarioaeditar.setNombre(usuarioEditado.getNombre());
		
		usuarioaeditar.setUsername(usuarioEditado.getUsername());
		
		usuarioaeditar.setPassword(passwordEncoder.encode(usuarioEditado.getPassword()));
		
		
		userDetailsService.insertarUsuario(usuarioaeditar);
		
		return "redirect:/usuarios";
	}
	
	@PostMapping("/add")
	public String addUsuario(@ModelAttribute("usuarioNuevo") UsuarioDTO usuarioNew, BindingResult bindingresult) {
	
		userDetailsService.insertarusuarioDTO(usuarioNew);
		
		return "redirect:/usuarios";
	}
	
	@GetMapping({"/{id}"})
	String idUsuario(Model model, @PathVariable Integer id) {
		
		Usuario usuarioMostrar = userDetailsService.obtenerUsuarioPorID(id);
		model.addAttribute("usuarioMostrar",usuarioMostrar);
		
		return "usuario";
	}
	
	@GetMapping("/delete/{id}")
	String deleteUsuario(Model model, @PathVariable Integer id) {
		
		if(esAdmin()) {
		Usuario aeliminar = userDetailsService.obtenerUsuarioPorID(id);
		ArrayList<Entrenador> entrenador = new ArrayList<Entrenador>();
		for(Entrenador e : aeliminar.getEntrenadores()) {
			for(Alumno a: e.getAlumnos()) {
				a.setEntrenadores(null);
			}
		}
		
		userDetailsService.eliminarUsuario(aeliminar);
		}
		
		
		return "redirect:/usuarios";
	}
	
	@GetMapping("/registro")
	public String mostrarRegistro(Model model, @ModelAttribute("usuarioNuevo") Usuario usuarioNew, BindingResult bindingresult) {
		
		model.addAttribute("newUserDTO", new UsuarioDTO());
		
		return "registro";
	}
	
	@PostMapping("/addRegistro")
	public String addRegistro(@ModelAttribute("usuarioNuevo") UsuarioDTO usuarioNew, BindingResult bindingresult, @RequestParam("file") MultipartFile file) {

		if(!file.isEmpty()) {

			 
			 try {

				 byte[] imageBytes =file.getBytes();
				 String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				 usuarioNew.setImagen(encodedString);
				 usuarioNew.setMimeType(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		userDetailsService.insertarusuarioDTO(usuarioNew);
		
		return "login";
	}
	
	public boolean esAdmin() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails userDetails = null;
		
		if(principal instanceof UserDetails) {
			
			userDetails = (UserDetails) principal;
			
			Usuario u = userDetailsService.obtenerUsuarioPorNombre(userDetails.getUsername());
			
			for (Rol r: u.getRoles()) {
				
				if(r.getNombre().compareTo("ROLE_ADMIN") == 0){
					
					return true;
				}
			}
		}
		
		return false;
	}
	
}
