package br.com.senac.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Entidade que representa um projeto")
@Entity
@Table(name = "projetos")
public class Projeto extends PanacheEntity {

    @Schema(description = "Nome do projeto", example = "Sistema de Gestão", maxLength = 100, required = true)
    @NotBlank(message = "O nome do projeto é obrigatório.")
    @Size(max = 100, message = "O nome do projeto não pode ultrapassar 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Schema(description = "Usuário responsável pelo projeto", required = true)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Schema(description = "Lista de tarefas do projeto")
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tarefa> tarefas;

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

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}