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
	
	@Column (name="apellidos")
	private String apellidos;
	
	@Column (name="correo")
	private String correo;
	
	@Column (name="sexo")
	private String sexo;
	
	@Column(name="edad")
	private int edad;
	
	@Column(name="altura")
	private int altura;
	
	@Column(name="peso")
	private double peso;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="objetivos")
	private String objetivos;
	
	@Column(name="insta")
	private String insta;
	
	@Column(name="youtube")
	private String youtube;
	
	@Column(name="tiktok")
	private String tiktok;
	
	@Column(name="twitter")
	private String twitter;
	
	@Column(name="facebook")
	private String facebook;
	
	@ManyToOne
	@JoinColumn(name = "id_Entrenador", nullable = true)
	private Entrenador entrenadores;
	
	@ManyToMany( cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
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
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name= "id_usuario", nullable = true)
	private Usuario usuarios;
	
	public Alumno() {
		ejercicios = new HashSet<Ejercicio>();
		entrenadores = new Entrenador();
		rutinas = new HashSet<Rutina>();
		usuarios = new Usuario();
	}
	
	
	
	public Alumno(String nombre, String apellidos, String correo, String sexo, int edad, int altura, double peso,
			String descripcion, String objetivos, String insta, String youtube, String tiktok, String twitter,
			String facebook) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
		this.sexo = sexo;
		this.edad = edad;
		this.altura = altura;
		this.peso = peso;
		this.descripcion = descripcion;
		this.objetivos = objetivos;
		this.insta = insta;
		this.youtube = youtube;
		this.tiktok = tiktok;
		this.twitter = twitter;
		this.facebook = facebook;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getEdad() {
		return edad;
	}



	public void setEdad(int edad) {
		this.edad = edad;
	}



	public int getAltura() {
		return altura;
	}



	public void setAltura(int altura) {
		this.altura = altura;
	}



	public double getPeso() {
		return peso;
	}



	public void setPeso(double peso) {
		this.peso = peso;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getObjetivos() {
		return objetivos;
	}



	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}



	public String getInsta() {
		return insta;
	}



	public void setInsta(String insta) {
		this.insta = insta;
	}



	public String getYoutube() {
		return youtube;
	}



	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}



	public String getTiktok() {
		return tiktok;
	}



	public void setTiktok(String tiktok) {
		this.tiktok = tiktok;
	}



	public String getTwitter() {
		return twitter;
	}



	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}



	public String getFacebook() {
		return facebook;
	}



	public void setFacebook(String facebook) {
		this.facebook = facebook;
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
