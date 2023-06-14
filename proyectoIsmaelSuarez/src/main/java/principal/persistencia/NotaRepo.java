package principal.persistencia;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.modelo.Alumno;
import principal.modelo.Nota;
import principal.modelo.Progreso;

public interface NotaRepo extends JpaRepository<Nota,Integer> {



	
}
