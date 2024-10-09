package org.lhq.service.gen;


import java.io.File;

@FunctionalInterface
public interface Gen<T> {
    void genFile(T dataItem, File taskFile);
}
