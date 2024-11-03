package org.lhq.service.book;

import jakarta.inject.Singleton;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.book.BookVo;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class BookService {


    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final DirConfigProperties dirConfigProperties;

    public BookService(DirConfigProperties dirConfigProperties) {
        this.dirConfigProperties = dirConfigProperties;
    }


    public List<BookVo> getBookList() {
        String bookDir = dirConfigProperties.bookDir();
        bookDir = bookDir + File.separator + "bookIndex.json";
        try (FileReader fileReader = new FileReader(bookDir)) {
            log.debug("bookDir:{}", bookDir);
            return JsonUtils.readJsonToList(fileReader, BookVo.class);
        } catch (IOException e) {
            log.error("bookDir not found", e);
            return Collections.emptyList();
        }
    }


    public BookInfo getBookInfo(String id) {
        List<BookVo> bookList = getBookList();
        BookVo exitsBook = bookList.stream()
                .filter(bookVo -> bookVo.getId().equals(id))
                .findFirst()
                .orElse(new BookVo());
        String path = exitsBook.getPath();
        log.info("path:{}", path);
        try (FileReader fileReader = new FileReader(path)) {
            return JsonUtils.fromFileReader(fileReader, BookInfo.class);
        } catch (IOException e) {
            log.info("path not found", e);
        }
        return new BookInfo();
    }

    public BookInfo updateBookInfoLocal(BookInfo bookInfo) {
        List<BookVo> bookList = getBookList();
        String id = bookInfo.getId();
        bookList.stream()
                .filter(bookVo -> bookVo.getId().equals(id))
                .findFirst()
                .ifPresent(bookVo -> {
                    String path = bookVo.getPath();
                    try (FileReader fileReader = new FileReader(path)) {
                        BookInfo info = JsonUtils.fromFileReader(fileReader, BookInfo.class);
                        Optional.ofNullable(info).ifPresent(item -> {
                            Optional.ofNullable(bookInfo.getTitle())
                                    .ifPresent(item::setTitle);
                            Optional.ofNullable(bookInfo.getProducer())
                                    .ifPresent(item::setProducer);
                            Optional.ofNullable(bookInfo.getSummary())
                                    .ifPresent(item::setSummary);
                        });
                        // 将修改后的文件写入回磁盘
                        String jsonStr = JsonUtils.toJson(info);
                        jsonStr = Optional.ofNullable(jsonStr).orElse("");
                        Files.write(Paths.get(path), jsonStr.getBytes());
                    } catch (IOException e) {
                        log.info("path not found", e);
                    }
                });
        return null;
    }


    public List<BookVo> getBookListByKeyword(String keyword) {
        List<BookVo> bookList = getBookList();
        return bookList.stream()
                .filter(item -> {
            boolean containsTitle = item.getTitle().contains(keyword);
            boolean containsSummary = item.getSummary().contains(keyword);
            return containsTitle || containsSummary;
        }
        ).toList();
    }

    public void deleteLocalBook(String id) {
        if (id == null) {
            return;
        }
        List<BookVo> bookList = getBookList();
        bookList.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .ifPresent(item -> {
                    String path = item.getPath();
                    log.info("delete path:{}", path);
                    Path deletePath = Paths.get(path);
                    Path parentPath = deletePath.getParent();
                    Path parent = parentPath.getParent();
                    log.info("parent path:{}", parent);
                    if (!Files.isDirectory(parent)) {
                        return;
                    }
                    try (Stream<Path> list = Files.list(parent)){
                        // 递归删除目录
                        this.deleteDirectoryRecursively(parentPath);
                        boolean present = list.findAny().isPresent();
                        if (!present) {
                            log.info("delete parent path:{}", parent);
                            Files.delete(parent);
                        }
                    } catch (IOException e) {
                        log.error("delete path error", e);
                    }
                });
        if (bookList.removeIf(item -> id.equals(item.getId()))) {
            try {
                String jsonStr = JsonUtils.toJson(bookList);
                jsonStr = Optional.ofNullable(jsonStr).orElse("");
                String bookDir = dirConfigProperties.bookDir();
                String filePath = bookDir + File.separator + "bookIndex.json";
                Files.write(Paths.get(filePath), jsonStr.getBytes());
                log.info("delete book success  bookList:{}",bookList);
            } catch (IOException e) {
                log.error("write bookIndex.json error", e);
            }
        }
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                for (Path entry : entries.toList()) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }



    public Map<String, List<BookVo>> getAllPublisher() {
        List<BookVo> bookList = getBookList();
        return bookList.stream()
                .collect(
                        Collectors.groupingBy(BookVo::getPublisher)
                );
    }

}
