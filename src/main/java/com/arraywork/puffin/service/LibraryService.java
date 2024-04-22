package com.arraywork.puffin.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.Library;
import com.arraywork.puffin.repository.LibraryRepo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

/**
 * 媒体库服务
 * @author AiChen
 * @created 2024/04/22
 */
@Service
public class LibraryService {

    @Resource
    private LibraryRepo libraryRepo;

    @PostConstruct
    public void test() {
        System.out.println("--------------------");
        Library lib = new Library();
        lib.setPath("/ect/data");
        lib.setName("JFK的萨拉");

        // Metafield[] fs = { Metafield.CENSORSHIP, Metafield.PRODUCER };
        lib.setMetafields(null);
        libraryRepo.save(lib);

        Optional<Library> opt = libraryRepo.findById(4791492636248130792L);
        System.out.println(opt.get().getMetafields().length);

    }

}