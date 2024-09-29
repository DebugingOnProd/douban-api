package org.lhq.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lhq.entity.CelebrityInfo;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.List;

@Path("/movie")
public class MovieApiController {
    private final EntityLoader<MovieInfo> movieLoader;
    private final EntityLoader<List<CelebrityInfo>> celebrityLoader;
    private final HtmlParseProvider<MovieInfo> htmlParseProvider;
    private final HtmlParseProvider<List<CelebrityInfo>> htmlToCelebrityInfoProvider;

    public MovieApiController(EntityLoader<MovieInfo> movieLoader,
                              EntityLoader<List<CelebrityInfo>> celebrityLoader,
                              HtmlParseProvider<MovieInfo> htmlParseProvider,
                              HtmlParseProvider<List<CelebrityInfo>> htmlToCelebrityInfoProvider) {
        this.movieLoader = movieLoader;
        this.celebrityLoader = celebrityLoader;
        this.htmlParseProvider = htmlParseProvider;
        this.htmlToCelebrityInfoProvider = htmlToCelebrityInfoProvider;
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieInfo getBook(@PathParam("id") String id) {
        return movieLoader.load(htmlParseProvider,id);
    }

    @GET
    @Path("{id}/cast")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CelebrityInfo> getMovieCast(@PathParam("id") String id) {
        return celebrityLoader.load(htmlToCelebrityInfoProvider,id);
    }
}
