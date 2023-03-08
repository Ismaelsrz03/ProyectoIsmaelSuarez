package principal.modelo.dto;

public class UsuarioDTO {

	private String username;
	private String nombre;
	private String password;
	private String rol;
	
	public UsuarioDTO() {
		
	}
	
	public UsuarioDTO(String username, String nombre, String password) {
		super();
		this.username = username;
		this.nombre = nombre;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
}
