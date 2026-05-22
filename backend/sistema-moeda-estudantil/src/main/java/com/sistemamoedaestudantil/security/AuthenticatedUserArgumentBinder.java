package com.sistemamoedaestudantil.security;

import io.micronaut.core.bind.ArgumentBinder;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.bind.binders.TypedRequestArgumentBinder;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class AuthenticatedUserArgumentBinder implements TypedRequestArgumentBinder<AuthenticatedUser> {

    @Override
    public Argument<AuthenticatedUser> argumentType() {
        return Argument.of(AuthenticatedUser.class);
    }

    @Override
    public BindingResult<AuthenticatedUser> bind(ArgumentConversionContext<AuthenticatedUser> context,
                                                 HttpRequest<?> source) {
        AuthenticatedUser user = source.getAttribute(AuthenticatedUser.class.getName(), AuthenticatedUser.class)
                .orElse(null);
        if (user == null) {
            return BindingResult.EMPTY;
        }
        return () -> Optional.of(user);
    }
}
