package principal.servicio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Entrenador;
import principal.persistencia.EntrenadorRepo;
import principal.servicio.interfaces.EntrenadorService;

@Service
public class EntrenadorServiceImpl implements EntrenadorService {
	
	@Autowired
	private EntrenadorRepo entrenadorRepo;

	@Override
	public Entrenador insertarEntrenador(Entrenador entrenador) {
		entrenadorRepo.save(entrenador);
		return null;
	}


	@Override
	public List<Entrenador> listarEntrenadors() {
		
		return entrenadorRepo.findAll();
	}

	@Override
	public Entrenador obtenerEntrenadorPorID(Integer id) {
		// TODO Auto-generated method stub
		return entrenadorRepo.findById(id).get();
	}

	@Override
	public Entrenador obtenerEntrenadorPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return entrenadorRepo.findByNombre(nombre).get();
	}

	@Override
	public void eliminarEntrenador(Entrenador entrenador) {
		entrenadorRepo.delete(entrenador);
	}

	@Override
	public void eliminarEntrenadorPorId(Integer id) {
		entrenadorRepo.delete(entrenadorRepo.findById(id).get());
		
	}
}
