package br.com.sistema.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class TokenFilter extends GenericFilterBean { // Classe que estende GenericFilterBean para criar um filtro personalizado para autenticação via JWT

	@Autowired
	private TokenService tokenProvider;

	public TokenFilter(TokenService tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException { // Método que intercepta requisições HTTP e aplica lógica de filtro

		String token = tokenProvider.recuperaToken((HttpServletRequest) request); // Tenta recuperar o token JWT da requisição HTTP (cabeçalho "Authorization")

		if (token != null && tokenProvider.validarToken(token)) { // Verifica se o token não é nulo e é válido
			Authentication authentication = tokenProvider.getAuthentication(token); // Obtém a autenticação associada ao token (usuário e permissões)

			if (authentication != null) { // Verifica se a autenticação foi bem-sucedida
				SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto de segurança do Spring
			}
		}

		chain.doFilter(request, response); // Continua o processamento da cadeia de filtros, passando a requisição e resposta adiante
	}
}
