package com.github.joostvdg.cmg.resource;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/hello")
public class HelloController {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String index(Principal principal) {
        return "Hello " + principal.getName();
    }

}