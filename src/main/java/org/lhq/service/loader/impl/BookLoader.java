package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class BookLoader extends EntityLoader<BookInfo> implements SearchLoader<BookInfo> {


    private static final Logger log = LoggerFactory.getLogger(BookLoader.class);

    protected BookLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public BookInfo load(HtmlParseProvider<BookInfo> htmlParseProvider, String id) {
        String url = processUrl(new TypeToken<>() {},id);
        log.info("load book info from url:{}", url);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .ignoreContentType(true)
                    .execute();
            String htmlStr = response.body();
            return htmlParseProvider.parse(url,htmlStr);
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return null;
        }
    }

    @Override
    public List<BookInfo> search(String keyword) {
        return Collections.emptyList();
    }
}
