package org.lhq.config;

import io.smallrye.config.ConfigMapping;

import java.util.Map;
import java.util.Optional;

@ConfigMapping(prefix = "douban.api")
public interface DoubanApiConfigProperties {
    Map<String,String> mappings();
    String baseUrl();
    String searchUrl();
    String detailUrl();
    String isbnUrl();
    String movieDetailUrl();
    String movieCharacterUrl();
    String personDetailUrl();
    String userAgent();
    boolean proxyImageUrl();
    int count();
    Optional<String> cookie();
}
