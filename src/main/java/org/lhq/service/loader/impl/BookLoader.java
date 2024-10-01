package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.utils.DoubanUrlUtils;
import org.lhq.service.utils.ThreadPoolType;
import org.lhq.service.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
@Named("bookLoader")
public class BookLoader extends EntityLoader<BookInfo> implements SearchLoader<BookInfo> {


    private static final Logger log = LoggerFactory.getLogger(BookLoader.class);

    protected BookLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public BookInfo load(HtmlParseProvider<BookInfo> htmlParseProvider, String id) {
        String url = processUrl(new TypeToken<>() {},id);
        log.info("load book info from url:{}", url);
        if (cacheService.containsKeyAndNotExpired(url)) {
            return cacheService.get(url);
        }
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .ignoreContentType(true)
                    .execute();
            Document htmlDoc = response.parse();
            BookInfo bookInfo = htmlParseProvider.parse(url, htmlDoc);
            cacheService.put(url, bookInfo, 10, TimeUnit.MINUTES);
            return bookInfo;
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return null;
        }
    }

    @Override
    public List<BookInfo> search(HtmlParseProvider<BookInfo> htmlParseProvider,String keyword) {
        Map<String, String> searchMap = doubanApiConfigProperties.mappings();
        String bookCat = searchMap.get("book");
        String url = doubanApiConfigProperties.searchUrl() + "?cat=" + bookCat + "&q=" + keyword;
        log.info("search book info from url:{}", url);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .ignoreContentType(true)
                    .execute();
            String htmlStr = response.body();
            Document document = Jsoup.parse(htmlStr);
            Elements elements = document.select("a.nbg");
            List<CompletableFuture<BookInfo>> list = new ArrayList<>();
            ThreadPoolExecutor executor = ThreadPoolUtil.getExecutor(ThreadPoolType.FIXED_THREAD);
            for (Element element : elements) {
                String href = element.attr("href");
                Map<String, String> map = DoubanUrlUtils.parseQuery(URI.create(href).getQuery());
                String singleUrl = map.get("url");
                if (DoubanUrlUtils.isBookUrl(singleUrl) && list.size() < doubanApiConfigProperties.count()) {
                    log.info("search book info from url:{}", singleUrl);
                    list.add(CompletableFuture.supplyAsync(() -> {
                        try {
                            Document htmlDocument = Jsoup.connect(singleUrl)
                                    .referrer(doubanApiConfigProperties.baseUrl())
                                    .userAgent(doubanApiConfigProperties.userAgent())
                                    .ignoreContentType(true)
                                    .get();
                            return htmlParseProvider.parse(singleUrl, htmlDocument);
                        } catch (HttpStatusException ex) {
                            log.warn("http status error :{}", ex.getMessage());
                            return null;
                        } catch (IOException ex) {
                            log.error("load book info error url:{}", singleUrl, ex);
                            return null;
                        }
                    }, executor));
                }
            }
            return list.stream().map(CompletableFuture::join).toList();
        } catch (IOException e) {
            log.error("search book info error url:{}", url, e);
            return Collections.emptyList();
        }
    }
}
