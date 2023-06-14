package principal.modelo;

import java.util.Set;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "progresos")
public class Progreso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Alumno")
    private Alumno alumno;

    @Column(name="ejercicio")
    private String ejercicio;
    
    @Column(name = "fecha")
    private Date fecha;
    
    @Column(name = "series")
    private int series;

    @Column(name = "peso")
    private double peso;

    @Column(name = "repeticiones")
    private int repeticiones;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;
    
    public Progreso() {
    	alumno = new Alumno();
    }
    
    
    
   

	public Progreso(String ejercicio,Date fecha, int series, double peso, int repeticiones,
			String descripcion) {
		this.ejercicio = ejercicio;
		this.fecha = fecha;
		this.series = series;
		this.peso = peso;
		this.repeticiones = repeticiones;
		this.descripcion = descripcion;
	}





	public Integer getId() {
		return id;
	}





	public void setId(Integer id) {
		this.id = id;
	}





	public Alumno getAlumno() {
		return alumno;
	}





	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public String getEjercicio() {
		return ejercicio;
	}





	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio;
	}





	public Date getFecha() {
		return fecha;
	}





	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}





	public int getSeries() {
		return series;
	}

	public void setSeries(int series) {
		this.series = series;
	}





	public double getPeso() {
		return peso;
	}





	public void setPeso(double peso) {
		this.peso = peso;
	}





	public int getRepeticiones() {
		return repeticiones;
	}





	public void setRepeticiones(int repeticiones) {
		this.repeticiones = repeticiones;
	}





	public String getDescripcion() {
		return descripcion;
	}





	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    
}
