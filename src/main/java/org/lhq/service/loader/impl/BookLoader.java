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
import org.lhq.entity.book.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.utils.DoubanUrlUtils;
import org.lhq.service.utils.thread.ThreadPoolType;
import org.lhq.service.utils.thread.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Singleton
@Named("bookLoader")
public class BookLoader extends EntityLoader<BookInfo> implements SearchLoader<BookInfo> {


    private static final Logger log = LoggerFactory.getLogger(BookLoader.class);

    protected BookLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public BookInfo load(HtmlParseProvider<BookInfo> htmlParseProvider, String id) {
        String url = processUrl(new TypeToken<>() {
        }, id);
        log.info("load book info from url:{}", url);
        if (cacheService.containsKeyAndNotExpired(url)) {
            return cacheService.get(url);
        }
        String cookie = doubanApiConfigProperties.cookie();
        Map<String, String> cookies = DoubanUrlUtils.getCookies(cookie);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .execute();
            Document htmlDoc = response.parse();
            BookInfo bookInfo = htmlParseProvider.parse(url, htmlDoc);
            cacheService.put(url, bookInfo, 10, TimeUnit.MINUTES);
            return bookInfo;
        } catch (IOException e) {
            log.error("load book info error url:{}", url, e);
            return new BookInfo();
        }
    }

    @Override
    public List<BookInfo> search(HtmlParseProvider<BookInfo> htmlParseProvider, String keyword) {
        Map<String, String> searchMap = doubanApiConfigProperties.mappings();
        String bookCat = searchMap.get("book");
        String url = doubanApiConfigProperties.searchUrl() + "?cat=" + bookCat + "&q=" + keyword;
        log.info("search book info from url:{}", url);
        List<Future<BookInfo>> list = new ArrayList<>();
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .ignoreContentType(true)
                    .execute();
            String htmlStr = response.body();
            Document document = Jsoup.parse(htmlStr);
            Elements elements = document.select("a.nbg");
            for (Element element : elements) {
                String href = element.attr("href");
                Map<String, String> map = DoubanUrlUtils.parseQuery(URI.create(href).getQuery());
                String singleUrl = map.get("url");
                String cookie = doubanApiConfigProperties.cookie();
                Map<String, String> cookies = DoubanUrlUtils.getCookies(cookie);
                if (DoubanUrlUtils.isBookUrl(singleUrl) && list.size() < doubanApiConfigProperties.count()) {
                    log.info("search book info from url:{}", singleUrl);
                    Future<BookInfo> bookInfoFutureTask = ThreadPoolUtil.submit(ThreadPoolType.NETWORK_REQUEST_THREAD, () -> {
                        Document htmlDocument = Jsoup.connect(singleUrl)
                                .referrer(doubanApiConfigProperties.baseUrl())
                                .userAgent(doubanApiConfigProperties.userAgent())
                                .cookies(cookies)
                                .ignoreContentType(true)
                                .get();
                        return htmlParseProvider.parse(singleUrl, htmlDocument);
                    });
                    list.add(bookInfoFutureTask);
                }
            }
        } catch (IOException e) {
            log.error("search book info error url:{}", url, e);
        }
        List<BookInfo> resultList = list.stream().map(item -> {
            try {
                return item.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("search book info error", e);
                return new BookInfo();
            }
        }).toList();
        log.info("book info size:{}", resultList.size());
        return resultList;
    }
}
