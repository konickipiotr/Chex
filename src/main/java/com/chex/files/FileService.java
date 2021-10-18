package com.chex.files;

import com.chex.config.GlobalSettings;
import com.chex.modules.post.model.PostPhoto;
import com.chex.user.model.User;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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


    public String saveToTmp(MultipartFile file) throws FileNotFoundException {

        if (file == null || file.isEmpty())
            throw new FileNotFoundException();

        String ext = "." + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);

        File dir = new File(GlobalSettings.chexTmp);
        if(!dir.exists())
            dir.mkdirs();


        String randomFilename;
        do{
            randomFilename = RandomString.make(40);
            randomFilename += ext;
        }while (Arrays.asList(dir.list()).contains(randomFilename));


        String path = dir.getPath() + "/" + randomFilename;
        try {
            Path fullpath = Paths.get(path);
            Files.copy(file.getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw  new FailedSaveFileException();
        }

        return path;
    }

    public String moveToFromTmpToWorkspace(String tmpPath, FileType fileType) throws FileNotFoundException {

        File tmpFile = new File(tmpPath);

        if(!new File(tmpPath).exists()){
            throw new FileNotFoundException();
        }

        String ext = "." + tmpPath.substring(tmpPath.length() - 3);

        File dir = new File(GlobalSettings.appPath + "/assets/challenges");
        if(!dir.exists())
            dir.mkdirs();

        String randomFilename;
        do{
            randomFilename = RandomString.make(20);
            randomFilename = "ch" + randomFilename + ext;
        }while (Arrays.asList(dir.list()).contains(randomFilename));


        String path = dir.getPath() + "/" + randomFilename;
        try {
            Files.copy(tmpFile.toPath(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw  new FailedSaveFileException();
        }

        return randomFilename;
    }
}
