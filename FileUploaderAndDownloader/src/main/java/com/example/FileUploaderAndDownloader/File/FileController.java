package com.example.FileUploaderAndDownloader.File;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/upload")
    public List<String> upload(@RequestParam MultipartFile[] files) throws Exception {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files){
            String singleUpdatedFileName = fileStorageService.upload(file);
            fileNames.add(singleUpdatedFileName);
        }
            return fileNames;

    }

    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) throws Exception {
        String extension = FilenameUtils.getExtension(fileName);

        switch (extension) {
            case "gif" -> response.setContentType(MediaType.IMAGE_GIF_VALUE);
            case "jpg", "jpeg" -> response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "png" -> response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return fileStorageService.download(fileName);

    }


}
