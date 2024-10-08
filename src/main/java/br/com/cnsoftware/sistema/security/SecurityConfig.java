package br.com.cnsoftware.sistema.security;

import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration		// Indica que esta é uma classe de configuração e que o Spring precisa carrega-la antes de carregar as outras classes
@EnableWebSecurity	// Informa que é uma classe que cuida da segurança WEB da aplicação
public class SecurityConfig {

	@Autowired
	private SecurityFilter securityFilter;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
				// Desativa a proteção padrão(login/senha) do Spring Security.
				// Desativa a proteção contra Cross-Site Request Forgery (CSRF) na sua aplicação
				// Estamos dizendo ao Spring Security para não aplicar a proteção CSRF, pois 
				// APIs REST não necessitam de proteção CSRF, pois são acessadas principalmente com tokens de autenticação
				httpSecurity.csrf(csrf -> csrf.disable())
                
                // Define que a aplicação é STATELLES(Não guarda estado de login dentro da aplicação), portanto, para
                // toda requisição precisa ser passado o token de autentição para ser autorizado. Este é o padrão REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		
                		// Define o endpoint que não necessita de autenticação. Serão os endpoints que não passarão
                		// pela classe Securityfilter que criamos
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()	// Permite metodo POST para o endpoint /auth/login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()	// Permite metodo POST para o endpoint /auth/register apenas quem possui a Role ADMIN
//                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")	// Permite metodo POST para o endpoint /auth/register apenas quem possui a Role ADMIN
                        .anyRequest().authenticated()	// Informa que qualquer outro Request precisa estar autenticado
                        
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); 	//Diz para a aplicação aplicar e utilizar o filtro que criamos, ANTES do filtro UsernamePasswordAuthenticationFilter
        
        return httpSecurity.build();
    }

    @Bean	// Beam necessário para realizar o encoding(criptografia) da senha(password). 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }
}
