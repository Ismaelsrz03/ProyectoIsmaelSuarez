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
import javax.persistence.Lob;
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
	
	@Lob
	@Column(name="imagen")
	private String imagen;
	
	@Column(name="mimeType")
	private String mimeType;
	
	@Column(name="series")
	private int series;
	
	@Column(name="repeticiones")
	private int reps;
	
	@Column(name="descripcion", length = 2000)
	private String descripcion;
	
	@ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
	private Set<Entrenador> entrenadores;
	
	@ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
	private Set<Alumno> alumnos;
	
	@ManyToMany(mappedBy="ejercicios", fetch = FetchType.EAGER)
	private Set<Rutina> rutinas;
	
	
public Ejercicio() {
	entrenadores = new HashSet<Entrenador>();
	alumnos = new HashSet<Alumno>();
	rutinas = new HashSet<Rutina>();
	}
	
	public Ejercicio(String n, int s, int r, String des, String img, String mime) {
		nombre = n;
		series = s;
		reps = r;
		descripcion = des;
		imagen = img;
		mimeType = mime;
		entrenadores = new HashSet<Entrenador>();
		alumnos = new HashSet<Alumno>();
		rutinas = new HashSet<Rutina>();
	}
	
	public Ejercicio(String img) {
		imagen = img;
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

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getSeries() {
		return series;
	}

	public void setSeries(int series) {
		this.series = series;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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