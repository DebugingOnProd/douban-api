package org.lhq.service.gen;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.entity.book.BookInfo;
import org.lhq.factory.FileGenFactory;
import org.lhq.service.utils.JsonUtils;

import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GenTest {


    @Test
    @DisplayName("生成xml文件")
    void getXml() {
        Gen<BookInfo> xml = FileGenFactory.getFileGen("xml");
        InputStream resourceAsStream = getClass().getResourceAsStream("/books/three_body.json");
        BookInfo bookInfo = JsonUtils.readInputStreamToJson(resourceAsStream, BookInfo.class);
        File file = new File("/test.xml");
        xml.genFile(bookInfo,file);
        assertTrue(file.exists());
    }
}