package principal.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import principal.modelo.Alumno;
import principal.modelo.Ejercicio;

@Entity
@Table(name="Entrenadores")
public class Entrenador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="nombre")
	private String nombre;
	
	@OneToMany(mappedBy="entrenadores", fetch = FetchType.EAGER)
	private Set<Alumno> alumnos;
	
	@ManyToMany(mappedBy = "entrenadores", fetch = FetchType.EAGER)
	private Set<Ejercicio> ejercicios;
	
	@ManyToMany(mappedBy = "entrenadores", fetch = FetchType.EAGER)
	private Set<Rutina> rutinas;
	
	public Entrenador() {
		
	}
	
	public Entrenador(String n) {
		nombre = n;
		alumnos = new HashSet<Alumno>();
		ejercicios = new HashSet<Ejercicio>();
		rutinas = new HashSet<Rutina>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Set<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public Set<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(Set<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}

	public Set<Rutina> getRutinas() {
		return rutinas;
	}

	public void setRutinas(Set<Rutina> rutinas) {
		this.rutinas = rutinas;
	}
	
	
}
