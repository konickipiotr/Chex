package com.chex.utils;

import com.chex.config.GlobalSettings;
import com.chex.modules.post.model.PostPhoto;
import com.chex.user.User;
import com.chex.utils.exceptions.FailedSaveFileException;
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

        String postsPhoto = getUserSpacePhotoPath(user);
        File userDirPosts = new File(postsPhoto);
        if(!userDirPosts.exists()){
            userDirPosts.mkdirs();
        }

        String profilePhoto = GlobalSettings.usersSpace + "/" + getUserDirectoryName(user) + "/profle_photo";
        File userDirProfil = new File(profilePhoto);
        if(!userDirProfil.exists()){
            userDirProfil.mkdirs();
        }
    }

    public void deleteUserSpace(User user){
        try {
            FileUtils.deleteDirectory(new File(GlobalSettings.usersSpace + "/" + getUserDirectoryName(user)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePostsPhotos(List<PostPhoto> photos) {
        for(PostPhoto p : photos){
            try {
                FileUtils.delete(new File(p.getRealPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserDirectoryName(User user){
        return user.getLastname() + "_" + user.getId();
    }
    private String getUserSpacePhotoPath(User user){
        return GlobalSettings.usersSpace  + "/" + getUserDirectoryName(user) + "/posts/";
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

    private FileNameStruct createFileName(MultipartFile mFile, User user){
        String orginalFileName = mFile.getOriginalFilename();
        String fileType = orginalFileName.substring(orginalFileName.length() - 3);

        LocalDateTime now = LocalDateTime.now();
        now = now.truncatedTo(ChronoUnit.SECONDS);

        String filename = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + photoId++ + "." + fileType;

        String realPath = getUserSpacePhotoPath(user);
        String wepAppPath = "/" + GlobalSettings.photosPath + getUserDirectoryName(user)+ "/posts/" + filename;

        File dir = new File(realPath);
        if(!dir.exists())
            dir.mkdirs();

        realPath += filename;

        return new FileNameStruct(realPath, wepAppPath);
    }

    public List<FileNameStruct> uploadPhotos(List<MultipartFile> uploadfiles, User user){

        if (uploadfiles == null || uploadfiles.isEmpty()) return null;

        List<FileNameStruct> filesNames = new ArrayList<>();
        for(MultipartFile mFile : uploadfiles) {
            filesNames.add(createFileName(mFile, user));
        }

        try {
            for(int i = 0; i < uploadfiles.size(); i++ ) {
                Path fullpath = Paths.get(filesNames.get(i).realPath);
                Files.copy(uploadfiles.get(i).getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailedSaveFileException();
        }
        return filesNames;

    }
}
