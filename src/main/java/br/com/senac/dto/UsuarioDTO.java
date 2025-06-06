package br.com.senac.dto;

import br.com.senac.entities.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO (Data Transfer Object) para a entidade Usuario.
 * Utilizado para transferir dados do usuário entre as camadas da aplicação.
 */
public class UsuarioDTO {
    /**
     * Identificador único do usuário.
     */
    public Long id;

    /**
     * Nome do usuário.
     */
    public String nome;

    /**
     * E-mail do usuário.
     */
    public String email;

    /**
     * Identificador da configuração associada ao usuário.
     */
    public Long configuracaoId;

    /**
     * Lista de identificadores dos projetos associados ao usuário.
     */
    public List<Long> projetosIds = new ArrayList<>();

    /**
     * Converte uma entidade Usuario em um UsuarioDTO.
     *
     * @param u Entidade Usuario a ser convertida.
     * @return UsuarioDTO correspondente à entidade fornecida.
     */
    public static UsuarioDTO fromEntity(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.id = u.id;
        dto.nome = u.getNome();
        dto.email = u.getEmail();
        if (u.getConfiguracao() != null) {
            dto.configuracaoId = u.getConfiguracao().id;
        }
        if (u.getProjetos() != null) {
            dto.projetosIds = u.getProjetos().stream()
                    .map(p -> p.id)
                    .collect(Collectors.toList());
        }
        return dto;
    }

    /**
     * Converte este DTO em uma entidade Usuario.
     *
     * @return Entidade Usuario correspondente a este DTO.
     */
    public Usuario toEntity() {
        Usuario u = new Usuario();
        u.setNome(this.nome);
        u.setEmail(this.email);
        return u;
    }
}