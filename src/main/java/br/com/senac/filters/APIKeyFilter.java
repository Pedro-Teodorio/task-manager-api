package br.com.senac.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class APIKeyFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "app.api-key")
    String validApiKey;

    @Override
    public void filter(ContainerRequestContext ctx) {
        String key = ctx.getHeaderString("x-api-key");
        if (key == null || !key.equals(validApiKey)) {
            ctx.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"API key inválida\"}")
                    .build());
        }
    }
}
