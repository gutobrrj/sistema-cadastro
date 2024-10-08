package br.com.cnsoftware.sistema.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.cnsoftware.sistema.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component	// Classe extende a classe de filtro do Spring Security que vai rodar uma vez a cada Request que chegar na API
public class SecurityFilter extends OncePerRequestFilter {

	// Esta classe fará a verificação do usuario. 
	// Classe verificará se o Token que o usuário mandou, é/está válido e se foi emitido pela nossa aplicação 
	
	@Autowired
    private TokenService tokenService;
	
    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @Override	// Método sobrescrito que inicia na primeira Request
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	var token = this.obterTokenDaRequisicao(request); 		// Pega apenas o "Authorization" do header da requisição.
        
    	if(token != null) {	
    		var loginSubject = tokenService.validarToken(token);						// Pega o token obtido da requisição e passas para a classe de serviço para validar o token
    		if(loginSubject != null){													// Verifica se a variável tokenValidado não está vazia. Significando que ela foi obtida e validada com sucesso
    			UserDetails usuario = usuarioRepository.findByLogin(loginSubject); 		// Utiliza o Subject(login) retornado do token gerado, e busca no banco pelo login.
    			var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());	// Cria o objeto de autenticação 
    			SecurityContextHolder.getContext().setAuthentication(authentication);	// Adiciona o objeto no contexto de segurança da aplicação criado pelo Spring Security
    		}
    	}
        filterChain.doFilter(request, response);	// Finaliza o nosso filtro e passa para o próximo filtro UsernamePasswordAuthenticationFilter
    }

    // Método que irá pegar o Request que veio do usuário, vai pegar o Header"Authorization" que veio na Request
    // Obs: Se o Token vier no body ou em outro local, este método deverá ser modificado.
    private String obterTokenDaRequisicao(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization"); // Cria uma variável e guarda o Authorization do Header
        if(authHeader == null) return null;					 // Se a variável estiver vazia, retorna null
        return authHeader.replace("Bearer ", "");			 // Se a variável estiver preenchida, retira do texto o tipo de autenticação e deixa apenas o valor(token)
    }
	
}
