package org.lhq.entity.book;

import lombok.Data;

import java.util.List;

@Data
public class BookVo {
    private String id;
    private String title;
    private List<String> authors;
    private String publishDate;
    private Float rating;
}
