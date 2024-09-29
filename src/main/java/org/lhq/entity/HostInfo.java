package org.lhq.entity;

import lombok.Data;

@Data
public class HostInfo {
    private String host;
    private int port;
    private String scheme;
    private String path;
}
