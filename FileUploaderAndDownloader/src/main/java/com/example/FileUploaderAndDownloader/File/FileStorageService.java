package com.example.FileUploaderAndDownloader.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "." + extension;
        File finalFolder = new File(fileRepositoryFolder);
        if (!finalFolder.exists()) throw new IOException("final folder doesn't exist");
        if (!finalFolder.isDirectory()) throw new IOException("final folder isn't a directory");

        File finalDestination = new File(fileRepositoryFolder + "\\" + newFileName);
        if (finalDestination.exists()) throw new IOException("File conflict");
        file.transferTo(finalDestination);
        return newFileName;
    }

    public byte[] download(String fileName) throws IOException {
        File fileFromRepo = new File(fileRepositoryFolder + "\\" + fileName);
        if (!fileFromRepo.exists()) throw new IOException("file doesn't exist");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepo));
    }

    public void remove(String profilePicture) throws IOException {
        File fileFromRepo = new File(fileRepositoryFolder + "\\" + profilePicture);
        if (!fileFromRepo.exists()) return;
        if (!fileFromRepo.delete()) throw new IOException("cannot delete file");
        fileFromRepo.delete();
    }
}
