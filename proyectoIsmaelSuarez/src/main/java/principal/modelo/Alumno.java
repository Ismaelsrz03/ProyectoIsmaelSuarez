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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinTable(
			name = "alumnos_ejercicios",
			joinColumns = {@JoinColumn(name = "id_Alumno")},
			inverseJoinColumns = {@JoinColumn(name = "id_Ejercicio")}
			
			)
	@JsonIgnore
	private Set<Ejercicio> ejercicios;
	
	@ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinTable(
			name = "alumnos_rutinas",
			joinColumns = {@JoinColumn(name = "id_Alumno")},
			inverseJoinColumns = {@JoinColumn(name = "id_Rutina")}
			
			)
	@JsonIgnore
	private Set<Rutina> rutinas;
	
	@ManyToOne
	@JoinColumn(name= "id_usuario", nullable = true)
	private Usuario usuarios;
	
	public Alumno() {
		ejercicios = new HashSet<Ejercicio>();
		entrenadores = new Entrenador();
		rutinas = new HashSet<Rutina>();
		usuarios = new Usuario();
	}
	
	public Alumno(String n) {
		nombre = n;
		ejercicios = new HashSet<Ejercicio>();
		entrenadores = new Entrenador();
		rutinas = new HashSet<Rutina>();
		usuarios = new Usuario();
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

	public Usuario getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}
	
	

}
