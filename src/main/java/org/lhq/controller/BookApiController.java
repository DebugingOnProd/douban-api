package org.lhq.controller;

import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.common.jaxrs.UriBuilderImpl;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.entity.HostInfo;
import org.lhq.service.image.ImageProxy;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

@Path("/book")
public class BookApiController {


    private final EntityLoader<BookInfo> bookLoader;

    private final HtmlParseProvider<BookInfo> htmlParseProvider;

    private final ImageProxy<BookInfo> imageProxy;

    public BookApiController(EntityLoader<BookInfo> bookLoader, HtmlParseProvider<BookInfo> htmlParseProvider, ImageProxy<BookInfo> imageProxy) {
        this.bookLoader = bookLoader;
        this.htmlParseProvider = htmlParseProvider;
        this.imageProxy = imageProxy;
    }

    @Context
    HttpServerRequest request;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookInfo getBook(@PathParam("id") String id) {
        BookInfo bookInfo = bookLoader.load(htmlParseProvider, id);
        HostInfo hostInfo = new HostInfo();
        hostInfo.setHost(request.authority().host());
        hostInfo.setPort(request.authority().port());
        hostInfo.setScheme(request.scheme());
        return imageProxy.process(bookInfo, hostInfo);
    }

}
