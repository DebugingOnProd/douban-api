package org.lhq.service.perse.impl;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Named("bookParser")
public class HtmlToBookImpl implements HtmlParseProvider<BookInfo> {
    private static final Logger log = LoggerFactory.getLogger(HtmlToBookImpl.class);
    public static final Pattern ID_PATTERN = Pattern.compile(".*/subject/(\\d+)/?");
    private static final Pattern SERIES_PATTERN = Pattern.compile(".*/series/(\\d+)/?");
    private static final Pattern TAGS_PATTERN = Pattern.compile("criteria = '(.+)'");
    @Override
    public BookInfo parse(String url, Document doc) {
        log.info("parse book info from url:{}", url);
        BookInfo bookInfo = new BookInfo();
        Elements body = doc.select("body");
        String html = doc.body().html();
        Element content = body.isEmpty() ? null : body.getFirst();
        // 提取书名
        Element titleElement = body.select("[property='v:itemreviewed']").getFirst();
        if (titleElement != null){
            String title = titleElement.text();
            log.info("bookTitle: {}", title);
            bookInfo.setTitle(title);
        }
        // 提取评分
        Element rateElement = body.select("[property='v:average']").getFirst();
        if (rateElement != null){
            String rate = rateElement.text();
            log.info("bookRate: {}", rate);
            Map<String, String> ratingMap = new HashMap<>();
            ratingMap.put("average", rateElement.text().trim());
            bookInfo.setRating(ratingMap);
        }
        // 提取简介
        Element summaryElement = content.selectFirst("#link-report :not(.short) .intro");
        String summary = "";
        if (summaryElement != null){
            summary =  StringUtils.trimToEmpty(summaryElement.html());
        }
        bookInfo.setSummary(summary);
        // 提取tag
        Matcher tagMatcher = TAGS_PATTERN.matcher(html);
        if (tagMatcher.find()) {
            List<Map<String, String>> tagsMap = Arrays.stream(tagMatcher.group(1).split("[|]"))
                    .filter(tag-> tag.startsWith("7:"))
                    .distinct()
                    .map(tag -> {
                        tag = tag.replace("7:", "");
                        Map<String, String> tagMap = new HashMap<>();
                        tagMap.put("name", tag);
                        tagMap.put("title", tag);
                        return tagMap;
                    }).toList();
            bookInfo.setTags(tagsMap);
        }

        // 提取封面
        Element imageElement = content.selectFirst("a.nbg");
        if (imageElement != null){
            String image = imageElement.attr("href");
            log.info("bookImage: {}", image);
            bookInfo.setImage(image);
        }
        // 提取id
        Matcher matcher = ID_PATTERN.matcher(url);
        if (matcher.matches()) {
            bookInfo.setId(matcher.group(1));
        }
        // 提取分享链接
        Element shareElement = content.selectFirst("a.bn-sharing");
        if (shareElement != null) {
            url = shareElement.attr("data-url");
            log.info("bookUrl: {}", url);
            bookInfo.setUrl(url);
        }
        Elements bookInfoElements = content.select("span.pl");

        for (Element element : bookInfoElements) {
            String text = element.text();
            if (text.contains("作者")){
                Elements authorElementList = element.nextElementSiblings();
                List<String> authorList = new ArrayList<>();
                for (Element authorElement : authorElementList) {
                    String author = authorElement.text();
                    log.info("bookAuthor: {}", author);
                    authorList.add(author);
                }
                bookInfo.setAuthor(authorList);
            }else if (text.contains("出版社")){
                String publisher = element.nextElementSibling().text();
                log.info("bookPublisher: {}",publisher);
                bookInfo.setPublisher(publisher);
            }else if (text.contains("出品方")) {
                String producer = element.nextElementSibling().text();
                log.info("bookProducer: {}",producer);
                bookInfo.setProducer(producer);
            }else if (text.contains("原作名")){
                String originTitle = element.nextSibling().toString();
                log.info("bookOriginTitle: {}",originTitle);
                bookInfo.setOriginTitle(originTitle);
            }else if (text.contains("译者")) {
                Elements translatorElement = element.nextElementSiblings();
                List<String> translatorList = new ArrayList<>();
                for (Element translator : translatorElement) {
                    String translatorText = translator.text();
                    log.info("bookTranslator: {}", translatorText);
                    translatorList.add(translatorText);
                }
                bookInfo.setTranslator(translatorList);
            }else if (text.contains("出版年")) {
                Node node = element.nextSibling();
                String publishDate = node.toString();
                log.info("bookPublishDate: {}", publishDate);
                bookInfo.setPublishDate(publishDate);
            }else if (text.contains("ISBN")) {
                String isbn = element.nextSibling().toString();
                log.info("bookIsbn: {}", isbn);
                bookInfo.setIsbn13(isbn);
            }else if (text.contains("页数")){
                String pages = element.nextSibling().toString();
                log.info("bookPages: {}", pages);
                bookInfo.setPages(pages);
            }else if (text.contains("装帧")){
                String binding = element.nextSibling().toString();
                log.info("bookBinding: {}", binding);
                bookInfo.setBinding(binding);
            }else if (text.contains("定价")){
                String price = element.nextSibling().toString();
                log.info("bookPrice: {}", price);
                bookInfo.setPrice(price);
            }else if (text.contains("丛书")) {
                Element seriesElement = element.nextElementSibling();
                if (seriesElement != null) {
                    String seriesHref = seriesElement.attr("href");
                    Matcher seriesMatcher = SERIES_PATTERN.matcher(seriesHref);
                    Map<String, String> series = new HashMap<>();
                    if (seriesMatcher.matches()) {
                        series.put("id", seriesMatcher.group(1));
                    }
                    series.put("title", seriesElement.text());
                    bookInfo.setSeries(series);
                }
            }
        }
        return bookInfo;
    }
}
