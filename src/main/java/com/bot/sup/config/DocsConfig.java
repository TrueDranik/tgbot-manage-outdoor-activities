package com.bot.sup.config;

import com.bot.sup.exception.ApiExceptionResponseBody;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

@Configuration
public class DocsConfig {
    @Value("${app.docs.title:Applicant Store API Docs}")
    private String apiTitle;

    @Value("${app.docs.version:v0.5}")
    private String apiVersion;

    @Bean
    public GlobalOpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.setInfo(new Info().title(apiTitle).version(apiVersion));
    }

    @Bean
    public GlobalOperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            interceptGlobal(operation);
            interceptDocumentedOperation(operation, handlerMethod);
            return operation;
        };
    }

    private void interceptGlobal(Operation operation) {
        addErrorResponse(operation, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void interceptDocumentedOperation(Operation operation, HandlerMethod handlerMethod) {
        DocumentedOperation annotation = handlerMethod.getMethodAnnotation(DocumentedOperation.class);
        if (annotation != null) {
            Arrays.stream(annotation.errors()).forEach(s -> addErrorResponse(operation, s));
            if (!annotation.desc().isBlank()) {
                operation.setSummary(annotation.desc());
            }
        }
    }

    private void addErrorResponse(Operation operation, HttpStatus status) {
        addErrorResponse(operation, status, ApiExceptionResponseBody.class);
    }

    private void addErrorResponse(Operation operation, HttpStatus status, Class<?> body) {
        if (!status.isError()) {
            return;
        }
        ApiResponse response = new ApiResponse();
        if (body != null) {
            ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(body).resolveAsRef(false));
            Content content = new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    new MediaType().schema(resolvedSchema.schema));
            response.setContent(content);
        }
        response.setDescription(status.getReasonPhrase());
        operation.getResponses().addApiResponse(Integer.toString(status.value()), response);
    }
}
