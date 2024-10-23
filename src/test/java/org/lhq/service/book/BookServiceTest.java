package org.lhq.service.book;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class BookServiceTest {


    private static final Logger log = LoggerFactory.getLogger(BookServiceTest.class);
    private BookService bookService;
    private static final String BOOK_DIR = "src/test/resources/books/";

    @BeforeEach
    public void setUp() {
        DirConfigProperties dirConfigProperties = new DirConfigProperties() {
            @Override
            public String autoScanDir() {
                return "";
            }

            @Override
            public Integer autoScanInterval() {
                return 0;
            }

            @Override
            public String bookDir() {
                return BOOK_DIR;
            }

            @Override
            public String movieDir() {
                return "";
            }

            @Override
            public List<String> ebookExtensions() {
                return List.of();
            }
        };
        bookService = new BookService(dirConfigProperties);
    }

    @Test
    @DisplayName("Test getBookList method")
    void getBookList_ValidJsonFile_ReturnsBookList() {
        List<BookVo> bookVoList = bookService.getBookList();

        assertEquals(12, bookVoList.size(), "Expected book list size is 2");

        BookVo bookVo = bookVoList.getFirst();
        assertEquals("Kathy Sierra,Bert Bates 著", bookVo.getAuthors().getFirst(), "Expected author is testAuthor");
        assertEquals("Head First Java（第二版·中文版）", bookVo.getTitle(), "Expected title is testBook");
    }

    @Test
    void getBookList_JsonFileNotFound_ReturnsEmptyList() {
        DirConfigProperties dirConfigProperties = new DirConfigProperties() {
            @Override
            public String autoScanDir() {
                return "";
            }

            @Override
            public Integer autoScanInterval() {
                return 0;
            }

            @Override
            public String bookDir() {
                return "src/test/resources/nonexistent/";
            }

            @Override
            public String movieDir() {
                return "";
            }

            @Override
            public List<String> ebookExtensions() {
                return List.of();
            }
        };
        bookService = new BookService(dirConfigProperties);
        List<BookVo> bookVoList = bookService.getBookList();
        log.info("bookVoList:{}", bookVoList);
        assertTrue(bookVoList.isEmpty(), "Expected book list to be empty");
    }


    @Test
    @DisplayName("Test getBookListByKeyword method with matching keyword")
    void getBookListByKeyword_MatchingKeyword_ReturnsFilteredList() {
        List<BookVo> bookVoList = bookService.getBookListByKeyword("Java");

        assertFalse(bookVoList.isEmpty(), "Expected book list to be non-empty");
        assertTrue(bookVoList.stream().allMatch(bookVo -> bookVo.getTitle().contains("Java") || bookVo.getSummary().contains("Java")),
                "Expected all books in the list to contain the keyword in title or summary");
    }

    @Test
    @DisplayName("Test getBookListByKeyword method with non-matching keyword")
    void getBookListByKeyword_NonMatchingKeyword_ReturnsEmptyList() {
        List<BookVo> bookVoList = bookService.getBookListByKeyword("NonExistentKeyword");

        assertTrue(bookVoList.isEmpty(), "Expected book list to be empty");
    }
}