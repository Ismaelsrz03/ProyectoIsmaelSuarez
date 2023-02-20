package principal.servicio.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import principal.modelo.Rol;

public interface RolService extends UserDetailsService {
	
	public Rol obtenerRolPorNombre (String nombre);

	
}
