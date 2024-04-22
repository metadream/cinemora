package com.arraywork.puffin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Library;
import com.arraywork.puffin.repo.LibraryRepo;

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

    // 获取媒体库列表
    public List<Library> getLibraries() {
        return libraryRepo.findAll();
    }

    // 保存媒体库
    @Transactional(rollbackFor = Exception.class)
    public Library saveLibrary(Library library) {
        return libraryRepo.save(library);
    }

    // 删除媒体库
    @Transactional(rollbackFor = Exception.class)
    public void deleteLibrary(long id) {
        Library library = libraryRepo.getReferenceById(id);
        libraryRepo.delete(library); // TODO 联级删除元数据测试
    }

    // @PostConstruct
    // public void test() {
    // System.out.println("--------------------");
    // Metadata metadata = new Metadata();
    //
    // metadata.setCode("asdfs");
    // metadata.setTitle("警方撒旦卡了发动机是");
    // metadata.setPath("/dfad/s/fdsafds");
    // metadata.setQuality(Quality.HD);
    //
    // String[] aa = { "aa", "bbb", "ccc", "dddddddddddddddddd" };
    // metadata.setProducers(aa);
    // metadataRepo.save(metadata);
    //
    // List<Metadata> list = metadataRepo.findAll(new MetadataSpec());
    // System.out.println(list);
    // }

}