package org.lhq.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "dir")
public interface DirConfigProperties {
    String autoScanDir();
    Integer autoScanInterval();
    String bookDir();
    String movieDir();
    String[] ebookExtensions();
}
