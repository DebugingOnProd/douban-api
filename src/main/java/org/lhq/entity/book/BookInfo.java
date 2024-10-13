package org.lhq.entity.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
public class BookInfo {
    private static final Logger log = LoggerFactory.getLogger(BookInfo.class);
    private String id;
    private String title;
    @JsonProperty("origin_title")
    private String originTitle;
    private List<String> author;
    private List<String> translator;
    private String summary;
    private String publisher;
    //出品方
    private String producer;
    @JsonProperty("pubdate")
    private String publishDate = "1900-01";
    private List<Map<String, String>> tags;
    private Map<String, String> rating;
    private Map<String, String> series;
    private String image;
    private String url;
    private String isbn13;
    private String isbn10;
    private String pages;
    private String binding;
    private String price;
    @JsonProperty("author_intro")
    private String authorIntro;
    private String catalog;
    @JsonProperty("ebook_url")
    private String ebookUrl;
    @JsonProperty("ebook_price")
    private String ebookPrice;


    public BookVo toBookVo() {
        log.info("BookInfo: {}", this);
        BookVo bookVo = new BookVo();
        bookVo.setId(this.id);
        bookVo.setTitle(this.title);
        bookVo.setAuthors(this.author);
        String trimDate = this.publishDate.trim();
        LocalDate localDate = LocalDate.parse(trimDate, DateTimeFormatter.ofPattern("yyyy-M-d"));
        bookVo.setPublishDate(localDate);
        String average = this.rating.get("average");
        float ratingFloat = Float.parseFloat(average);
        bookVo.setRating(ratingFloat);
        log.info("BookVo: {}", bookVo);
        return bookVo;
    }
}
