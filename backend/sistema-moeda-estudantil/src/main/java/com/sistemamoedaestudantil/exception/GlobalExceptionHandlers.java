package com.sistemamoedaestudantil.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

public class GlobalExceptionHandlers {

    @Singleton
    @Requires(classes = { NotFoundException.class, ExceptionHandler.class })
    @Produces
    public static class NotFoundHandler implements ExceptionHandler<NotFoundException, HttpResponse<ApiError>> {
        @Override
        public HttpResponse<ApiError> handle(HttpRequest request, NotFoundException ex) {
            ApiError err = new ApiError(HttpStatus.NOT_FOUND.getCode(), "Not Found", ex.getMessage());
            return HttpResponse.notFound(err);
        }
    }

    @Singleton
    @Requires(classes = { BusinessException.class, ExceptionHandler.class })
    @Produces
    public static class BusinessHandler implements ExceptionHandler<BusinessException, HttpResponse<ApiError>> {
        @Override
        public HttpResponse<ApiError> handle(HttpRequest request, BusinessException ex) {
            ApiError err = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Unprocessable Entity", ex.getMessage());
            return HttpResponse.<ApiError>unprocessableEntity().body(err);
        }
    }

    @Singleton
    @Requires(classes = { UnauthorizedException.class, ExceptionHandler.class })
    @Produces
    public static class UnauthorizedHandler implements ExceptionHandler<UnauthorizedException, HttpResponse<ApiError>> {
        @Override
        public HttpResponse<ApiError> handle(HttpRequest request, UnauthorizedException ex) {
            ApiError err = new ApiError(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized", ex.getMessage());
            return HttpResponse.status(HttpStatus.UNAUTHORIZED).body(err);
        }
    }

    @Singleton
    @Requires(classes = { ForbiddenException.class, ExceptionHandler.class })
    @Produces
    public static class ForbiddenHandler implements ExceptionHandler<ForbiddenException, HttpResponse<ApiError>> {
        @Override
        public HttpResponse<ApiError> handle(HttpRequest request, ForbiddenException ex) {
            ApiError err = new ApiError(HttpStatus.FORBIDDEN.getCode(), "Forbidden", ex.getMessage());
            return HttpResponse.status(HttpStatus.FORBIDDEN).body(err);
        }
    }
}
