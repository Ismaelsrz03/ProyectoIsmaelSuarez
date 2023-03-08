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
import principal.servicio.interfaces.RolService;
import principal.servicio.interfaces.UsuarioService;

@Service
public class RolServiceImpl implements RolService {
	
	@Autowired
	private RolRepo rolrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	
	}
	
	@Override
	public Rol insertarRol(Rol rol) {
		rolrepo.save(rol);
		return null;
	}
	
	@Override
	public Rol obtenerRolPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return rolrepo.findByNombre(nombre);
	}
	
	
	
}
