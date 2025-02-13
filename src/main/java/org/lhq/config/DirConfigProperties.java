package org.lhq.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "dir")
public interface DirConfigProperties {
    String autoScanDir();
    Integer autoScanInterval();
    String bookDir();
    String movieDir();
    List<String> ebookExtensions();
    boolean autoScanEnabled();
}
