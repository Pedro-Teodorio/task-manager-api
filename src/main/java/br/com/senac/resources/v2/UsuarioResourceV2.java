package br.com.senac.resources.v2;

import br.com.senac.dto.UsuarioDTO;
import br.com.senac.entities.Usuario;
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

@Path("/api/v2/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários V2", description = "Gerenciamento de usuários na versão 2 da API")
@RateLimit(
        value = 10, // Limite de 10 requisições por segundo
        window = 1, // Janela de 1 segundo
        windowUnit = ChronoUnit.MINUTES
)
public class UsuarioResourceV2 {

    @Inject
    UsuarioRepository repo;

    @GET
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de usuários",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    public List<UsuarioDTO> listAll() {
        return repo.listAll().stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Busca um usuário por ID", description = "Retorna os detalhes de um usuário específico pelo seu ID.")
    @APIResponse(responseCode = "200", description = "Usuário encontrado",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response getById(@PathParam("id") Long id) {
        return repo.findByIdOptional(id)
                .map(u -> Response.ok(UsuarioDTO.fromEntity(u)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Operation(summary = "Cria um novo usuário (idempotente)", description = "Cadastra um novo usuário no sistema.")
    @APIResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Response create(@HeaderParam("Idempotency-Key") String idemKey,
                           UsuarioDTO dto,
                           @Context UriInfo uriInfo) {
        Usuario u = dto.toEntity();
        repo.persist(u);
        URI uri = uriInfo.getAbsolutePathBuilder().path(u.id.toString()).build();
        return Response.created(uri)
                .entity(UsuarioDTO.fromEntity(u))
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Operation(summary = "Atualiza um usuário (idempotente)", description = "Atualiza os dados de um usuário existente.")
    @APIResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response update(@HeaderParam("Idempotency-Key") String idemKey, @PathParam("id") Long id, UsuarioDTO dto) {
        return repo.findByIdOptional(id)
                .map(u -> {
                    u.setNome(dto.nome);
                    u.setEmail(dto.email);
                    repo.getEntityManager().merge(u);
                    return Response.ok(UsuarioDTO.fromEntity(u)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(summary = "Deleta um usuário", description = "Remove um usuário do sistema pelo seu ID.")
    @APIResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response delete(@PathParam("id") Long id) {
        boolean removed = repo.deleteById(id);
        return removed
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/projetos")
    @Operation(summary = "Lista os projetos de um usuário", description = "Retorna os IDs dos projetos associados a um usuário específico.")
    @APIResponse(responseCode = "200", description = "Lista de IDs dos projetos do usuário",
            content = @Content(schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public List<Long> getProjetos(@PathParam("id") Long id) {
        return repo.findByIdOptional(id)
                .map(u -> u.getProjetos().stream()
                        .map(p -> p.id)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}