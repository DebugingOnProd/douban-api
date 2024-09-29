package org.lhq.service.loader;

import java.util.Collections;
import java.util.List;

public interface SearchLoader<T> {


    default List<T> search(String keyword) {
        return Collections.emptyList();
    }
}
