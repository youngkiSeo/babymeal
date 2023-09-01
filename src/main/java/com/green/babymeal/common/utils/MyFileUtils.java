package com.green.babymeal.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
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
    public static String getRandomFileNm() {
        return UUID.randomUUID().toString();
    }

    // 랜덤 파일명 만들기 (with 확장자)
    public static String getRandomFileNm(String originFileNm) {
        String randomFileName = getRandomFileNm(); // 랜덤 파일명 생성
        String extension = getExt(originFileNm); // 확장자 추출
        return randomFileName + extension;
    }

    // 랜덤 파일명 만들기
    public static String getRandomFileNm(MultipartFile file) {
        String originFileNm = file.getOriginalFilename();
        return getRandomFileNm(originFileNm);
    }

    //확장자 얻기               "aaa.jpg"
    public static String getExt(String fileNm) {
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


    //절대경로 리턴
    public static String getAbsolutePath(String src) {
        return Paths.get(src).toFile().getAbsolutePath(); // 내가 실행하는 드라이버를 자동으로 찍어주는 구문이다 (ex: C: , D:)
    }


    public static void delFolder(String path){
        File file=new File(path);
        if(file.exists() && file.isDirectory()){
            File[] fileArr=file.listFiles(); //폴더안에 파일들을 배열로 받는다
            for (File f:fileArr) {
                if(f.isDirectory()){
                    delFolder(f.getPath());
                }else {
                    f.delete();
                }
            }
        }
        file.delete(); //마지막으로 첫번재폴더를 삭제한다
    }



}
