package org.lhq.controller;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.HostInfo;
import org.lhq.entity.book.BookVo;
import org.lhq.service.book.BookService;
import org.lhq.service.image.ImageProxy;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.List;

@Path("/book")
@Consumes({ MediaType.APPLICATION_JSON})
public class BookApiController {


    private final EntityLoader<BookInfo> bookLoader;

    private final HtmlParseProvider<BookInfo> htmlParseProvider;

    private final ImageProxy<BookInfo> imageProxy;

    private final SearchLoader<BookInfo> searchLoader;

    private final BookService bookService;

    public BookApiController(EntityLoader<BookInfo> bookLoader,
                             HtmlParseProvider<BookInfo> htmlParseProvider,
                             ImageProxy<BookInfo> imageProxy,
                             SearchLoader<BookInfo> searchLoader, BookService bookService) {
        this.bookLoader = bookLoader;
        this.htmlParseProvider = htmlParseProvider;
        this.imageProxy = imageProxy;
        this.searchLoader = searchLoader;
        this.bookService = bookService;
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

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookInfo> search(@QueryParam("keyword") String keyword) {
        return searchLoader.search(htmlParseProvider,keyword);
    }

    @GET
    @Path("classified")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookVo> classifiedBookList() {
        return bookService.getBookList();
    }

    @GET
    @Path("local/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BookInfo getLocalBook(String id) {
        return bookService.getBookInfo(id);
    }


}
