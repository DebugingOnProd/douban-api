package org.lhq.service.task;


import org.lhq.config.DirConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanTask implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(ScanTask.class);

    private final SearchLoader<BookInfo> searchLoader;
    private final HtmlParseProvider<BookInfo> htmlParseProvider;
    private final DirConfigProperties dirConfigProperties;
    private final EntityLoader<List<Byte>> imageLoader;

    public ScanTask(SearchLoader<BookInfo> searchLoader,
                    HtmlParseProvider<BookInfo> htmlParseProvider, DirConfigProperties dirConfigProperties, EntityLoader<List<Byte>> imageLoader) {
        this.searchLoader = searchLoader;
        this.htmlParseProvider = htmlParseProvider;
        this.dirConfigProperties = dirConfigProperties;
        this.imageLoader = imageLoader;
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        String scanDir = dirConfigProperties.autoScanDir();
        log.info("dir scan start dir:{}", scanDir);
        File directory = new File(scanDir);
        if (!directory.exists()|| !directory.isDirectory()) {
            log.warn("dir scan dir not exists or not directory:{}", scanDir);
            return;
        }
        listFilesRecursively(directory);

    }

    private void listFilesRecursively(File directory) {
        Map<String,File> fileName = new HashMap<>();
        File[] files = directory.listFiles();
        if (files == null){
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                listFilesRecursively(file);
            } else {
                // Process the file
                if(isPdfFile(file)){
                    fileName.put(removeExtension(file.getName()),file);
                }
            }
        }

        moveToNewDir(fileName);

    }

    private void moveToNewDir(Map<String, File> fileName) {
        for (Map.Entry<String, File> entry : fileName.entrySet()) {
            String name = entry.getKey();
            File realFile = entry.getValue();
            try {
                log.info("fileName:{} ,filePath: {}",name, realFile.getAbsolutePath());
                List<BookInfo> bookInfoList = searchLoader.search(htmlParseProvider, name);
                String bookDir = dirConfigProperties.bookDir();
                if (bookInfoList.isEmpty()) {
                    continue;
                }
                log.info("bookInfoList:{}",bookInfoList);
                BookInfo firstBook = bookInfoList.getFirst();
                String author = firstBook.getAuthor().getFirst();
                String title = firstBook.getTitle();
                String bookImageUrl = firstBook.getImage();
                String newFilePathStr =
                        bookDir + File.separator + author + File.separator + title + File.separator + title + "-" + author + ".pdf";
                File targetDir = new File(bookDir + File.separator + author + File.separator + title);
                if (!targetDir.exists() && !targetDir.mkdirs()){
                    log.warn("create target dir error {}" , targetDir.getAbsolutePath());
                    return;
                }
                File newFile = new File(newFilePathStr);
                Path oldFilePath = Paths.get(realFile.getAbsolutePath());
                Path newFilePath = Paths.get(newFile.getAbsolutePath());
                Files.move(oldFilePath,newFilePath, StandardCopyOption.REPLACE_EXISTING);
                List<Byte> byteList = imageLoader.load((url, html) -> Collections.emptyList(), bookImageUrl);
                saveImage(byteList, newFile);
            } catch (IOException e) {
                log.error("file move error",e);
            }
        }
    }

    private void saveImage(List<Byte> byteList, File newFile) throws IOException {
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
        ImageIO.write(image,getImageFormat(filePath),output);
    }

    private boolean isPdfFile(File file){
        return file.getName().toLowerCase().endsWith(".pdf");
    }
    private String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
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
}
