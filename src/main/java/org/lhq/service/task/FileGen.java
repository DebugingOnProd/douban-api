package org.lhq.service.task;


import java.io.File;

@FunctionalInterface
public interface FileGen<T> {
    void genFile(T t, File taskFile);
}
