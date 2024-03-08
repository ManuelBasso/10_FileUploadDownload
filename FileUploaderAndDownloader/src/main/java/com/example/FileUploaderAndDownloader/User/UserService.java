package com.example.FileUploaderAndDownloader.User;

import com.example.FileUploaderAndDownloader.File.FileStorageService;
import com.example.FileUploaderAndDownloader.File.dto.FileDownloadPictureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStorageService fileStorageService;

    private UserEntity getUser(long userId) throws Exception {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) throw new Exception("user not found");
        return user.get();
    }
    public UserEntity UploadProfilePic(Long id, MultipartFile profilePic) throws Exception {
        UserEntity user = getUser(id);
        if (user.getProfilePicture() != null){
            fileStorageService.remove(user.getProfilePicture());
        }
        String fileName = fileStorageService.upload(profilePic);
        user.setProfilePicture(fileName);
        return userRepository.save(user);
    }

    public FileDownloadPictureDTO downloadProfilePic(long id) throws Exception {
        UserEntity user = getUser(id);
        FileDownloadPictureDTO dto = new FileDownloadPictureDTO();
        dto.setUser(user);
        if(user.getProfilePicture() == null) return dto;

        byte[]profilePic = fileStorageService.download(user.getProfilePicture());
        dto.setProfilePic(profilePic);
        return dto;

    }

    public void remove(long id) throws Exception {
        UserEntity user = getUser(id);
        if (user.getProfilePicture() != null){
            fileStorageService.remove(user.getProfilePicture());
        }
        userRepository.deleteById(id);
    }
}
