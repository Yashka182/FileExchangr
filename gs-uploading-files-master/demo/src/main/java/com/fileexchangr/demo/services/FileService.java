package com.fileexchangr.demo.services;

import com.fileexchangr.demo.entitys.DBFile;
import com.fileexchangr.demo.entitys.User;
import com.fileexchangr.demo.repositories.UserRepository;
import com.fileexchangr.demo.storage.StorageProperties;
import com.fileexchangr.demo.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public void addFile(String name, User user, MultipartFile file) throws IOException {
        if(!file.isEmpty() && file != null){
            File directory = new File(StorageProperties.uploadPath);

            if(!directory.exists()){
                directory.mkdir();
            }

            String resultPath = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            file.transferTo(new File(StorageProperties.uploadPath + resultPath));

            if(name.equals("")){
                name = file.getOriginalFilename();
            }

            DBFile newFile = new DBFile(user, name, file.getSize());
            newFile.setPath(resultPath);
            fileRepository.save(newFile);
        }
    }

    public void fillModel(Model model){
        Iterable<DBFile> files = fileRepository.findAll();
        model.addAttribute("files", files);
    }

    public boolean canDownload(User user, DBFile file) throws IOException {
        Date date = new Date();
        if(user.getBlocked()) {
            if (user.getDateOfBlock().after(date)) {
                user.setBlocked(false);
                user.setDownloadLimit(0L);
            } else {
                return false;
            }
        }
        if(user.getDownloadLimit() < StorageProperties.maxDownloadSize){
            user.setDownloadLimit(user.getDownloadLimit() - file.getSize());
            if(user.getDownloadLimit()< 0L){
                user.setBlocked(true);
                return false;
            }
            userService.justSave(user);
            return true;
        }else {
            user.setBlocked(true);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            user.setDateOfBlock(calendar.getTime());
            userService.justSave(user);
            return false;
        }
    }

    public User getLoggedUser(){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }
}
