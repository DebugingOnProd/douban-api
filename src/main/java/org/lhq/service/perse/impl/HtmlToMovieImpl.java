package org.lhq.service.perse.impl;

import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.lhq.entity.MovieInfo;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class HtmlToMovieImpl implements HtmlParseProvider<MovieInfo> {

    private static final Logger log = LoggerFactory.getLogger(HtmlToMovieImpl.class);

    /**
     * 解析html
     *
     * @param url
     * @param html
     * @return MovieInfo
     */
    @Override
    public MovieInfo parse(String url, String html) {
        log.info("url:{}", url);
        Document document = Jsoup.parse(html);
        Elements htmlbody = document.select("body");
        Element content = htmlbody.isEmpty() ? null : htmlbody.getFirst();
        String title = document.select("[property='v:itemreviewed']").text(); // title获取方式修改
        log.info("title:{}", title);
        MovieInfo movieVo = new MovieInfo();
        movieVo.setTitle(title);
        // 提取评分
        Element rating = document.selectFirst("strong.ll.rating_num");
        String score = rating != null ? rating.text() : "N/A";
        log.info("评分: {}" , score);
        movieVo.setRating(Map.of("douban", score));

        // 提取导演
        Elements directors = document.select("a[rel='v:directedBy']");
        StringBuilder directorsStr = new StringBuilder();
        for (Element director : directors) {
            directorsStr.append(director.text()).append(", ");
        }
        String directorsText = directorsStr.toString().trim();
        if (directorsText.endsWith(",")) {
            directorsText = directorsText.substring(0, directorsText.length() - 1);
        }
        log.info("导演: {}" , directorsText);
        movieVo.setDirector(directorsText);
        // 提取主演
        Elements actors = document.select("a[rel='v:starring']");
        StringBuilder actorsStr = new StringBuilder();
        ArrayList<String> actorList = new ArrayList<>();
        for (Element actor : actors) {
            actorsStr.append(actor.text()).append(", ");
            actorList.add(actor.text());
        }
        String actorsText = actorsStr.toString().trim();
        if (actorsText.endsWith(",")) {
            actorsText = actorsText.substring(0, actorsText.length() - 2);
        }
        log.info("主演: {}" , actorsText);
        movieVo.setCast(actorList);
        // 提取简介
        Element summary = document.selectFirst("span[property='v:summary']");
        String synopsis = summary != null ? summary.text() : "N/A";
        log.info("简介: {} " , synopsis);
        movieVo.setSummary(synopsis);


        // 选取包含影片类型的元素
        Elements genreElements = document.select("span[property='v:genre']");
        StringBuilder genreStr = new StringBuilder();
        List<String> genreList = new ArrayList<>();
        if (!genreElements.isEmpty()) {
            for (Element element : genreElements) {
                genreStr.append(element.text()).append(", ");
                genreList.add(element.text());
            }

        }
        movieVo.setGenre(genreList);
        log.info("影片类型: {} " , genreStr);

        // 提取上映日期
        Elements releaseDate = content.select("span[property='v:initialReleaseDate']");
        if (releaseDate.isEmpty()) {
            log.info("未找到上映日期信息。");
        }
        StringBuilder releaseDateStr = new StringBuilder();
        for (Element element : releaseDate) {
            releaseDateStr.append(element.text()).append(", ");
        }
        log.info("上映日期: {}" , releaseDateStr);
        movieVo.setPublishDate(releaseDateStr.toString());



        // 查找包含语言信息的标签
        Element languageElement = content.selectFirst("span.pl:contains(语言:)");

        if (languageElement != null && languageElement.nextElementSibling() != null) {
            Node node = languageElement.nextSibling();
            // 提取并打印语言信息
            log.info("影片语言: {}",node.toString());
            String languageList = node.toString();
            if (!languageList.isEmpty()){
                String[] languageArrays = languageList.split(" / ");
                movieVo.setLanguage(List.of(languageArrays));
            }
        } else {
            log.info("未找到影片语言信息。");
        }

        // 提取片长
        Element durationElement = content.selectFirst("span:contains(片长:) + span");
        if (durationElement != null) {
            String durationText = durationElement.ownText(); // 获取文本内容，例如："173分钟"
            log.info("片长: {}" , durationText);
            movieVo.setDuration(durationText);
        } else {
            log.info("未找到片长信息。");
        }


        //获取imdb信息
        Element imdbElement = content.selectFirst("span.pl:contains(IMDb:)");
        if (imdbElement != null){
            Node node = imdbElement.nextSibling();
            log.info("imdb: {}",node.toString());
            movieVo.setImdb(node.toString());
        }else {
            log.info("未找到imdb信息。");
        }

        Element akaElement = content.selectFirst("span.pl:contains(又名:)");
        if (akaElement != null){
            Node node = akaElement.nextSibling();
            log.info("又名: {}",node.toString());
            String akaStr = node.toString();
            List<String> akaList = Arrays.stream(akaStr.split(" / ")).toList();
            movieVo.setAlsoKnownAs(akaList);
        }else {
            log.info("未找到又名信息。");
        }
        return movieVo;
    }
}
