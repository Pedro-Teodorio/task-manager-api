package br.com.senac.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager API",
                version = "1.0.0",
                description = "API REST para gerenciamento de tarefas, projetos e usuários. " +
                        "Permite criar, atualizar, listar e excluir tarefas e projetos, além de gerenciar usuários associados. " +
                        "<h3>Funcionalidades Principais</h3>" +
                        "<ul>" +
                        "<li><strong>Usuários</strong>: Gerencie usuários com informações detalhadas e configurações personalizadas.</li>" +
                        "<li><strong>Projetos</strong>: Crie e gerencie projetos, vinculando-os a usuários responsáveis.</li>" +
                        "<li><strong>Tarefas</strong>: Gerencie tarefas associadas a projetos, incluindo status e atribuição de usuários.</li>" +
                        "<li><strong>Configurações</strong>: Personalize configurações específicas para cada usuário.</li>" +
                        "</ul>",
                contact = @Contact(
                        name = "Pedro Silva",
                        url = "https://github.com/Pedro-Teodorio"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        tags = {
                @Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema - v1"),
                @Tag(name = "Projetos", description = "Gerenciamento de projetos do sistema - v1"),
                @Tag(name = "Tarefas", description = "Gerenciamento de tarefas do sistema - v1"),

                @Tag(name = "Usuários V2", description = "Gerenciamento de usuários do sistema - v2"),
                @Tag(name = "Projetos V2", description = "Gerenciamento de projetos do sistema - v2"),
                @Tag(name = "Tarefas V2", description = "Gerenciamento de tarefas do sistema - v2")
        }
)
public class OpenApiConfig extends Application {
    // Classe vazia, apenas para configuração da versão 1
}

