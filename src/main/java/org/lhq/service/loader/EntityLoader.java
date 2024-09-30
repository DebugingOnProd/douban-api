package org.lhq.service.loader;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Inject;
import org.lhq.cache.CacheEnum;
import org.lhq.cache.CacheService;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.entity.CelebrityInfo;
import org.lhq.entity.MovieInfo;
import org.lhq.service.perse.HtmlParseProvider;

import java.util.List;
import java.util.Map;

public abstract class EntityLoader<T> {

    protected final CacheService<String, BookInfo> cacheService;

    protected final DoubanApiConfigProperties doubanApiConfigProperties;

    protected final Map<TypeToken<?>, String> urlMap;


    @Inject
    protected EntityLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        this.doubanApiConfigProperties = doubanApiConfigProperties;
        this.cacheService = CacheEnum.INSTANCE.getBookCacheService();

        this.urlMap = Map.of(
                new TypeToken<BookInfo> () {} ,doubanApiConfigProperties.detailUrl(),
                new TypeToken<MovieInfo> () {} ,doubanApiConfigProperties.movieDetailUrl(),
                new TypeToken<List<CelebrityInfo>> () {},doubanApiConfigProperties.movieCharacterUrl()
        );
    }


    public String processUrl(TypeToken<T> type, String id) {
        String urlTemplate = urlMap.get(type);
        urlTemplate = urlTemplate.replace("{id}",id);
        return urlTemplate;
    }
    public abstract T load(HtmlParseProvider<T> htmlParseProvider, String id);


}
