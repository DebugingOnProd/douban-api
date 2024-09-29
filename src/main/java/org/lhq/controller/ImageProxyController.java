package org.lhq.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.lhq.service.loader.EntityLoader;

import java.util.Collections;
import java.util.List;

@Path("/imageProxy")
public class ImageProxyController {


    private final EntityLoader<List<Byte>> imageLoader;

    public ImageProxyController(EntityLoader<List<Byte>> imageLoader) {
        this.imageLoader = imageLoader;
    }

    @GET
    @Path("/view/cover")
    @Produces({"image/jpeg"})
    public byte[] getImage(@QueryParam("cover") String cover) {
        List<Byte> imageByteList = imageLoader.load((url, html)-> Collections.emptyList(),cover);
        byte[] byteArray = new byte[imageByteList.size()];
        for (int i = 0; i < imageByteList.size(); i++) {
            byteArray[i] = imageByteList.get(i);
        }
        return byteArray;
    }
}
