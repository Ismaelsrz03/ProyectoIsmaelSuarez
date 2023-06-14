package principal.modelo.dto;

public class UsuarioDTO {

	private String username;
	private String nombre;
	private String password;
	
	private String imagen;
	
	private String mimeType;
	
	private String apellidos;
	
	private String correo;
	
	private String sexo;
	
	private String rol;
	
	public UsuarioDTO() {
		
	}
	
	public UsuarioDTO(String username, String nombre, String password, String imagen, String mimetype,
			 String apellidos, String correo, String sexo) {
		super();
		this.username = username;
		this.nombre = nombre;
		this.password = password;
		this.imagen = imagen;
		this.mimeType = mimetype;
		this.apellidos = apellidos;
		this.correo = correo;
		this.sexo = sexo;
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

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
}
