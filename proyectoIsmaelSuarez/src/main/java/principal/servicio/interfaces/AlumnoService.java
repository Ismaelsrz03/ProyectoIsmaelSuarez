package principal.servicio.interfaces;

import java.util.List;

import principal.modelo.Alumno;

public interface AlumnoService {
	
	public Alumno insertarAlumno (Alumno alumno);
	public List<Alumno> listarAlumnos();
	public Alumno obtenerAlumnoPorID (Integer id);
	public Alumno obtenerAlumnoPorNombre (String nombre);
	public void eliminarAlumno(Alumno alumno);
	public void eliminarAlumnoPorId(Integer id);

}
