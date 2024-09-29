package org.lhq.service.loader.impl;

import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class BookLoader extends EntityLoader<BookInfo> {


    private static final Logger log = LoggerFactory.getLogger(BookLoader.class);

    protected BookLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public BookInfo load(HtmlParseProvider<BookInfo> htmlParseProvider, String id) {
        String url = super.processUrl(BookInfo.class,id);
        log.info("load book info from url:{}", url);
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            String htmlStr = response.body();
            return htmlParseProvider.parse(url,htmlStr);
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return null;
        }
    }
}
