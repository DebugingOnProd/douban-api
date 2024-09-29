package org.lhq.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;

@Path("/movie")
public class MovieApiController {
    private final EntityLoader<MovieInfo> movieLoader;
    private final HtmlParseProvider<MovieInfo> htmlParseProvider;

    public MovieApiController(EntityLoader<MovieInfo> movieLoader, HtmlParseProvider<MovieInfo> htmlParseProvider) {
        this.movieLoader = movieLoader;
        this.htmlParseProvider = htmlParseProvider;
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieInfo getBook(@PathParam("id") String id) {
        return movieLoader.load(htmlParseProvider,id);
    }
}
