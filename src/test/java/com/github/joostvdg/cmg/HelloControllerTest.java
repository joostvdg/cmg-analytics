package com.github.joostvdg.cmg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class HelloControllerTest {

    @Property(name = "cmg.analytics.user")
    protected String user;

    @Property(name = "cmg.analytics.password")
    protected String password;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testHello() {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
        HttpRequest request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);

        String accessToken = rsp.body().getAccessToken();
        HttpRequest requestWithAuthorization = HttpRequest.GET("/hello" ).bearerAuth(accessToken);
        HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String.class);
        String body = client.toBlocking().retrieve(requestWithAuthorization);

        assertNotNull(body);
        assertEquals("Hello " + user, body);
    }
}