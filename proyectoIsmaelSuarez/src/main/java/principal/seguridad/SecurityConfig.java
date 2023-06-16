package principal.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import principal.servicio.impl.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UsuarioServiceImpl userDetailsService;
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
	
	 @Bean
	    public WebSecurityCustomizer webSecurityCustomizer() {
	        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
	    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
           
            http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/usuarios","/alumnos","/ejercicios","/rutinas","/entrenadores","/panel","/contrasena","/cuenta","/alumnosNombre","/entrenadoresNombre","/ejerciciosNombre","/rutinasNombre").hasRole("ADMIN")
            .antMatchers("/alumnoEntrenador","/alumnoEjercicio","/alumnoRutina","/perfilAlumno","/progresoAlumno").hasRole("USER")
            .antMatchers("/entrenadorAlumno","/entrenadorEjercicio","/entrenadorRutina","/perfilEntrenador","/notaEntrenador").hasRole("ENTRENADOR")
            .regexMatchers("/ejercicios/.","/rutinas/.","/entrenadores/.","/alumnos/.","/usuarios/.","/panel/.","/contrasena/.","/cuenta/.").hasRole("ADMIN")
            .regexMatchers("/alumnoEntrenador/.","/alumnoEjercicio/.","/alumnoRutina/.","/perfilAlumno/.","/progresoAlumno/.").hasRole("USER")
            .regexMatchers("/entrenadorAlumno/.","/entrenadorEjercicio/.","/entrenadorRutina/.","/perfilEntrenador/.","/notaEntrenador/.").hasRole("ENTRENADOR")
                    .and()
                    .formLogin()
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            for (GrantedAuthority auth : authentication.getAuthorities()) {
                                if (auth.getAuthority().equals("ROLE_USER")) {
                                    response.sendRedirect("/perfilAlumno");
                                }
                                if (auth.getAuthority().equals("ROLE_ENTRENADOR")) {
                                    response.sendRedirect("/perfilEntrenador");
                                }
                                if (auth.getAuthority().equals("ROLE_ADMIN")) {
                                    response.sendRedirect("/");
                                }
                            }
                        })
                        .and()
                    .logout()
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                        .and()
                   
                    .csrf().disable();
           
    }
   
}
