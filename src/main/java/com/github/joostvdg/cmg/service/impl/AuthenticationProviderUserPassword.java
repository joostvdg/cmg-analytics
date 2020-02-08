package com.github.joostvdg.cmg.service.impl;
import io.micronaut.context.annotation.Property;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Property(name = "cmg.analytics.user")
    protected String user;

    @Property(name = "cmg.analytics.password")
    protected String password;

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getIdentity().equals(user) &&
                authenticationRequest.getSecret().equals(password)) {
            return Flowable.just(new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>()));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}