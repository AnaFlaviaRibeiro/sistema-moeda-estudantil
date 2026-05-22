package com.sistemamoedaestudantil.security;

import com.sistemamoedaestudantil.exception.ApiError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.filter.ServerFilterPhase;
import org.reactivestreams.Publisher;

@Filter("/api/**")
public class AuthFilter implements HttpServerFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public int getOrder() {
        return ServerFilterPhase.SECURITY.order();
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        if (!request.getMethod().name().equals("OPTIONS") && isPublicPath(request)) {
            return chain.proceed(request);
        }

        String authHeader = request.getHeaders().getAuthorization().orElse(null);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return Publishers.just(unauthorized("Token de autenticação ausente ou inválido."));
        }

        try {
            String token = authHeader.substring(BEARER_PREFIX.length()).trim();
            AuthenticatedUser user = jwtService.parseToken(token);
            return chain.proceed(request.setAttribute(AuthenticatedUser.class.getName(), user));
        } catch (Exception ex) {
            return Publishers.just(unauthorized("Token de autenticação inválido ou expirado."));
        }
    }

    private boolean isPublicPath(HttpRequest<?> request) {
        String path = request.getPath();
        String method = request.getMethod().name();

        if (path.equals("/api/auth/login") && method.equals("POST")) {
            return true;
        }
        if (path.startsWith("/api/instituicoes") && method.equals("GET")) {
            return true;
        }
        if (path.equals("/api/alunos") && method.equals("POST")) {
            return true;
        }
        return path.equals("/api/empresas") && method.equals("POST");
    }

    private MutableHttpResponse<ApiError> unauthorized(String message) {
        ApiError err = new ApiError(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized", message);
        return HttpResponse.status(HttpStatus.UNAUTHORIZED).body(err);
    }
}
