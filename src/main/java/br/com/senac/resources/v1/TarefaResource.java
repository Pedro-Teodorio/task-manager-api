package br.com.senac.resources.v1;

import br.com.senac.dto.TarefaDTO;
import br.com.senac.entities.Projeto;
import br.com.senac.entities.Tarefa;
import br.com.senac.entities.Usuario;
import br.com.senac.entities.enums.StatusTarefa;
import br.com.senac.repositories.ProjetoRepository;
import br.com.senac.repositories.TarefaRepository;
import br.com.senac.repositories.UsuarioRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/v1/tarefas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas do sistema")
public class TarefaResource {

    @Inject
    TarefaRepository repo;
    @Inject
    ProjetoRepository projetoRepo;
    @Inject
    UsuarioRepository usuarioRepo;

    @GET
    @Operation(summary = "Lista todas as tarefas", description = "Retorna uma lista de todas as tarefas cadastradas.")
    @APIResponse(responseCode = "200", description = "Lista de tarefas",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "204", description = "Nenhuma tarefa encontrada")
    public List<TarefaDTO> listAll() {
        return repo.listAll()
                .stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Busca uma tarefa por ID", description = "Retorna os detalhes de uma tarefa específica pelo seu ID.")
    @APIResponse(responseCode = "200", description = "Tarefa encontrada",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "404", description = "Tarefa não encontrada")
    public Response getById(@PathParam("id") Long id) {
        return repo.findByIdOptional(id)
                .map(t -> Response.ok(TarefaDTO.fromEntity(t)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Operation(summary = "Cria uma nova tarefa", description = "Cadastra uma nova tarefa vinculada a um projeto e, opcionalmente, atribui usuários.")
    @APIResponse(responseCode = "201", description = "Tarefa criada com sucesso",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "404", description = "Projeto não encontrado")
    public Response create(TarefaDTO dto, @Context UriInfo uriInfo) {
        Projeto p = projetoRepo.findByIdOptional(dto.projetoId)
                .orElseThrow(() -> new WebApplicationException("Projeto não encontrado", 404));
        Tarefa t = dto.toEntity();
        t.setProjeto(p);
        for (Long uid : dto.usuariosIds) {
            Usuario u = usuarioRepo.findById(uid);
            if (u != null) t.getUsuarios().add(u);
        }
        repo.persist(t);
        URI uri = uriInfo.getAbsolutePathBuilder().path(t.id.toString()).build();
        return Response.created(uri).entity(TarefaDTO.fromEntity(t)).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Operation(summary = "Atualiza uma tarefa", description = "Atualiza os dados de uma tarefa existente.")
    @APIResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "404", description = "Tarefa não encontrada")
    public Response update(@PathParam("id") Long id, TarefaDTO dto) {
        return repo.findByIdOptional(id)
                .map(t -> {
                    t.setNome(dto.nome);
                    t.setStatus(dto.status);
                    repo.getEntityManager().merge(t);
                    return Response.ok(TarefaDTO.fromEntity(t)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(summary = "Deleta uma tarefa", description = "Remove uma tarefa do sistema pelo seu ID.")
    @APIResponse(responseCode = "204", description = "Tarefa deletada com sucesso")
    @APIResponse(responseCode = "404", description = "Tarefa não encontrada")
    public Response delete(@PathParam("id") Long id) {
        boolean removed = repo.deleteById(id);
        return removed
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("status/{status}")
    @Operation(summary = "Lista tarefas por status", description = "Retorna todas as tarefas com o status informado.")
    @APIResponse(responseCode = "200", description = "Lista de tarefas filtradas",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    public List<TarefaDTO> byStatus(@PathParam("status") StatusTarefa status) {
        return repo.findByStatus(status)
                .stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("usuario/{userId}")
    @Operation(summary = "Lista tarefas por usuário", description = "Retorna todas as tarefas atribuídas ao usuário informado.")
    @APIResponse(responseCode = "200", description = "Lista de tarefas filtradas",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    public List<TarefaDTO> byUsuario(@PathParam("userId") Long userId) {
        return repo.findByUsuarioId(userId)
                .stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("projeto/{projId}")
    @Operation(summary = "Lista tarefas por projeto", description = "Retorna todas as tarefas vinculadas ao projeto informado.")
    @APIResponse(responseCode = "200", description = "Lista de tarefas filtradas",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    public List<TarefaDTO> byProjeto(@PathParam("projId") Long projId) {
        return repo.findByProjetoId(projId)
                .stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}/usuarios")
    @Operation(summary = "Lista usuários atribuídos à tarefa", description = "Retorna os IDs dos usuários atribuídos à tarefa informada.")
    @APIResponse(responseCode = "200", description = "Lista de IDs de usuários")
    public List<Long> listUsuarios(@PathParam("id") Long id) {
        return repo.findByIdOptional(id)
                .map(t -> t.getUsuarios().stream()
                        .map(u -> u.id)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @POST
    @Path("{taskId}/usuarios/{userId}")
    @Transactional
    @Operation(summary = "Atribui usuário à tarefa", description = "Atribui um usuário existente à tarefa informada.")
    @APIResponse(responseCode = "200", description = "Usuário atribuído com sucesso",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "404", description = "Tarefa ou usuário não encontrado")
    public Response assignUsuario(@PathParam("taskId") Long taskId,
                                  @PathParam("userId") Long userId) {
        Tarefa t = repo.findById(taskId);
        Usuario u = usuarioRepo.findById(userId);
        if (t == null || u == null) {
            throw new WebApplicationException("Tarefa ou usuário não encontrado", 404);
        }
        t.getUsuarios().add(u);
        return Response.ok(TarefaDTO.fromEntity(t)).build();
    }

    @PATCH
    @Path("{id}/status")
    @Transactional
    @Operation(summary = "Atualiza status da tarefa", description = "Atualiza o status de uma tarefa existente.")
    @APIResponse(responseCode = "200", description = "Status atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = TarefaDTO.class)))
    @APIResponse(responseCode = "404", description = "Tarefa não encontrada")
    public Response updateStatus(@PathParam("id") Long id, StatusTarefa novoStatus) {
        return repo.findByIdOptional(id)
                .map(t -> {
                    t.setStatus(novoStatus);
                    repo.getEntityManager().merge(t);
                    return Response.ok(TarefaDTO.fromEntity(t)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}