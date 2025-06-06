package br.com.senac.filters;

import jakarta.annotation.Priority;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Suporta cabeçalho Idempotency-Key em POST e PUT.
 * Armazena em memória o JSON serializado para chaves já vistas.
 */
@Provider
@Singleton
@Priority(Priorities.ENTITY_CODER)
public class IdempotencyFilter implements ContainerRequestFilter {

    private static final String IDEMPOTENCY_KEY = "Idempotency-Key";

    // apenas guarda que a chave já foi vista (não precisa do body)
    private final Set<String> seen = ConcurrentHashMap.newKeySet();

    @Override
    public void filter(ContainerRequestContext req) {
        String method = req.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method))) {
            return;
        }
        String key = req.getHeaderString(IDEMPOTENCY_KEY);
        if (key == null) {
            return;
        }
        // se já processado, aborta com mensagem simples
        if (!seen.add(key)) {
            req.abortWith(Response
                    .status(Response.Status.OK)
                    .entity(java.util.Map.of("message",
                            "Requisição já processada anteriormente (idempotência garantida)."))
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }
    }
}