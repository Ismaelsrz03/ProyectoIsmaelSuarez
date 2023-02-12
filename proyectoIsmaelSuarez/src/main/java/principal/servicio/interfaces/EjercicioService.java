package principal.servicio.interfaces;

import java.util.List;

import principal.modelo.Ejercicio;

public interface EjercicioService {

	public Ejercicio insertarEjercicio (Ejercicio ejer);
	public List<Ejercicio> listarEjercicios();
	public Ejercicio obtenerEjercicioPorID (Integer id);
	public Ejercicio obtenerEjercicioPorNombre (String nombre);
	public void eliminarEjercicio(Ejercicio ejer);
	public void eliminarEjercicioPorId(Integer id);
	
}
