package org.lhq;

import io.quarkus.test.junit.QuarkusTest;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.perse.HtmlParseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@QuarkusTest
class MainTest {
    private static final Logger log = LoggerFactory.getLogger(MainTest.class);

    @Test
    void testHelloEndpoint() {
       HtmlParseProvider<BookInfo> parseProvider = (url, html) -> new BookInfo();
       log.info("{}",parseProvider.parse("",new Document("")));
    }
}
