package org.lhq.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lhq.cache.CacheEnum;
import org.lhq.cache.CacheService;
import org.lhq.entity.book.BookInfo;

@Path("/cache")
public class CacheController {
    private final CacheService<String, BookInfo> cacheService;

    public CacheController() {
        this.cacheService = CacheEnum.INSTANCE.getBookCacheService();
    }

    @Path("{key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BookInfo get(@PathParam("key") String key) {
        return cacheService.get(key);
    }

    @Path("size")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public int size() {
        return cacheService.size();
    }

    @Path("clear")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String clear() {
        cacheService.clear();
        return "ok";
    }

}
