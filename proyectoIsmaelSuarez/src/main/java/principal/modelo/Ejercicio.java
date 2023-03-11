package principal.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import principal.modelo.Alumno;
import principal.modelo.Entrenador;
import principal.modelo.Rutina;

@Entity
@Table(name="Ejercicios")
public class Ejercicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="nombre")
	private String nombre;
	
	@ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
	private Set<Entrenador> entrenadores;
	
	@ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
	private Set<Alumno> alumnos;
	
	@ManyToMany(mappedBy="ejercicios", fetch = FetchType.EAGER)
	private Set<Rutina> rutinas;
	
public Ejercicio() {
		
	}
	
	public Ejercicio(String n) {
		nombre = n;
		entrenadores = new HashSet<Entrenador>();
		alumnos = new HashSet<Alumno>();
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

	public Set<Entrenador> getEntrenadores() {
		return entrenadores;
	}

	public void setEntrenadores(Set<Entrenador> entrenadores) {
		this.entrenadores = entrenadores;
	}

	public Set<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Set<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public Set<Rutina> getRutinas() {
		return rutinas;
	}

	public void setRutinas(Set<Rutina> rutinas) {
		this.rutinas = rutinas;
	}
	
	
}
