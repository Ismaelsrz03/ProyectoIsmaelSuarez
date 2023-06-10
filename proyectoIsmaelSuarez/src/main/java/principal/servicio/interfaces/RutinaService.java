package principal.servicio.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import principal.modelo.Rutina;


public interface RutinaService {
	
	public Rutina insertarRutina (Rutina rutina);
	public List<Rutina> listarRutinas();
	public Optional<Rutina> obtenerRutinaPorID (Integer id);
	public Optional<Rutina> obtenerRutinaPorNombre (String nombre);
	public void eliminarRutina(Rutina rutina);
	public void eliminarRutinaPorId(Integer id);
	ArrayList<Rutina> obtenerRutinasPorNombre(String nombre);

}
