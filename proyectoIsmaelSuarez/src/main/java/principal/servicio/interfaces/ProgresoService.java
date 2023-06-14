package principal.servicio.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import principal.modelo.Alumno;
import principal.modelo.Progreso;

public interface ProgresoService {
	
	 Progreso guardarProgreso(Progreso progreso);

	Optional<Progreso> obtenerProgresoPorId(Integer id);

	void eliminarProgresoPorId(Integer id);

}
