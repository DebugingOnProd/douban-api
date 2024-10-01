package org.lhq.service.task;

import org.lhq.config.DirConfigProperties;
import org.lhq.service.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class FileListening implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(FileListening.class);
    private final BeanUtils beanUtils;

    private final DirConfigProperties dirConfigProperties;

    protected FileListening(BeanUtils beanUtils, DirConfigProperties dirConfigProperties) {
        this.beanUtils = beanUtils;
        this.dirConfigProperties = dirConfigProperties;
    }


    public abstract void process(Map<String, File> filaNameMap,
                                 BeanUtils beanUtils,
                                 DirConfigProperties dirConfigProperties);

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
            return ;
        }
        File[] files = directory.listFiles();
        if (files == null){
            return ;
        }
        Map<String, File> fileNameMap = new HashMap<>();
        listFilesRecursively(directory,fileNameMap);
        log.info("fileNameMap:{}",fileNameMap);
        process(fileNameMap, beanUtils, dirConfigProperties);
    }


    private void listFilesRecursively(File directory,Map<String, File> fileName) {
        File[] files = directory.listFiles();
        if (files == null){
            return ;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                listFilesRecursively(file, fileName);
            } else {
                // Process the file
                if(isPdfFile(file)){
                    fileName.put(removeExtension(file.getName()),file);
                }
            }
        }
    }


    private String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, dotIndex);
    }

    private boolean isPdfFile(File file){
        return file.getName().toLowerCase().endsWith(".pdf");
    }
}
