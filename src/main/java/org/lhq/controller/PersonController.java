package org.lhq.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lhq.entity.PersonageInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {


    private final HtmlParseProvider<PersonageInfo> htmlParseProvider;

    private final EntityLoader<PersonageInfo> personLoader;

    public PersonController(HtmlParseProvider<PersonageInfo> htmlParseProvider,
                            EntityLoader<PersonageInfo> personLoader) {
        this.htmlParseProvider = htmlParseProvider;
        this.personLoader = personLoader;
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonageInfo getPerson(@PathParam("id") String id) {
        return personLoader.load(htmlParseProvider, id);
    }
}
