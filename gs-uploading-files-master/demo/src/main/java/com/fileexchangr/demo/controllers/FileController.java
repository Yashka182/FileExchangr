package com.fileexchangr.demo.controllers;

import com.fileexchangr.demo.entitys.DBFile;
import com.fileexchangr.demo.entitys.User;
import com.fileexchangr.demo.repositories.FileRepository;
import com.fileexchangr.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fileexchangr.demo.configs.StringConstants;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/")
    public String listFiles(Model model ){
        fileService.fillModel(model);
        return "uploadForm";
    }

    @PostMapping("/")
    public String upload(@RequestParam(name = "filename") String filename,
                         MultipartFile file) throws IOException {
        User user = fileService.getLoggedUser();
        fileService.addFile(filename, user, file);
        return "redirect:/";
    }

    @GetMapping("/download")
    public String download(@RequestParam(name = "file") String fileName,
                           Model model) throws IOException {
        DBFile file = fileRepository.findByName(fileName);
        User user = fileService.getLoggedUser();
        if(fileService.canDownload(user, file)){
            return "redirect:/file/" + file.getName();
        } else{
            fileService.fillModel(model);
            model.addAttribute(StringConstants.warning, StringConstants.downloadLimit);
            return "uploadForm";
        }
    }

    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        DBFile file = fileRepository.findByName(filename);
        Resource resource = new UrlResource(file.getPath());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                StringConstants.attachmentFilename + file.getName() + "\"").body(resource);
    }

}
