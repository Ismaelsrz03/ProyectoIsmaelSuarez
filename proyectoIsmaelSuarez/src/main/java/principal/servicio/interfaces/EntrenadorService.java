package principal.servicio.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import principal.modelo.Entrenador;

public interface EntrenadorService {
	
	public Entrenador insertarEntrenador (Entrenador entrenador);
	public List<Entrenador> listarEntrenadors();
	public Optional<Entrenador> obtenerEntrenadorPorID (Integer id);
	public ArrayList<Entrenador> obtenerEntrenadorPorNombre (String nombre);
	public void eliminarEntrenador(Entrenador entrenador);
	public void eliminarEntrenadorPorId(Integer id);
	ArrayList<Entrenador> obtenerEntrenadoresPorNombre(String nombre);

}
