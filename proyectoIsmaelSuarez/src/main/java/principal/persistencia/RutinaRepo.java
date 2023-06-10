package principal.persistencia;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Rutina;

public interface RutinaRepo extends JpaRepository<Rutina,Integer> {
	
	public Optional<Rutina> findByNombre(String nombre);

	public ArrayList<Rutina> findAllByNombre(String nombre);

}
