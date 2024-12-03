package org.lhq.service.utils;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.Getter;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.List;

@Getter
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

}
