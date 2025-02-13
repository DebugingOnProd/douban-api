package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.CelebrityInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.utils.DoubanUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public class CelebrityLoader extends EntityLoader<List<CelebrityInfo>> {
    private static final Logger log = LoggerFactory.getLogger(CelebrityLoader.class);

    protected CelebrityLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public List<CelebrityInfo> load(HtmlParseProvider<List<CelebrityInfo>> htmlParseProvider, String id) {
        TypeToken<List<CelebrityInfo>> typeToken = new TypeToken<>() {};
        String url = processUrl(typeToken,id);
        log.info("load celebrity info from url:{}", url);
        String cookie = doubanApiConfigProperties.cookie().orElse("");
        Map<String, String> cookies = DoubanUrlUtils.getCookies(cookie);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .execute();
            return htmlParseProvider.parse(url, response.parse());
        } catch (IOException e) {
            log.error("load celebrity info error",e);
        }
        return Collections.emptyList();
    }
}
