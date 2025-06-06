package br.com.senac.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Entidade que representa a configuração de um usuário")
    @Entity
    @Table(name = "configuracoes_usuario")
    public class ConfiguracaoUsuario extends PanacheEntity {

        @Schema(description = "Nome da configuração", example = "Tema Escuro", required = true)
        @NotBlank(message = "A configuração deve ter um nome.")
        @Column(nullable = false)
        private String nome;

        @Schema(description = "Usuário associado à configuração", required = true)
        @OneToOne
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}