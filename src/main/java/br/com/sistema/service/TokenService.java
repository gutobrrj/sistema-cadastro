package br.com.sistema.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.sistema.model.entity.Usuario;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")	// Variável existente em application.properties
	private String secret;
	
	
	// Método que realiza a criação do Token
	public String generateToken(Usuario usuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret); 	// Define a chave(texto) que será utilizada para ser criptografada
			String token = JWT.create()							
					.withIssuer("sistema-cadastro") 			// Quem está emitindo o token - Neste caso é o nosso projeto
					.withSubject(usuario.getUsername())			// Define quem é o dono do token - Este é o atributo que retornaremos após a validação para retornar um UserDatail através do método findByXXX
					.withExpiresAt(this.generateExpirationDate())	// Define o tempo de expiração do Token
					.sign(algorithm);							// Assina/Cria o Token
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Erro durante a geração do token.", e);
		}
	}
	
	
	// Método que gera o tempo de expiração do Token
	public Instant generateExpirationDate() {
		return LocalDateTime.now()					// Pega o tempo/hora atual
				.plusHours(2)						// Adiciona mais 2 horas
				.toInstant(ZoneOffset.of("-03:00"));	// Transforma em Instante(Utilizado pelo JWT)
	}
	
	
	// Método realiza a validação do Token
	public String validarToken(String token) {
		 try {
			 Algorithm algorithm = Algorithm.HMAC256(secret); 	// Define a chave(texto) que será utilizada para ser criptografada
			 return JWT.require(algorithm)
					 .withIssuer("sistema-cadastro") 			// Quem está emitindo o token - Neste caso é o nosso projeto
					 .build()									// Monta o objeto para realizar a verificação
					 .verify(token)								// Inicia/executa a verificação
					 .getSubject();								// Devolve o "dono" que solicitou o token. Informado no método generateToken(). Retornaremos este valor para a classe de configuração para utilizar no findByXXX do repository
		} catch (JWTVerificationException e) {
			return null;	//Caso ocorra alguma falha, retornará null
		}
	}
}
