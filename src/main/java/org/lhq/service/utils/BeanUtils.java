package org.lhq.service.utils;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.List;

@Singleton
public class BeanUtils {

    @Inject
    @Named("bookLoader")
    EntityLoader<BookInfo> bookLoader;

    @Inject
    @Named("movieLoader")
    EntityLoader<MovieInfo> movieLoader;

    @Inject
    @Named("imageLoader")
    EntityLoader<List<Byte>> imageLoader;

    @Inject
    @Named("bookParser")
    HtmlParseProvider<BookInfo> htmlToBookParser;

    @Inject
    @Named("bookLoader")
    SearchLoader<BookInfo> searchLoader;

    @Inject
    DirConfigProperties dirConfigProperties;

    public EntityLoader<BookInfo> getBookLoader() {
        return bookLoader;
    }

    public EntityLoader<MovieInfo> getMovieLoader() {
        return movieLoader;
    }

    public EntityLoader<List<Byte>> getImageLoader() {
        return imageLoader;
    }

    public DirConfigProperties getDirConfigProperties() {
        return dirConfigProperties;
    }

    public HtmlParseProvider<BookInfo> getHtmlToBookParser() {
        return htmlToBookParser;
    }

    public SearchLoader<BookInfo> getSearchLoader() {
        return searchLoader;
    }
}
