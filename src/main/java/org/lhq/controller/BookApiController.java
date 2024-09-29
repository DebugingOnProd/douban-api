package org.lhq.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;

@Path("/book")
public class BookApiController {


    private final EntityLoader<BookInfo> bookLoader;

    private final HtmlParseProvider<BookInfo> htmlParseProvider;

    @Inject
    public BookApiController(EntityLoader<BookInfo> bookLoader,
                             HtmlParseProvider<BookInfo> htmlParseProvider) {
        this.bookLoader = bookLoader;
        this.htmlParseProvider = htmlParseProvider;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookInfo getBook(@PathParam("id") String id) {
        return bookLoader.load(htmlParseProvider,id);
    }
}
