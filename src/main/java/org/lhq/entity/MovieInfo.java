package org.lhq.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MovieInfo {
    private String id;
    private String title;
    private String originTitle;
    //导演
    private String director;
    //主演
    private List<String> cast;
    //影片类型
    private List<String> genre;
    //简介
    private String summary;
    //上映时间
    private String publishDate;
    //片长
    private String duration;
    //影片语言
    private List<String> language;
    private String imdb;
    //评分
    private Map<String, String> rating;
    //又名
    private List<String> alsoKnownAs;
}
