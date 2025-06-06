package br.com.senac.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Entidade que representa um usuário do sistema")
@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntity {

    @Schema(description = "Nome do usuário", example = "João da Silva", maxLength = 100, required = true)
    @NotBlank(message = "O nome do usuário é obrigatório.")
    @Size(max = 100, message = "O nome do usuário não pode ultrapassar 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Schema(description = "Email do usuário", example = "joao@email.com", maxLength = 150, required = true)
    @NotBlank(message = "O email do usuário é obrigatório.")
    @Size(max = 150, message = "O email do usuário não pode ultrapassar 150 caracteres.")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Schema(description = "Configuração do usuário")
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ConfiguracaoUsuario configuracao;

    @Schema(description = "Projetos associados ao usuário")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Projeto> projetos;

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

    public ConfiguracaoUsuario getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(ConfiguracaoUsuario configuracao) {
        this.configuracao = configuracao;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }
}