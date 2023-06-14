package principal.modelo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column (name="nombre")
	private String nombre;
	
	@Column (name="apellidos")
	private String apellidos;
	
	@Column (name="correo")
	private String correo;
	
	@Column (name="sexo")
	private String sexo;
	
	@Column(name="username", unique = true)
	private String username;
	
	@Column (name="password")
	private String password;
	
	@Lob
	@Column(name="imagen")
	private String imagen;
	
	@Column(name="mimeType")
	private String mimeType;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(
			name="usuario_roles",
			joinColumns = {@JoinColumn(name="id_usuario")},
			inverseJoinColumns = @JoinColumn(name="id_rol")
			)
	@JsonIgnore
	private Set<Rol> roles;
	
	@OneToMany(mappedBy="usuarios", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<Alumno> alumnos = new HashSet<>();
	
	@OneToMany(mappedBy="usuarios", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private Set<Entrenador> entrenadores = new HashSet<>();
	
	public Usuario() {
		roles = new HashSet<>();
		alumnos = new HashSet<Alumno>();
		entrenadores = new HashSet<Entrenador>();
	}
	
	public Usuario(String username, String nombre, String password, String imagen, String mimetype, 
			String apellidos, String correo, String sexo) {
		super();
		this.username = username;
		this.nombre = nombre;
		this.password = password;
		this.imagen = imagen;
		this.mimeType = mimetype;
		this.apellidos=apellidos;
		this.correo=correo;
		this.sexo=sexo;
		roles = new HashSet<Rol>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}
	
	

	public Set<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Set<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public Set<Entrenador> getEntrenadores() {
		return entrenadores;
	}

	public void setEntrenadores(Set<Entrenador> entrenadores) {
		this.entrenadores = entrenadores;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
	
}
