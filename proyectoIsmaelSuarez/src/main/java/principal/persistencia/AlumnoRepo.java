package principal.persistencia;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Alumno;

public interface AlumnoRepo extends JpaRepository<Alumno,Integer> {

	public ArrayList<Alumno> findByNombre(String nombre);

	public ArrayList<Alumno> findAllByNombre(String nombre);

	
}
