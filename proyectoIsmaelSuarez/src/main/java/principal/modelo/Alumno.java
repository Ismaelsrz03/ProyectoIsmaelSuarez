package principal.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import principal.modelo.Ejercicio;
import principal.modelo.Entrenador;

@Entity
@Table(name="Alumnos")
public class Alumno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="nombre")
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "id_Entrenador", nullable = true)
	private Entrenador entrenadores;
	
	@ManyToMany(mappedBy = "alumnos", fetch = FetchType.EAGER)
	private Set<Ejercicio> ejercicios;
	
	@ManyToMany(mappedBy = "alumnos", fetch = FetchType.EAGER)
	private Set<Rutina> rutinas;
	
	public Alumno() {
		
	}
	
	public Alumno(String n) {
		nombre = n;
		ejercicios = new HashSet<Ejercicio>();
		entrenadores = new Entrenador();
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

	public Entrenador getEntrenadores() {
		return entrenadores;
	}

	public void setEntrenadores(Entrenador entrenadores) {
		this.entrenadores = entrenadores;
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
