package com.example.FileUploaderAndDownloader.File.dto;

import com.example.FileUploaderAndDownloader.User.UserEntity;
import lombok.Data;
import org.apache.catalina.User;

import java.io.File;

@Data
public class FileDownloadPictureDTO {
    private UserEntity user;
    private byte[] profilePic;
}
