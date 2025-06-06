package br.com.senac.repositories;

import br.com.senac.entities.Tarefa;
import br.com.senac.entities.enums.StatusTarefa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
/**
 * Repositório para operações relacionadas à entidade Tarefa.
 */
public class TarefaRepository implements PanacheRepository<Tarefa> {

    /**
     * Busca tarefas pelo status informado.
     *
     * @param status o status da tarefa
     * @return lista de tarefas com o status especificado
     */
    public List<Tarefa> findByStatus(StatusTarefa status) {
        return list("status", status);
    }

    /**
     * Busca tarefas associadas a um projeto específico.
     *
     * @param projetoId o ID do projeto
     * @return lista de tarefas do projeto informado
     */
    public List<Tarefa> findByProjetoId(Long projetoId) {
        return list("projeto.id", projetoId);
    }

    /**
     * Busca tarefas atribuídas a um usuário específico.
     *
     * @param usuarioId o ID do usuário
     * @return lista de tarefas atribuídas ao usuário informado
     */
    public List<Tarefa> findByUsuarioId(Long usuarioId) {
        // join fetch usuários
        return list("select t from Tarefa t join t.usuarios u where u.id = ?1", usuarioId);
    }
}