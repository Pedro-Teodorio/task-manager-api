package br.com.senac.dto;

import br.com.senac.entities.Tarefa;
import br.com.senac.entities.enums.StatusTarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para transferência de dados da entidade Tarefa.
 */
public class TarefaDTO {
    /**
     * Identificador da tarefa.
     */
    public Long id;

    /**
     * Nome da tarefa.
     */
    public String nome;

    /**
     * Status da tarefa.
     */
    public StatusTarefa status;

    /**
     * Identificador do projeto relacionado à tarefa.
     */
    public Long projetoId;

    /**
     * Lista de identificadores dos usuários associados à tarefa.
     */
    public List<Long> usuariosIds = new ArrayList<>();

    /**
     * Converte uma entidade Tarefa em um TarefaDTO.
     *
     * @param t entidade Tarefa a ser convertida
     * @return DTO correspondente à entidade
     */
    public static TarefaDTO fromEntity(Tarefa t) {
        TarefaDTO dto = new TarefaDTO();
        dto.id = t.id;
        dto.nome = t.getNome();
        dto.status = t.getStatus();
        dto.projetoId = t.getProjeto().id;
        if (t.getUsuarios() != null) {
            dto.usuariosIds = t.getUsuarios()
                    .stream()
                    .map(u -> u.id)
                    .collect(Collectors.toList());
        }
        return dto;
    }

    /**
     * Converte este DTO em uma entidade Tarefa.
     *
     * @return entidade Tarefa correspondente ao DTO
     */
    public Tarefa toEntity() {
        Tarefa t = new Tarefa();
        t.setNome(this.nome);
        t.setStatus(this.status);
        return t;
    }
}
