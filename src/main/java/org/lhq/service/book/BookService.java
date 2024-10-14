package org.lhq.service.book;

import jakarta.inject.Singleton;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookVo;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class BookService {


    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final DirConfigProperties dirConfigProperties;

    public BookService(DirConfigProperties dirConfigProperties) {
        this.dirConfigProperties = dirConfigProperties;
    }


    public List<BookVo> getBookList() {
        String bookDir = dirConfigProperties.bookDir();
        try (FileReader fileReader = new FileReader(bookDir)){
            return JsonUtils.readJsonToList(fileReader, BookVo.class);
        } catch (IOException e) {
            log.error("bookDir not found", e);
        }
        return Collections.emptyList();
    }
}
