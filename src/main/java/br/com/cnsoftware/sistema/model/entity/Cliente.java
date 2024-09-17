package br.com.cnsoftware.sistema.model.entity;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import br.com.cnsoftware.sistema.model.entity.enums.Sexo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Column(name = "cpf_cnpj")
	private String cpfCNPJ;

	@Column(name = "rg")
	private String rg;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "sexo")
	private Sexo sexo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_cliente")
	private List<Endereco> enderecos;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_cliente")
	private List<Contato> contatos;

	@Column(name = "login")
	private String login;

	@Column(name = "senha")
	private String senha;

	
	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	
	public Cliente(Long id, String nome, LocalDate dataNascimento, String cpfCNPJ, String rg, Sexo sexo,
			List<Endereco> enderecos, List<Contato> contatos, String login, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.cpfCNPJ = cpfCNPJ;
		this.rg = rg;
		this.sexo = sexo;
		this.enderecos = enderecos;
		this.contatos = contatos;
		this.login = login;
		this.senha = senha;
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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpfCNPJ() {
		return cpfCNPJ;
	}

	public void setCpfCNPJ(String cpfCNPJ) {
		this.cpfCNPJ = cpfCNPJ;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
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



}
