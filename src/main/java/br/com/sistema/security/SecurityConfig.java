package br.com.sistema.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private TokenService tokenProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() { // Define um bean de codificador de senhas para ser usado no sistema
	    return new BCryptPasswordEncoder(); // Retorna uma instância de BCryptPasswordEncoder, que é usada para criptografar senhas de maneira segura
	}

	@Bean
	public AuthenticationManager authenticationManagerTeste(AuthenticationConfiguration authenticationConfiguration) throws Exception { // Define um bean para o gerenciador de autenticação
	    return authenticationConfiguration.getAuthenticationManager(); // Retorna o gerenciador de autenticação configurado a partir do AuthenticationConfiguration
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Define um bean que configura a cadeia de filtros de segurança da aplicação
	    TokenFilter filtroCustomizado = new TokenFilter(tokenProvider); // Cria um filtro personalizado baseado no provedor de token JWT
	    return http
	            .httpBasic(basic -> basic.disable()) // Desativa a autenticação HTTP básica
	            .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF (Cross-Site Request Forgery), pois a aplicação é stateless
	            .addFilterBefore(filtroCustomizado, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro JWT personalizado antes do filtro de autenticação padrão do Spring Security
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura a gestão de sessão como "stateless", indicando que a aplicação não mantém estado de sessão
	            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
	                    .requestMatchers(
	                            "/auth/login", // Permite acesso público ao endpoint de login
	                            "/auth/refresh").permitAll() // Permite acesso público ao endpoint de refresh token
	                    .anyRequest().authenticated()) // Exige autenticação para qualquer outra requisição
	            .cors(cors -> {}) // Habilita o suporte a CORS (Cross-Origin Resource Sharing)
	            .build(); // Constrói e retorna o objeto SecurityFilterChain configurado
	}

	
}
