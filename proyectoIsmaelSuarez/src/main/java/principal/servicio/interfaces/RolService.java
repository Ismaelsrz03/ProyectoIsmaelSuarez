package principal.servicio.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import principal.modelo.Rol;
import principal.modelo.Usuario;

public interface RolService extends UserDetailsService {
	
	public Rol obtenerRolPorNombre (String nombre);

	Usuario insertarRol(Rol rol);

	
}
