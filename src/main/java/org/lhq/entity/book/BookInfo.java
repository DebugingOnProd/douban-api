package org.lhq.entity.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.lhq.entity.book.calibre.Creator;
import org.lhq.entity.book.calibre.Guide;
import org.lhq.entity.book.calibre.Identifier;
import org.lhq.entity.book.calibre.Meta;
import org.lhq.entity.book.calibre.Metadata;
import org.lhq.entity.book.calibre.Package;
import org.lhq.entity.book.calibre.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        BookVo bookVo = new BookVo();
        bookVo.setId(this.id);
        bookVo.setTitle(this.title);
        bookVo.setAuthors(this.author);
        String trimDate = this.publishDate.trim();
        bookVo.setPublishDate(trimDate);
        String average = this.rating.get("average");
        if (average.isBlank()) {
            log.warn("average is null");
            average = "0";
        }
        float ratingFloat = Float.parseFloat(average);
        bookVo.setRating(ratingFloat);
        bookVo.setSummary(this.summary);
        bookVo.setPublisher(this.publisher);
        return bookVo;
    }



    public Package toPackage() {
        Package result = new Package();
        result.setXmlns("http://www.idpf.org/2007/opf");
        result.setUniqueIdentifier("uuid_id");
        result.setVersion("2.0");

        Metadata metadata = new Metadata();
        metadata.setTitle(this.title);
        metadata.setPublisher(this.publisher);
        metadata.setLanguage("zh-CN");
        metadata.setDescription(this.summary);
        metadata.setXmlnsOpf("http://www.idpf.org/2007/opf");
        result.setMetadata(metadata);

        List<Identifier> identifiers = new ArrayList<>();
        Identifier calibre = new Identifier();
        calibre.setId("calibre_id");
        calibre.setScheme("calibre");
        calibre.setValue(this.id);
        identifiers.add(calibre);


        Identifier isbn = new Identifier();
        isbn.setScheme("ISBN");
        isbn.setValue(this.isbn13);
        identifiers.add(isbn);
        metadata.setIdentifiers(identifiers);

        Creator creator = new Creator();
        creator.setValue(this.author.getFirst());
        creator.setFileAs(this.author.getFirst());
        creator.setRole("aut");
        metadata.setCreator(creator);
        List<String> tagList = this.tags.stream().map(m -> m.get("name")).toList();
        metadata.setSubjects(tagList);

        Reference reference = new Reference();
        reference.setHref("cover.jpg");
        reference.setTitle("封面");
        reference.setType("cover");
        List<Reference> guideList = List.of(reference);

        Guide guide = new Guide();
        guide.setReferences(guideList);
        result.setGuide(guide);

        Meta meta = new Meta();
        meta.setName("calibre:rating");
        meta.setContent(String.valueOf(this.rating.get("average")));
        metadata.setMetas(List.of(meta));

        return result;
    }
}
