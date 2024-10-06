package org.lhq.service.loader.impl;

import com.google.common.reflect.TypeToken;
import jakarta.inject.Singleton;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.PersonageInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class PersonLoader extends EntityLoader<PersonageInfo> {
    private static final Logger log = LoggerFactory.getLogger(PersonLoader.class);

    protected PersonLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public PersonageInfo load(HtmlParseProvider<PersonageInfo> htmlParseProvider, String id) {
        TypeToken<PersonageInfo> typeToken = new TypeToken<>() {};
        String url = processUrl(typeToken, id);
        try {
            Connection.Response execute = Jsoup.connect(url)
                    .referrer(doubanApiConfigProperties.baseUrl())
                    .userAgent(doubanApiConfigProperties.userAgent())
                    .execute();
            Document document = execute.parse();
            return htmlParseProvider.parse(url, document);
        } catch (IOException e) {
            log.error("load person info error url:{}", url, e);
            return null;
        }
    }
}
