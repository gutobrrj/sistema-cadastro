package br.com.sistema.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.sistema.model.entity.Usuario;
import br.com.sistema.repository.UsuarioRepository;
import br.com.sistema.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component	// Classe extende a classe de filtro do Spring Security que vai rodar uma vez a cada Request que chegar na API
public class SecurityFilter extends OncePerRequestFilter {

	// Esta classe fará a verificação do usuario. 
	// Classe verificará se o Token que o usuário mandou, é/está válido e se foi emitido pela nossa aplicação 
	
	@Autowired
    TokenService tokenService;
	
    @Autowired
    UsuarioRepository usuarioRepository;

    
    @Override	// Método sobrescrito que inicia na primeira Request
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	var token = this.recoverToken(request); 		// Pega apenas o "Authorization" do header da requisição.
        var login = tokenService.validarToken(token);	//Pega o subject(username) retornado pelo método validarToken e atribui a variavel login
    	
    	if(login != null) {	
    		Usuario usuario = usuarioRepository.findByUsername(login).orElseThrow( () -> new RuntimeException("Usuário1 '" + login + "' não encontrado.") );
//    		var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    		var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());	// Cria o objeto de autenticação
    		SecurityContextHolder.getContext().setAuthentication(authentication);	// Adiciona o objeto no contexto de segurança da aplicação criado pelo Spring Security
    	}
        filterChain.doFilter(request, response);	// Finaliza o nosso filtro e passa para o próximo filtro UsernamePasswordAuthenticationFilter
    }

    // Método que irá pegar o Request que veio do usuário, vai pegar o Header"Authorization" que veio na Request
    // Obs: Se o Token vier no body ou em outro local, este método deverá ser modificado.
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization"); // Cria uma variável e guarda o Authorization do Header
        if(authHeader == null) return null;					 // Se a variável estiver vazia, retorna null
        return authHeader.replace("Bearer ", "");			 // Se a variável estiver preenchida, retira do texto o tipo de autenticação e deixa apenas o valor(token)
    }
	
}
