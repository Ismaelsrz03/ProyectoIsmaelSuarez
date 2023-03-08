package principal.servicio.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import principal.modelo.Rol;
import principal.modelo.Usuario;
import principal.modelo.dto.UsuarioDTO;
import principal.persistencia.RolRepo;
import principal.persistencia.UsuarioRepo;
import principal.servicio.interfaces.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepo usuarioRepo;
	
	@Autowired
	private RolRepo rolRepo;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<Usuario> usuario = usuarioRepo.findByUsername(username);
		
		if(usuario.isPresent()) {
			
			Usuario springUsermio = usuario.get();
			return springUsermio;
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
		
	}

	@Override
	public Usuario insertarUsuario(Usuario user) {
		usuarioRepo.save(user);
		return null;
	}

	@Override
	public Usuario insertarusuarioDTO(UsuarioDTO userDTO) {
		String rolSeleccionado = userDTO.getRol();
		Usuario nuevoUsuario = new Usuario(userDTO.getUsername(),userDTO.getNombre(),passwordEncoder.encode(userDTO.getPassword()));
		
		if (rolSeleccionado.equals("Alumno")) {
	        nuevoUsuario.getRoles().add(rolRepo.findByNombre("ROLE_USER"));
	    } else if (rolSeleccionado.equals("Entrenador")) {
	        nuevoUsuario.getRoles().add(rolRepo.findByNombre("ROLE_ENTRENADOR"));
	    }
		
		
//		nuevoUsuario.getRoles().add(new Rol("ROLE_ADMIN"));
		
		return usuarioRepo.save(nuevoUsuario);
	}

	@Override
	public List<Usuario> listarUsuarios() {
		
		return usuarioRepo.findAll();
	}

	@Override
	public Usuario obtenerUsuarioPorID(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepo.findById(id).get();
	}

	@Override
	public Usuario obtenerUsuarioPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return usuarioRepo.findByUsername(nombre).get();
	}

	@Override
	public void eliminarUsuario(Usuario user) {
		usuarioRepo.delete(user);
	}

	@Override
	public void eliminarUsuarioPorId(Integer id) {
		usuarioRepo.delete(usuarioRepo.findById(id).get());
		
	}

}
