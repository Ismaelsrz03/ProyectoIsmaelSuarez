package principal.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Entrenador;

public interface EntrenadorRepo extends JpaRepository<Entrenador,Integer> {
	
	public Optional<Entrenador> findByNombre(String nombre);

}
