package org.lhq.service.loader;

import org.lhq.service.perse.HtmlParseProvider;

import java.util.Collections;
import java.util.List;

public interface SearchLoader<T> {


    default List<T> search(HtmlParseProvider<T> htmlParseProvider, String keyword) {
        return Collections.emptyList();
    }
}
