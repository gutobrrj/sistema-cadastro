package br.com.cnsoftware.sistema.model.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.cnsoftware.sistema.model.entity.enums.RoleUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")	// Implementa a classe UserDetails do Spring Security para identificar uma classe de usuário que será autenticada na aplicação
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "login", unique = true, nullable = false)
	private String login;

	@Column(name = "senha", nullable = false)
	private String senha;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "role")
	private RoleUsuario role;

	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public RoleUsuario getRole() {
		return role;
	}

	public void setRole(RoleUsuario role) {
		this.role = role;
	}
	
	
	// ### MÉTODOS IMPLEMENTADOS/SOBRESCRITOS ###

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		// Se o usuário for do tipo enum Admin, então o usuário receberá uma lista com todas as ROLES para obter todas as permissões
		if(this.role == RoleUsuario.ADMIN) return List.of( new SimpleGrantedAuthority( "ROLE_ADMIN" ), new SimpleGrantedAuthority( "ROLE_USER" ) );
		
		// Caso contrário, se o usuário não for Admin, apenas a role de usuário é atribuída. 
		else return List.of( new SimpleGrantedAuthority( "ROLE_USER" ) );
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.login;	// Mudado o retorno de username para login
	}

}
