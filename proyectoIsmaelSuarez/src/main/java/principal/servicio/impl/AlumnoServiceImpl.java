package principal.servicio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Alumno;
import principal.persistencia.AlumnoRepo;
import principal.servicio.interfaces.AlumnoService;

@Service
public class AlumnoServiceImpl implements AlumnoService {
	
	@Autowired
	private AlumnoRepo alumnoRepo;

	@Override
	public Alumno insertarAlumno(Alumno alumno) {
		alumnoRepo.save(alumno);
		return null;
	}


	@Override
	public List<Alumno> listarAlumnos() {
		
		return alumnoRepo.findAll();
	}

	@Override
	public Alumno obtenerAlumnoPorID(Integer id) {
		// TODO Auto-generated method stub
		return alumnoRepo.findById(id).get();
	}

	@Override
	public Alumno obtenerAlumnoPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return alumnoRepo.findByNombre(nombre).get();
	}

	@Override
	public void eliminarAlumno(Alumno alumno) {
		alumnoRepo.delete(alumno);
	}

	@Override
	public void eliminarAlumnoPorId(Integer id) {
		alumnoRepo.delete(alumnoRepo.findById(id).get());
		
	}

}
