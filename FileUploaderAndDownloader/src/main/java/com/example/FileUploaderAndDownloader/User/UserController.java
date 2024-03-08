package com.example.FileUploaderAndDownloader.User;

import com.example.FileUploaderAndDownloader.File.FileStorageService;
import com.example.FileUploaderAndDownloader.File.dto.FileDownloadPictureDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public UserEntity create(@RequestBody UserEntity user) {
        return userRepository.save(user);

    }


    @PostMapping("/{id}/profilePic")
    public UserEntity uploadProfilePicture(@PathVariable long id, @RequestParam MultipartFile profilePic) throws Exception {
        return userService.UploadProfilePic(id, profilePic);

    }

    @GetMapping("/getAll")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}/getSingle")
    public Optional<UserEntity> getSingle(@PathVariable long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/{id}/profilePic")
    public @ResponseBody byte[] getProfilePicture(@PathVariable long id, HttpServletResponse response) throws Exception {
        FileDownloadPictureDTO fileDownloadPictureDTO = userService.downloadProfilePic(id);

        String fileName = fileDownloadPictureDTO.getUser().getProfilePicture();
        String extension = FilenameUtils.getExtension(fileName);
        if (fileName == null) throw new Exception("User doesn't have profile picture");
        switch (extension) {
            case "gif" -> response.setContentType(MediaType.IMAGE_GIF_VALUE);
            case "jpg", "jpeg" -> response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "png" -> response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return fileDownloadPictureDTO.getProfilePic();


    }

    @PutMapping("/{id}/update")
    public UserEntity updateUser(@PathVariable long id, @RequestBody UserEntity user) {
        user.setId(id);
        return userRepository.save(user);

    }

    @DeleteMapping("/{id}/delete")
    public void deleteUser(@PathVariable long id) throws Exception {
        userService.remove(id);
    }
}
