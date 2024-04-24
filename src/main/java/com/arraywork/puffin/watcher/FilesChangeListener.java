package com.arraywork.puffin.watcher;

import java.util.Set;

import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;

/**
 * 文件变化监听器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
public class FilesChangeListener implements FileChangeListener {

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles files : changeSet) {
            for (ChangedFile file : files.getFiles()) {
                System.out.println(file);
            }
        }
    }

}