package principal.servicio.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Rutina;
import principal.persistencia.RutinaRepo;
import principal.servicio.interfaces.RutinaService;

@Service
public class RutinaServiceImpl implements RutinaService {
	
	@Autowired
	private RutinaRepo rutinaRepo;

	@Override
	public Rutina insertarRutina(Rutina rutina) {
		rutinaRepo.save(rutina);
		return null;
	}


	@Override
	public List<Rutina> listarRutinas() {
		
		return rutinaRepo.findAll();
	}

	@Override
	public Optional<Rutina> obtenerRutinaPorID(Integer id) {
		// TODO Auto-generated method stub
		return rutinaRepo.findById(id);
	}

	@Override
	public Rutina obtenerRutinaPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return rutinaRepo.findByNombre(nombre).get();
	}

	@Override
	public void eliminarRutina(Rutina rutina) {
		rutinaRepo.delete(rutina);
	}

	@Override
	public void eliminarRutinaPorId(Integer id) {
		rutinaRepo.delete(rutinaRepo.findById(id).get());
		
	}

}
