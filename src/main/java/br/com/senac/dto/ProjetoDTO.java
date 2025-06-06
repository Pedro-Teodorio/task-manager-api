package br.com.senac.dto;

import br.com.senac.entities.Projeto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para a entidade Projeto.
 * Contém informações básicas do projeto e IDs relacionados.
 */
public class ProjetoDTO {
    /** Identificador do projeto */
    public Long id;
    /** Nome do projeto */
    public String nome;
    /** ID do usuário associado ao projeto */
    public Long usuarioId;
    /** Lista de IDs das tarefas associadas ao projeto */
    public List<Long> tarefasIds = new ArrayList<>();

    /**
     * Converte uma entidade Projeto em um ProjetoDTO.
     *
     * @param p Entidade Projeto a ser convertida
     * @return ProjetoDTO correspondente
     */
    public static ProjetoDTO fromEntity(Projeto p) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.id = p.id;
        dto.nome = p.getNome();
        dto.usuarioId = p.getUsuario().id;
        if (p.getTarefas() != null) {
            dto.tarefasIds = p.getTarefas().stream()
                    .map(t -> t.id)
                    .collect(Collectors.toList());
        }
        return dto;
    }

    /**
     * Converte este DTO em uma entidade Projeto.
     *
     * @return Entidade Projeto correspondente
     */
    public Projeto toEntity() {
        Projeto p = new Projeto();
        p.setNome(this.nome);
        return p;
    }
}
