package org.lhq.service.perse;

import org.jsoup.nodes.Document;

@FunctionalInterface
public interface HtmlParseProvider<T> {
    /**
     * 解析html
     * @param url
     * @param html
     * @return T
     */
    T parse(String url, Document html);
}
