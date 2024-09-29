package org.lhq.service.image.impl;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.common.jaxrs.UriBuilderImpl;
import org.lhq.config.DoubanApiConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.entity.HostInfo;
import org.lhq.service.image.ImageProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

@Singleton
public class ImageProxyImpl implements ImageProxy<BookInfo> {

    private static final Logger log = LoggerFactory.getLogger(ImageProxyImpl.class);
    private final DoubanApiConfigProperties doubanApiConfigProperties;

    public ImageProxyImpl(DoubanApiConfigProperties doubanApiConfigProperties) {
        this.doubanApiConfigProperties = doubanApiConfigProperties;
    }

    @Override
    public BookInfo process(BookInfo item, HostInfo hostInfo) {
        log.info("process image proxy");
        try {
            if (item != null &&
                    StringUtils.isNotBlank(item.getImage()) &&
                    doubanApiConfigProperties.proxyImageUrl()) {
                UriBuilder coverBuilder = new UriBuilderImpl().encode(false)
                        .scheme(hostInfo.getScheme())
                        .host(hostInfo.getHost())
                        .port(hostInfo.getPort())
                        .path("imageProxy/view/cover");
                String coverUrlString = coverBuilder.build().toString();
                if (!item.getImage().contains(coverUrlString)) {
                    String image = item.getImage();
                    String coverUrl = coverBuilder.queryParam("cover",image).build().toURL().toString();
                    item.setImage(coverUrl);
                }
            }
            return item;
        } catch (MalformedURLException e) {
            log.error("MalformedURLException",e);
            return item;
        }
    }
}
