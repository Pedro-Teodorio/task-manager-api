package br.com.senac.dto;

import br.com.senac.entities.ConfiguracaoUsuario;
import br.com.senac.entities.Usuario;

/**
 * DTO para transferir dados de ConfiguracaoUsuario.
 */
public class ConfiguracaoUsuarioDTO {
    /**
     * Identificador da configuração do usuário.
     */
    public Long id;

    /**
     * Nome da configuração.
     */
    public String nome;

    /**
     * Identificador do usuário associado.
     */
    public Long usuarioId;

    /**
     * Converte uma entidade ConfiguracaoUsuario em um DTO.
     *
     * @param c Entidade ConfiguracaoUsuario
     * @return DTO correspondente
     */
    public static ConfiguracaoUsuarioDTO fromEntity(ConfiguracaoUsuario c) {
        ConfiguracaoUsuarioDTO dto = new ConfiguracaoUsuarioDTO();
        dto.id = c.id;
        dto.nome = c.getNome();
        dto.usuarioId = c.getUsuario().id;
        return dto;
    }

    /**
     * Converte este DTO em uma entidade ConfiguracaoUsuario.
     *
     * @param usuario Entidade Usuario associada
     * @return Entidade ConfiguracaoUsuario correspondente
     */
    public ConfiguracaoUsuario toEntity(Usuario usuario) {
        ConfiguracaoUsuario c = new ConfiguracaoUsuario();
        c.setNome(this.nome);
        c.setUsuario(usuario);
        return c;
    }
}
