package principal.seguridad;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
public class ErrorConfig {

	@Bean
	public ErrorViewResolver customErrorViewResolver() {
		return (HttpServletRequest request, HttpStatus status, Map<String, Object> model) -> {
			if(status == HttpStatus.BAD_REQUEST) {
				return new ModelAndView("error400");
			} else if (status == HttpStatus.FORBIDDEN) {
				return new ModelAndView("error403");
			} else if (status == HttpStatus.NOT_FOUND) {
				return new ModelAndView("error404");
			} else if (status == HttpStatus.METHOD_NOT_ALLOWED) {
				return new ModelAndView("error405");
			}
			
			return null;
		};
		
		
	}
}
