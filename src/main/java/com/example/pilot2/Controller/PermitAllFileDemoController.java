package com.example.pilot2.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class PermitAllFileDemoController {

    @Value("${file.dir}")
    private String fileDir;

    @PostMapping
    public boolean saveFile(@RequestParam MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return false;
        }
        String saveFileFullPath = fileDir + files.getOriginalFilename();
        File saveFile = new File(saveFileFullPath);
        System.out.println(saveFile.getAbsolutePath());
        files.transferTo(saveFile);

        return true;
    }
}
