package principal.persistencia;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Alumno;
import principal.modelo.Progreso;

public interface ProgresoRepo extends JpaRepository<Progreso,Integer> {



	
}
