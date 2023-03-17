package principal.persistencia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Alumno;

public interface AlumnoRepo extends JpaRepository<Alumno,Integer> {

	public Optional<Alumno> findByNombre(String nombre);

	
}
