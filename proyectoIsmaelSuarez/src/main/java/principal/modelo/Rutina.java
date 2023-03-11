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

import principal.modelo.Ejercicio;

@Entity
@Table(name="Rutinas")
public class Rutina {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="Nombre")
	private String nombre;
	
	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(
			name = "rutinas_ejercicios",
			joinColumns = {@JoinColumn(name = "id_Rutina")},
			inverseJoinColumns = {@JoinColumn(name = "id_Ejercicio")}
			
			)
	@JsonIgnore
	private Set<Ejercicio> ejercicios;
	
	@ManyToMany(mappedBy = "rutinas", fetch = FetchType.EAGER)
	private Set<Entrenador> entrenadores;
	
	@ManyToMany(mappedBy = "rutinas", fetch = FetchType.EAGER)
	private Set<Alumno> alumnos;
	
	public Rutina() {
		
	}
	
	public Rutina(String n) {
		nombre = n;
		ejercicios = new HashSet<Ejercicio>();
		entrenadores = new HashSet<Entrenador>();
		alumnos = new HashSet<Alumno>();
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

	public Set<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(Set<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
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
	
	

}
