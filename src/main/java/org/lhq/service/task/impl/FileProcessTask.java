package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookInfo;
import org.lhq.factory.FileGenFactory;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.task.FileProcess;
import org.lhq.service.gen.Gen;
import org.lhq.service.utils.CommonUtils;
import org.lhq.service.utils.thread.ThreadPoolType;
import org.lhq.service.utils.thread.ThreadPoolUtil;
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
import java.util.Optional;

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
        String bookDir = dirConfigProperties.bookDir();
        List<BookInfo> bookInfoList = searchLoader.search(htmlParseProvider, fileName);
        if (bookInfoList.isEmpty()) {
            log.info("search book info isEmpty {}", bookInfoList);
            return;
        }
        BookInfo firstBook = bookInfoList.getFirst();
        List<String> authorList = Optional.ofNullable(firstBook.getAuthor()).orElse(Collections.singletonList(""));
        String author = Optional.ofNullable(authorList.getFirst()).orElse("");
        String title = Optional.of(firstBook.getTitle()).orElse("");
        String bookImageUrl = firstBook.getImage();
        String newFilePathStr = bookDir + File.separator + author + File.separator + title + File.separator + title + "-" + author + ".pdf";
        File targetDir = new File(bookDir + File.separator + author + File.separator + title);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            log.warn("create target dir error {}", targetDir.getAbsolutePath());
            return;
        }
        File newFile = new File(newFilePathStr);
        ThreadPoolUtil.execute(ThreadPoolType.FILE_RW_THREAD,()-> moveFile(taskFile, newFile));
        ThreadPoolUtil.execute(ThreadPoolType.FILE_RW_THREAD,()-> saveCoverImage(bookImageUrl, newFile));
        ThreadPoolUtil.execute(ThreadPoolType.FILE_RW_THREAD,()-> {
            Gen<BookInfo> jsonGen = FileGenFactory.getFileGen("json");
            jsonGen.genFile(firstBook, newFile);
        });
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


    private void saveCoverImage(String bookImageUrl, File newFile) {
        try {
            List<Byte> byteList = imageLoader.load((url, html) -> Collections.emptyList(), bookImageUrl);
            byte[] imageData = CommonUtils.byteArrayTran(byteList);
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
        } catch (IOException e) {
            log.error("save cover image error", e);
        }
    }

    private void moveFile(File realFile, File newFile)  {
        try {
            Path oldFilePath = Paths.get(realFile.getAbsolutePath());
            Path newFilePath = Paths.get(newFile.getAbsolutePath());
            Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
           log.error("move file error", e);
        }
    }
}
