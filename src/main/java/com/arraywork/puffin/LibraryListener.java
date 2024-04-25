package com.arraywork.puffin;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.arraywork.springfield.filewatch.DirectoryWatcher;
import com.arraywork.springfield.filewatch.FileSystemListener;

/**
 *
 * @author AiChen
 * @created 2024/04/25
 */
@Component
public class LibraryListener implements FileSystemListener {

    @Override
    public void onStarted(File file) {
        System.out.print("Started: ");
        System.out.println(file);
    }

    @Override
    public void onAdded(File file) {
        System.out.print("Added: ");
        System.out.println(file);
    }

    @Override
    public void onModified(File file) {
        System.out.print("Modified: ");
        System.out.println(file);
    }

    @Override
    public void onDeleted(File file) {
        System.out.print("Deleted: ");
        System.out.println(file);
    }

    @Bean
    public DirectoryWatcher directoryWatcher() {
        return new DirectoryWatcher(3, 1, new LibraryListener());
    }

}