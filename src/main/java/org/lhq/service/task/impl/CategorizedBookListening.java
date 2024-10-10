package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.service.task.FileListening;
import org.lhq.service.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CategorizedBookListening extends FileListening {
    private static final Logger log = LoggerFactory.getLogger(CategorizedBookListening.class);

    public CategorizedBookListening(BeanUtils beanUtils,
                                       DirConfigProperties dirConfigProperties) {
        super(beanUtils, dirConfigProperties);
    }

    @Override
    public void process(Map<String, File> filaNameMap, BeanUtils beanUtils, DirConfigProperties dirConfigProperties) {
        log.info("CategorizedBookListening process");
        log.info("filaNameMap: {}", filaNameMap);
        filaNameMap.forEach((name, value) -> log.info("fileName:{},filePath:{}", name, value));
        //TODO
    }

    @Override
    protected boolean isNeedFileExt(File file) {
        List<String> ebookExtensions = getDirConfiguration().ebookExtensions();
        for (String extension : ebookExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getScanDirs() {
        return getDirConfiguration().bookDir();
    }
}
