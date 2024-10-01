package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.MovieInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.utils.DoubanUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Singleton
@Named("movieLoader")
public class MovieLoader extends EntityLoader<MovieInfo> implements SearchLoader<MovieInfo> {

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
            return htmlParseProvider.parse(url,response.parse());
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return null;
        }
    }

    @Override
    public List<MovieInfo> search(HtmlParseProvider<MovieInfo> htmlParseProvider, String keyword) {
        Map<String, String> searchMap = doubanApiConfigProperties.mappings();
        String movieCat = searchMap.get("movie");
        String url = doubanApiConfigProperties.searchUrl() + "?cat=" + movieCat + "&q=" + keyword;
        log.info("search movie info from url:{}", url);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .execute();
            String htmlStr = response.body();
            Document document = Jsoup.parse(htmlStr);
            Elements elements = document.select("a.nbg");
            List<CompletableFuture<MovieInfo>> list = new ArrayList<>();
            for (Element element : elements) {
                String href = element.attr("href");
                Map<String, String> parseQuery = DoubanUrlUtils.parseQuery(URI.create(href).getQuery());
                String singleUrl = parseQuery.get("url");
                if (DoubanUrlUtils.isBookUrl(singleUrl) && list.size() < doubanApiConfigProperties.count()){
                    list.add(CompletableFuture.supplyAsync(() -> {
                        try {
                            Connection.Response singleResponse = Jsoup.connect(singleUrl)
                                    .referrer(doubanApiConfigProperties.baseUrl())
                                    .userAgent(doubanApiConfigProperties.userAgent())
                                    .ignoreContentType(true)
                                    .execute();
                            return htmlParseProvider.parse(singleUrl, singleResponse.parse());
                        } catch (IOException ex) {
                            log.error("load movie info error url:{}", singleUrl, ex);
                            return null;
                        }
                    }));
                }
            }
            return list.stream().map(CompletableFuture::join).toList();
        } catch (IOException e) {
            log.error("search movie info error url:{}", url, e);
            return Collections.emptyList();
        }
    }
}
