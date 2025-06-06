package br.com.senac.repositories;

import br.com.senac.entities.ConfiguracaoUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfiguracaoUsuarioRepository implements PanacheRepository<ConfiguracaoUsuario> {
}