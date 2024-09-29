package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class MovieLoader extends EntityLoader<MovieInfo> {

    private static final Logger log = LoggerFactory.getLogger(MovieLoader.class);

    protected MovieLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public MovieInfo load(HtmlParseProvider<MovieInfo> htmlParseProvider, String id) {
        TypeToken<MovieInfo> typeToken = new TypeToken<>(){};
        String url = processUrl(typeToken,id);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .execute();
            String htmlStr = response.body();
            return htmlParseProvider.parse(url,htmlStr);
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return null;
        }
    }
}
