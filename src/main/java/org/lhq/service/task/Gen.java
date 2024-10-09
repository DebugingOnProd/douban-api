package org.lhq.service.task;


import java.io.File;

@FunctionalInterface
public interface Gen<T> {
    void genFile(T dataItem, File taskFile);
}
