package org.lhq.service.task.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.task.FileProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

public class FileProcessTask extends FileProcess<BookInfo> {


    private static final Logger log = LoggerFactory.getLogger(FileProcessTask.class);

    protected FileProcessTask(String fileName,
                              File taskFile,
                              HtmlParseProvider<BookInfo> htmlParseProvider,
                              EntityLoader<List<Byte>> imageLoader,
                              SearchLoader<BookInfo> searchLoader,
                              DirConfigProperties dirConfigProperties) {
        super(
                fileName,
                taskFile,
                htmlParseProvider,
                imageLoader,
                searchLoader,
                dirConfigProperties
        );
    }



    @Override
    public void process(String fileName, File taskFile) {
        try {
            String bookDir = dirConfigProperties.bookDir();
            List<BookInfo> bookInfoList = searchLoader.search(htmlParseProvider, fileName);
            if (bookInfoList.isEmpty()) {
                return;
            }
            BookInfo firstBook = bookInfoList.getFirst();
            String author = firstBook.getAuthor().getFirst();
            String title = firstBook.getTitle();
            String bookImageUrl = firstBook.getImage();
            String newFilePathStr = bookDir + File.separator + author + File.separator + title + File.separator + title + "-" + author + ".pdf";
            File targetDir = new File(bookDir + File.separator + author + File.separator + title);
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                log.warn("create target dir error {}", targetDir.getAbsolutePath());
                return;
            }
            File newFile = new File(newFilePathStr);
            moveFile(taskFile, newFile);
            saveCoverImage(bookImageUrl, newFile);
            writeJsonToFile(firstBook, newFile);
        } catch (IOException e) {
            log.error("file move error", e);
        }
    }

    private String getImageFormat(String filePath) {
        // 获取文件扩展名
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IllegalArgumentException("无效的文件路径: " + filePath);
        }
        String extension = filePath.substring(dotIndex + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "jpg";
            case "png" -> "png";
            case "gif" -> "gif";
            default -> throw new IllegalArgumentException("不支持的图片格式: " + extension);
        };
    }

    private void writeJsonToFile(BookInfo bookInfo, File newFile) {
        File parentFile = newFile.getParentFile();
        String path = parentFile.getPath();
        String filePath = path + File.separator + "metadata.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonStr = objectMapper.writeValueAsString(bookInfo);
            Files.write(Paths.get(filePath), jsonStr.getBytes());
            log.info("write json success {}", filePath);
        } catch (IOException e) {
            log.error("write json error", e);
        }
    }

    private void saveCoverImage(String bookImageUrl, File newFile) throws IOException {
        List<Byte> byteList = imageLoader.load((url, html) -> Collections.emptyList(), bookImageUrl);
        byte[] imageData = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            imageData[i] = byteList.get(i);
        }
        // 将 byte[] 转换为 InputStream
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        // 读取图片数据到 BufferedImage
        BufferedImage image = ImageIO.read(bis);
        File parentFile = newFile.getParentFile();
        String path = parentFile.getPath();
        String filePath = path + File.separator + "cover.jpg";
        // 创建目标文件
        File output = new File(filePath);
        // 将 BufferedImage 写入目标文件
        ImageIO.write(image, getImageFormat(filePath), output);
    }

    private void moveFile(File realFile, File newFile) throws IOException {
        Path oldFilePath = Paths.get(realFile.getAbsolutePath());
        Path newFilePath = Paths.get(newFile.getAbsolutePath());
        Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
}