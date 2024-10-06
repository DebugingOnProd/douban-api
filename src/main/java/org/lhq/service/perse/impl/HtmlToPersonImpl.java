package org.lhq.service.perse.impl;

import jakarta.inject.Singleton;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lhq.entity.PersonageInfo;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Singleton
public class HtmlToPersonImpl implements HtmlParseProvider<PersonageInfo> {

    private static final Logger log = LoggerFactory.getLogger(HtmlToPersonImpl.class);



    /**
     * 解析html
     *
     * @param url
     * @param html
     * @return T
     */
    @Override
    public PersonageInfo parse(String url, Document html) {
        // 2. 解析基本信息
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");

        // 3. 提取具体信息
        Element body = html.body();
        // 2. 解析ID为"content"的div元素
        Element contentDiv = body.selectFirst("#content");
        Element element = contentDiv.selectFirst("h1.subject-name");
        PersonageInfo personageInfo = new PersonageInfo();
        if (element != null){
            String text = element.text();
            log.info("text: {}", text);
            personageInfo.setName(text);
        }
        Element avatarElement = contentDiv.selectFirst("img.avatar");
        if (avatarElement != null){
            String src = avatarElement.attr("src");
            log.info("src: {}", src);
            personageInfo.setAvatar(src);
        }

        // 2. 提取各个属性值
        Elements listItems = contentDiv.select("ul.subject-property li");
        for (Element listItem : listItems) {
            Element labelElement = listItem.selectFirst("span.label");
            Element valueElement = listItem.selectFirst("span.value");

            if (labelElement != null && valueElement != null) {
                String label = labelElement.text().trim();
                String value = valueElement.text().trim();
                log.info("{}: {}", label, value);
                switch (label) {
                    case "性别:":
                        personageInfo.setGender(value);
                        break;
                    case "出生日期:":
                        LocalDate date = LocalDate.parse(value, formatter);
                        personageInfo.setBirthday(date);
                        break;
                    case "出生地:":
                        personageInfo.setBirthPlace(value);
                        break;
                    case "IMDb编号:":
                        personageInfo.setImdbId(value);
                    case "职业:":
                        personageInfo.setOccupation(value);
                    default:
                        break;
                }
            }
        }
        Element descElement = contentDiv.selectFirst("div.desc");
        Element content = descElement.selectFirst(".content p");
        if (content != null){
            personageInfo.setSummary(content.text());
        }
        return personageInfo;
    }
}
