package br.com.sistema.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;

import br.com.sistema.dto.security.TokenDto;
import jakarta.annotation.PostConstruct;

@Service
public class JwtToken {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-lenght:3600000}")
	private long validadeToken = 3600000;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algorithm = null;
	
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenDto criarAccessToken(String username, List<String> roles) {
		Date agora = new Date();
		Date validade = new Date(agora.getTime() + validadeToken);
		var accessToken = obterAccessToken(username, roles, agora, validade);
		var refreshToken = obterAccessToken(username, roles, agora);
		return new TokenDto(username, true, agora, validade, accessToken, refreshToken);
	}

	private String obterAccessToken(String username, List<String> roles, Date agora) {
		return null;
	}

	private String obterAccessToken(String username, List<String> roles, Date agora, Date validade) {
		return null;
	}
}
