package com.chex.api.user;

import com.chex.files.FileNameStruct;
import com.chex.files.FileType;
import com.chex.user.model.User;
import com.chex.user.repository.UserRepository;
import com.chex.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfilePhotoService {

    private UserRepository userRepository;
    private FileService fileService;

    @Autowired
    public ProfilePhotoService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    public void deleteProfilePhoto(Long userid){
        User user = this.userRepository.getById(userid);
        fileService.deleteFile(user.getImgpath());
        user.setImgurl(null);
        user.setImgurl(null);
        this.userRepository.save(user);
    }

    public void setNewProfilePhoto(String stringPhoto, Long userid){
        User user = this.userRepository.getById(userid);

        MultipartFile multipartFile = fileService.convertToMultipartFile(stringPhoto);
        FileNameStruct fileNameStruct = fileService.uploadPhoto(multipartFile, user, FileType.PROFILEPHOTO);

        user.setImgurl(fileNameStruct.webAppPath);
        user.setImgpath(fileNameStruct.realPath);
        this.userRepository.save(user);
    }
}
