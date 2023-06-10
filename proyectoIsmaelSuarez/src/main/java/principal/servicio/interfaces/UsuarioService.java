package principal.servicio.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import principal.modelo.Usuario;
import principal.modelo.dto.UsuarioDTO;

public interface UsuarioService extends UserDetailsService {
	
	public Usuario insertarUsuario (Usuario user);
	public Usuario insertarusuarioDTO (UsuarioDTO userDTO);
	public List<Usuario> listarUsuarios();
	public Optional<Usuario> obtenerUsuarioPorID (Integer id);
	public Usuario obtenerUsuarioPorNombre (String nombre);
	public void eliminarUsuario(Usuario user);
	public void eliminarUsuarioPorId(Integer id);
	Optional<Usuario> obtenerUsuariosPorNombre2(String nombre);
	
}
