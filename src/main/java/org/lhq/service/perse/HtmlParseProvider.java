package org.lhq.service.perse;

@FunctionalInterface
public interface HtmlParseProvider<T> {

    T parse(String url,String html);
}
