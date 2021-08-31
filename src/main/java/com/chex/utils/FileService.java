package com.chex.utils;

import com.chex.config.GlobalSettings;
import com.chex.user.User;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    public void createUserSpace(User user){

        String postsPhoto = GlobalSettings.usersSpace + File.separator + getUserDirectoryName(user) + File.separator + "posts";
        File userDirPosts = new File(postsPhoto);
        if(!userDirPosts.exists()){
            userDirPosts.mkdirs();
        }

        String profilePhoto = GlobalSettings.usersSpace + File.separator + getUserDirectoryName(user) + File.separator + "profle_photo";
        File userDirProfil = new File(profilePhoto);
        if(!userDirProfil.exists()){
            userDirProfil.mkdirs();
        }
    }

    public void deleteUserSpace(User user) throws IOException {
        FileUtils.deleteDirectory(new File(GlobalSettings.usersSpace + File.separator + getUserDirectoryName(user)));
    }

    private String getUserDirectoryName(User user){
        return user.getLastname() + "_" + user.getId();
    }

    public long checkUserSpace(User user){
        return FileUtils.sizeOfDirectory(new File(GlobalSettings.usersSpace + File.separator + getUserDirectoryName(user)));
    }
}
