package principal.servicio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public Optional<Entrenador> obtenerEntrenadorPorID(Integer id) {
		// TODO Auto-generated method stub
		return entrenadorRepo.findById(id);
	}

	@Override
	public ArrayList<Entrenador> obtenerEntrenadorPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return entrenadorRepo.findAllByNombre(nombre);
	}
	
	@Override
	public ArrayList<Entrenador> obtenerEntrenadoresPorNombre(String nombre) {
		
		ArrayList<Entrenador> lista = entrenadorRepo.findAllByNombre(nombre);
		
		return lista;
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
