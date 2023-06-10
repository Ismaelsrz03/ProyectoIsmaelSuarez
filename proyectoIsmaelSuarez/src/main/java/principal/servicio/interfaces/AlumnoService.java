package principal.servicio.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import principal.modelo.Alumno;

public interface AlumnoService {
	
	public Alumno insertarAlumno (Alumno alumno);
	public List<Alumno> listarAlumnos();
	public Optional<Alumno> obtenerAlumnoPorID (Integer id);
	public ArrayList<Alumno> obtenerAlumnoPorNombre (String nombre);
	public void eliminarAlumno(Alumno alumno);
	public void eliminarAlumnoPorId(Integer id);
	ArrayList<Alumno> obtenerAlumnosPorNombre(String nombre);

}
