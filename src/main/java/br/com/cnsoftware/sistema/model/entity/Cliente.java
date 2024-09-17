package br.com.cnsoftware.sistema.model.entity;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import br.com.cnsoftware.sistema.model.entity.enums.Sexo;
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

@Entity(name = "cliente")
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
	
	@OneToMany
	@JoinColumn(name = "id_cliente")
	private List<Endereco> enderecos;
	
	@OneToMany
	@JoinColumn(name = "id_cliente")
	private List<String> telefones;
	
	@OneToMany
	@JoinColumn(name = "id_cliente")
	private List<String> emails;
	
	@Column(name = "login")
	private String login;
	
	@Column(name = "senha")
	private String senha;
	
}
