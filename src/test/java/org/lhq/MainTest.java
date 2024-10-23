package org.lhq;

import io.quarkus.test.junit.QuarkusTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@QuarkusTest
class MainTest {
    private static final Logger log = LoggerFactory.getLogger(MainTest.class);

    @Test
    void testHelloEndpoint() {
    }
}
