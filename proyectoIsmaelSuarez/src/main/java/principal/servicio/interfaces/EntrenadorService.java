package principal.servicio.interfaces;

import java.util.List;

import principal.modelo.Entrenador;

public interface EntrenadorService {
	
	public Entrenador insertarEntrenador (Entrenador entrenador);
	public List<Entrenador> listarEntrenadors();
	public Entrenador obtenerEntrenadorPorID (Integer id);
	public Entrenador obtenerEntrenadorPorNombre (String nombre);
	public void eliminarEntrenador(Entrenador entrenador);
	public void eliminarEntrenadorPorId(Integer id);

}
