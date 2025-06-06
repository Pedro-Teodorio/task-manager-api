package br.com.senac.entities;

import br.com.senac.entities.enums.StatusTarefa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Entidade que representa uma tarefa")
@Entity
@Table(name = "tarefas")
public class Tarefa extends PanacheEntity {

    @Schema(description = "Nome da tarefa", example = "Implementar autenticação", maxLength = 100)
    @NotBlank(message = "O nome da tarefa é obrigatório.")
    @Size(max = 100, message = "O nome da tarefa não pode ultrapassar 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Schema(description = "Status da tarefa", example = "PENDENTE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Schema(description = "Projeto relacionado à tarefa")
    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Schema(description = "Lista de usuários atribuídos à tarefa")
    @ManyToMany
    @JoinTable(
            name = "tarefa_usuario",
            joinColumns = @JoinColumn(name = "tarefa_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
