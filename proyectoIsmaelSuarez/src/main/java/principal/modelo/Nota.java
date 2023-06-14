package principal.modelo;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

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

@Entity
@Table(name = "notas")
public class Nota {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

	@ManyToMany(mappedBy = "notas", fetch = FetchType.EAGER)
    private Set<Entrenador> entrenadores;
    
	@ManyToMany(mappedBy = "notas", fetch = FetchType.EAGER)
    private Set<Alumno> alumnos;

    @Column(name="titulo")
    private String titulo;
    
    @Column(name="contenido", length = 2000)
    private String contenido;
    
    @Column(name = "fecha")
    private Date fecha;
    
    public Nota() {
    	alumnos = new HashSet<Alumno>();
    	entrenadores = new HashSet<Entrenador>();
    }
    

	public Nota(String titulo, String contenido, Date fecha) {
		this.titulo = titulo;
		this.contenido = contenido;
		this.fecha = fecha;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
