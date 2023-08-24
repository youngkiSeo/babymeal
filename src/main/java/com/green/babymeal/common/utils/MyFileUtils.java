package com.green.babymeal.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class MyFileUtils {
    @Value("${file.dir}")
    private String uploadImagePath;

    //폴더 만들기
    public String makeFolders(String path) {
        File folder = new File(uploadImagePath, path);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }

    //랜덤 파일명 만들기
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();
    }

    //랜덤 파일명 만들기 (with 확장자)
    public String getRandomFileNm(String originFileNm) {
        return getRandomFileNm() + getExt(originFileNm);
    }

    //랜덤 파일명 만들기
    public String getRandomFileNm(MultipartFile file) {
        return getRandomFileNm(file.getOriginalFilename());
    }

    //확장자 얻기               "aaa.jpg"
    public String getExt(String fileNm) {
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    public String transferTo(MultipartFile mf, String target) {
        String fileNm = getRandomFileNm(mf); //"aslkdfjaslkf2130asdwds.jpg"
        String basePath = makeFolders(target); //(폴더가 없을 수 있기 때문에)폴더를 만들어준다.
        File saveFile = new File(basePath, fileNm);
        try {
            mf.transferTo(saveFile);
            return fileNm;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
