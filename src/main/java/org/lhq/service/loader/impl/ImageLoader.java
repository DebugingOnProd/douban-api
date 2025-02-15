package org.lhq.service.loader.impl;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.utils.DoubanUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
@Named("imageLoader")
public class ImageLoader extends EntityLoader<List<Byte>> {
    private static final Logger log = LoggerFactory.getLogger(ImageLoader.class);

    protected ImageLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public List<Byte> load(HtmlParseProvider<List<Byte>> htmlParseProvider, String imageUrl) {
        String baseUrl = doubanApiConfigProperties.baseUrl();
        String userAgent = doubanApiConfigProperties.userAgent();
        String cookie = doubanApiConfigProperties.cookie().orElse("");
        Map<String, String> cookiesMap = DoubanUrlUtils.getCookies(cookie);
        try {
            Connection.Response response = Jsoup.connect(imageUrl)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .referrer(baseUrl)
                    .cookies(cookiesMap)
                    .userAgent(userAgent)
                    .execute();
            if (response.statusCode() == 200) {
                log.info("load image success");
                byte[] bytes = response.bodyAsBytes();
                List<Byte> byteList = new ArrayList<>();
                for (byte aByte : bytes) {
                    byteList.add(aByte);
                }
                return byteList;
            }
            return Collections.emptyList();
        }catch (Exception e){
            log.error("load image error",e);
            return Collections.emptyList();
        }
    }
}
