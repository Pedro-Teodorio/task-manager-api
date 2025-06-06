package br.com.senac.repositories;

import br.com.senac.entities.Projeto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ProjetoRepository implements PanacheRepository<Projeto> {
}
