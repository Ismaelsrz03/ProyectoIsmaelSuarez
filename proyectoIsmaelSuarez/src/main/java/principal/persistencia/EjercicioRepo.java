package principal.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Ejercicio;

public interface EjercicioRepo extends JpaRepository<Ejercicio,Integer> {
	
	public Optional<Ejercicio> findByNombre(String nombre);

}
