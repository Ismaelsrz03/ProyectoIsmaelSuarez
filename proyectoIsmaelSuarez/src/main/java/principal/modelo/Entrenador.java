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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@OneToMany(mappedBy="entrenadores",fetch = FetchType.EAGER)
	private Set<Alumno> alumnos;
	
	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(
			name = "entrenadores_ejercicios",
			joinColumns = {@JoinColumn(name = "id_Entrenador")},
			inverseJoinColumns = {@JoinColumn(name = "id_Ejercicio")}
			
			)
	@JsonIgnore
	private Set<Ejercicio> ejercicios;
	
	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(
			name = "entrenadores_rutinas",
			joinColumns = {@JoinColumn(name = "id_Entrenador")},
			inverseJoinColumns = {@JoinColumn(name = "id_Rutina")}
			
			)
	@JsonIgnore
	private Set<Rutina> rutinas;
	
	@ManyToOne
	@JoinColumn(name= "id_usuario", nullable = true)
	private Usuario usuarios;
	
	public Entrenador() {
		
	}
	
	public Entrenador(String n) {
		nombre = n;
		alumnos = new HashSet<Alumno>();
		ejercicios = new HashSet<Ejercicio>();
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

	public Usuario getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuario usuarios) {
		this.usuarios = usuarios;
	}
	
	
	
}
