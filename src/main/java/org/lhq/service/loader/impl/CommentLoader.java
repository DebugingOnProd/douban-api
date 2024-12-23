package org.lhq.service.loader.impl;

import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.perse.HtmlParseProvider;

public class CommentLoader  extends EntityLoader<String> {
    protected CommentLoader(DoubanApiConfigProperties doubanApiConfigProperties) {
        super(doubanApiConfigProperties);
    }

    @Override
    public String load(HtmlParseProvider<String> htmlParseProvider, String id) {
        return "";
    }
}
