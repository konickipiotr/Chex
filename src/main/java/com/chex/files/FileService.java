package com.chex.files;

import com.chex.config.GlobalSettings;
import com.chex.modules.post.model.PostPhoto;
import com.chex.user.model.User;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FileService {
    private static long photoId = 1;

    public void createUserSpace(User user){

        String postsPhoto = getUserSpacePath(user);
        File userDirPosts = new File(postsPhoto);
        if(!userDirPosts.exists()){
            userDirPosts.mkdirs();
        }
    }

    public void deleteUserSpace(User user){
        try {
            FileUtils.deleteDirectory(new File(GlobalSettings.usersSpace + "/" + getUserDirectoryName(user)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String path){
        try {
            FileUtils.delete(new File(GlobalSettings.appPath + path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePostsPhotos(List<PostPhoto> photos) {
        for(PostPhoto p : photos){
            try {
                FileUtils.delete(new File(GlobalSettings.appPath + p.getImg()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserDirectoryName(User user){
        return user.getLastname() + "_" + user.getId();
    }

    private String getUserSpacePath(User user){
        return GlobalSettings.usersSpace  + "/" + getUserDirectoryName(user);
    }

    public long checkUserSpace(User user){
        return FileUtils.sizeOfDirectory(new File(GlobalSettings.usersSpace + "/" + getUserDirectoryName(user)));
    }

    public List<MultipartFile> convertToMultipartFiles(List<String> imagesStringBytes){
        List<MultipartFile> mFiles = new ArrayList<>();
        for(String sImage : imagesStringBytes){
            byte[] imgbyte = Base64.getDecoder().decode(sImage);
            mFiles.add(new CustomMultipartFile(imgbyte));
        }
        return mFiles;
    }

    public MultipartFile convertToMultipartFile(String imagesStringBytes){
        byte[] imgbyte = Base64.getDecoder().decode(imagesStringBytes);
        return new CustomMultipartFile(imgbyte);
    }

    public boolean uploadFiles(MultipartFile mFile, String filePath){
        if (mFile == null || mFile.isEmpty()) return false;
        try {
            Path fullpath = Paths.get(GlobalSettings.appPath + filePath);
            Files.copy(mFile.getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String createFileName(MultipartFile mFile, User user, FileType fileType){

        String filename;
        String filepath;
        String directory = "/users/" + getUserDirectoryName(user);
        String fileExtension = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().length() - 3);

        switch (fileType){
            case POSTPHOTO:{
                LocalDateTime now = LocalDateTime.now();
                now = now.truncatedTo(ChronoUnit.SECONDS);
                filename = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + photoId++ + "." + fileExtension;
                filepath = directory + "/posts/" + filename;

                if(photoId == Integer.MAX_VALUE)
                    photoId = 0;
            }break;
            case PROFILEPHOTO:{
                filepath = directory + "/profile_photo." + fileExtension;
            }break;
            default:
                throw new IllegalArgumentException();
        }

        File dir = new File(GlobalSettings.appPath + directory);
        if(!dir.exists())
            dir.mkdirs();

        return filepath;
    }

    public String createAssetName(MultipartFile mFile, String filename, FileType fileType){

        filename = filename.replaceAll("\\.", "");
        String fileDirectory = "/assets";
        filename = filename + "." + mFile.getOriginalFilename().substring(mFile.getOriginalFilename().length() - 3);

        switch (fileType){
            case ACHIEVEMENTASSET:{
                fileDirectory += "/achievements/";
            }break;
            case PLACEPICTURE:{
                fileDirectory += "/places/";
            }break;
            default:
                throw new IllegalArgumentException();
        }

        File dir = new File(GlobalSettings.appPath + fileDirectory);
        if(!dir.exists())
            dir.mkdirs();

        return fileDirectory + filename;
    }
}
