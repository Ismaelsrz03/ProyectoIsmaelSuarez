package principal.servicio.interfaces;

import java.util.Optional;

import principal.modelo.Nota;

public interface NotaService {

	Nota guardarNota(Nota nota);

	Optional<Nota> obtenerNotaPorId(Integer id);

	void eliminarNotaPorId(Integer id);

}
