package br.com.sistema.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration	// Indica que esta é uma classe de configuração e que o Spring precisa carrega-la antes de carregar as outras classes
@EnableWebSecurity		
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final SecurityFilter securityFilter;
	
	public SecurityConfig(SecurityFilter securityFilter, CustomUserDetailsService userDetailsService) {
		this.securityFilter = securityFilter;
		this.userDetailsService = userDetailsService;
	}
	
	
//	@Bean 
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
	
	@Bean	// Configuração do filtro de segurança
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.csrf( csrf -> csrf.disable() ) 	// Desativa a proteção padrão(login/senha) do Spring Security.
												// Desativa a proteção contra Cross-Site Request Forgery (CSRF) na sua aplicação
												// Estamos dizendo ao Spring Security para não aplicar a proteção CSRF, pois 
												// APIs REST não necessitam de proteção CSRF, pois são acessadas principalmente com tokens de autenticação
		 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	// Define que a aplicação é STATELLES(Não guarda estado de login dentro da aplicação), portanto, para
      																									// toda requisição precisa ser passado o token de autentição para ser autorizado. Este é o padrão REST
		 .authorizeHttpRequests( authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()			// Define o endpoint que não necessita de autenticação. Serão os endpoints que não passarão pela classe Securityfilter que criamos
				.anyRequest().authenticated() )											// Informa que qualquer outro Request precisa estar autenticado
	     .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); 	// Diz para a aplicação aplicar e utilizar o filtro que criamos, ANTES do filtro UsernamePasswordAuthenticationFilter
        return http.build();
    }

	
    @Bean	// Configuração do PasswordEncoder, necessário para realizar o encoding(criptografia) da senha(password). 
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
}
