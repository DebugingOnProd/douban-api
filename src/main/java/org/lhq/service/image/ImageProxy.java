package org.lhq.service.image;

import org.lhq.entity.HostInfo;

@FunctionalInterface
public interface ImageProxy<T> {
    T process(T item, HostInfo hostInfo);
}
