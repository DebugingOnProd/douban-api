package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.service.utils.ThreadPoolType;
import org.lhq.service.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileListeningTask implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(FileListeningTask.class);


    private final DirConfigProperties dirConfigProperties;

    public FileListeningTask(DirConfigProperties dirConfigProperties) {
        this.dirConfigProperties = dirConfigProperties;
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
            return ;
        }
        Map<String,File> fileName = new HashMap<>();
        File[] files = directory.listFiles();
        if (files == null){
            return ;
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
        log.info("fileName:{}",fileName);

        ThreadPoolUtil.execute(ThreadPoolType.FIXED_THREAD,new FileProcessTask(dirConfigProperties,fileName));
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
}
