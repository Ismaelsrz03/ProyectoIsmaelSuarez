package principal.servicio.interfaces;

import java.util.List;

import principal.modelo.Rutina;

public interface RutinaService {
	
	public Rutina insertarRutina (Rutina rutina);
	public List<Rutina> listarRutinas();
	public Rutina obtenerRutinaPorID (Integer id);
	public Rutina obtenerRutinaPorNombre (String nombre);
	public void eliminarRutina(Rutina rutina);
	public void eliminarRutinaPorId(Integer id);

}
