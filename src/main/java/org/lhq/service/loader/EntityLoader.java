package org.lhq.service.loader;

import jakarta.inject.Inject;
import org.lhq.cache.CacheEnum;
import org.lhq.cache.CacheService;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.entity.MovieInfo;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.Map;

public abstract class EntityLoader<T>  {

    protected final CacheService<String, BookInfo> cacheService;

    protected final DoubanApiConfigProperties doubanApiConfigProperties;

    protected final Map<Class<?>, String> urlMap;


    @Inject
    protected EntityLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        this.doubanApiConfigProperties = doubanApiConfigProperties;
        this.cacheService = CacheEnum.INSTANCE.getBookCacheService();
        this.urlMap = Map.of(
                BookInfo.class ,doubanApiConfigProperties.detailUrl(),
                MovieInfo.class ,doubanApiConfigProperties.movieDetailUrl()
        );
    }


    public String processUrl(Class<T> type,String id) {
        String urlTemplate = urlMap.get(type);
        urlTemplate = urlTemplate.replace("{id}",id);
        return urlTemplate;
    }
    public abstract T load(HtmlParseProvider<T> htmlParseProvider, String id);
}
