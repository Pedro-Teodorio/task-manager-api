package br.com.senac.resources.v2;

import br.com.senac.dto.ProjetoDTO;
import br.com.senac.entities.Projeto;
import br.com.senac.entities.Usuario;
import br.com.senac.repositories.ProjetoRepository;
import br.com.senac.repositories.UsuarioRepository;
import io.smallrye.faulttolerance.api.RateLimit;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/v2/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Projetos V2", description = "Gerenciamento de projetos na versão 2 da API")
@RateLimit(
        value = 10, // Limite de 10 requisições por segundo
        window = 1, // Janela de 1 segundo
        windowUnit = ChronoUnit.MINUTES
)
public class ProjetoResourceV2 {

    @Inject
    ProjetoRepository repo;
    @Inject
    UsuarioRepository usuarioRepo;

    @GET
    @Operation(summary = "Lista todos os projetos", description = "Retorna uma lista de todos os projetos cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de projetos",
            content = @Content(schema = @Schema(implementation = ProjetoDTO.class)))
    @APIResponse(responseCode = "204", description = "Nenhum projeto encontrado")
    public List<ProjetoDTO> listAll() {
        return repo.listAll()
                .stream()
                .map(ProjetoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Busca um projeto por ID", description = "Retorna os detalhes de um projeto específico pelo seu ID.")
    @APIResponse(responseCode = "200", description = "Projeto encontrado",
            content = @Content(schema = @Schema(implementation = ProjetoDTO.class)))
    @APIResponse(responseCode = "404", description = "Projeto não encontrado")
    public Response getById(@PathParam("id") Long id) {
        return repo.findByIdOptional(id)
                .map(p -> Response.ok(ProjetoDTO.fromEntity(p)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/usuario/{userId}")
    @Transactional
    @Operation(summary = "Cria um novo projeto para um usuário (idempotente)", description = "Cadastra um novo projeto vinculado a um usuário.")
    @APIResponse(responseCode = "201", description = "Projeto criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjetoDTO.class)))
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response create(@HeaderParam("Idempotency-Key") String idemKey,
                           @PathParam("userId") Long userId,
                           ProjetoDTO dto,
                           @Context UriInfo uriInfo) {
        Usuario u = usuarioRepo.findByIdOptional(userId)
                .orElseThrow(() -> new WebApplicationException("Usuário não encontrado", 404));
        Projeto p = dto.toEntity();
        p.setUsuario(u);
        repo.persist(p);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(p.id)).build();
        return Response.created(uri)
                .entity(ProjetoDTO.fromEntity(p))
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Operation(summary = "Atualiza um projeto (idempotente)", description = "Atualiza os dados de um projeto existente.")
    @APIResponse(responseCode = "200", description = "Projeto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjetoDTO.class)))
    @APIResponse(responseCode = "404", description = "Projeto não encontrado")
    public Response update(@HeaderParam("Idempotency-Key") String idemKey,
                           @PathParam("id") Long id,
                           ProjetoDTO dto) {
        return repo.findByIdOptional(id)
                .map(p -> {
                    p.setNome(dto.nome);
                    repo.getEntityManager().merge(p);
                    return Response.ok(ProjetoDTO.fromEntity(p)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(summary = "Deleta um projeto", description = "Remove um projeto do sistema pelo seu ID.")
    @APIResponse(responseCode = "204", description = "Projeto deletado com sucesso")
    @APIResponse(responseCode = "404", description = "Projeto não encontrado")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = repo.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
