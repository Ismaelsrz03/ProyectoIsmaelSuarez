package principal.servicio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Ejercicio;
import principal.persistencia.EjercicioRepo;
import principal.servicio.interfaces.EjercicioService;

@Service
public class EjercicioServiceImpl implements EjercicioService {
	
	@Autowired
	private EjercicioRepo ejercicioRepo;

	@Override
	public Ejercicio insertarEjercicio(Ejercicio ejer) {
		ejercicioRepo.save(ejer);
		return null;
	}


	@Override
	public List<Ejercicio> listarEjercicios() {
		
		return ejercicioRepo.findAll();
	}

	@Override
	public Optional<Ejercicio> obtenerEjercicioPorID(Integer id) {
		// TODO Auto-generated method stub
		return ejercicioRepo.findById(id);
	}

	@Override
	public Optional<Ejercicio> obtenerEjercicioPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return ejercicioRepo.findByNombre(nombre);
	}
	
	public ArrayList<Ejercicio> obtenerEjerciciosPorNombre(String nombre) {
		ArrayList<Ejercicio> lista = ejercicioRepo.findAllByNombre(nombre);
		return lista;
	}

	@Override
	public void eliminarEjercicio(Ejercicio ejer) {
		ejercicioRepo.delete(ejer);
	}

	@Override
	public void eliminarEjercicioPorId(Integer id) {
		ejercicioRepo.delete(ejercicioRepo.findById(id).get());
		
	}

}
