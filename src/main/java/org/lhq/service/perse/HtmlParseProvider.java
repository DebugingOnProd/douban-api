package org.lhq.service.perse;

@FunctionalInterface
public interface HtmlParseProvider<T> {
    /**
     * 解析html
     * @param url
     * @param html
     * @return T
     */
    T parse(String url,String html);
}
