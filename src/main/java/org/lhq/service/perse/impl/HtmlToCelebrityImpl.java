package org.lhq.service.perse.impl;

import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lhq.entity.CelebrityInfo;
import org.lhq.entity.enums.MovieRole;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class HtmlToCelebrityImpl implements HtmlParseProvider<List<CelebrityInfo>> {
    private static final Logger log = LoggerFactory.getLogger(HtmlToCelebrityImpl.class);

    /**
     * 解析html
     *
     * @param url
     * @param html
     * @return T
     */
    @Override
    public List<CelebrityInfo> parse(String url, String html) {
        Document document = Jsoup.parse(html);

        Elements actorList = document.select(".celebrities-list .celebrity");
        List<CelebrityInfo> celebritiesVoList = new ArrayList<>();
        for (Element actor : actorList) {
            CelebrityInfo celebritiesVo = new CelebrityInfo();
            // 提取演员姓名
            Element nameElement = actor.selectFirst(".name a");
            String name = nameElement.text();
            String nameUrl = nameElement.attr("href");
            celebritiesVo.setName(name);
            celebritiesVo.setLink(nameUrl);

            // 提取角色信息
            Element roleElement = actor.selectFirst(".role");
            String role = roleElement.text();
            if (role.contains("导演")){
                celebritiesVo.setRole(MovieRole.DIRECTOR);
            }else if (role.contains("演员")) {
                celebritiesVo.setRole(MovieRole.ACTOR);
                String characterName = extractRole(role);
                celebritiesVo.setCharacterName(characterName);
            }else if (role.contains("配音")) {
                celebritiesVo.setRole(MovieRole.VOICE);
                String characterName = extractRole(role);
                celebritiesVo.setCharacterName(characterName);
            }else {
                celebritiesVo.setRole(MovieRole.OTHER);
            }
            // 提取代表作品
            Elements worksElements = actor.select(".works a");
            StringBuilder works = new StringBuilder();
            for (Element work : worksElements) {
                if (!works.isEmpty()) {
                    works.append(", ");
                }
                works.append(work.text());
            }
            celebritiesVo.setWorks(works.toString());

            Element element = actor.selectFirst(".avatar");

            String style = element.attr("style");
            if (!style.isEmpty()) {
                String imageUrl = parseImageUrlFromStyle(style);
                celebritiesVo.setAvatar(imageUrl);
            }

            log.info("Name: {}", name);
            log.info("URL: {}", nameUrl);
            log.info("Role: {}", role);
            log.info("Works: {}", works);
            log.info("------------------------------");
            celebritiesVoList.add(celebritiesVo);
        }
        celebritiesVoList.forEach(item -> log.info("{}", item));
        return celebritiesVoList;
    }

    private static String extractRole(String roleText) {
        log.info("roleText: {}", roleText);
        if (roleText.contains("饰 ")) {
            // 去掉前缀 "演员 Actor (饰 "
            int start = roleText.indexOf("饰 ") + 2;
            // 去掉后缀 ")"
            int end = roleText.lastIndexOf(")");
            return roleText.substring(start, end);
        }else if (roleText.contains("配 ")) {
            // 去掉前缀 "配音 Voice (配 "
            int start = roleText.indexOf("配 ") + 2;
            // 去掉后缀 ")"
            int end = roleText.lastIndexOf(")");
            return roleText.substring(start, end);
        } else {
            return "";
        }

    }

    private static String parseImageUrlFromStyle(String style) {
        Pattern pattern = Pattern.compile("url\\((.*?)\\)");
        Matcher matcher = pattern.matcher(style);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
