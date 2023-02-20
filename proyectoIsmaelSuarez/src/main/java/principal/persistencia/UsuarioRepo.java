package principal.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario,Integer> {

	public Optional<Usuario> findByUsername(String username);
	
}
