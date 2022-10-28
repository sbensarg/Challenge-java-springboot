package com.example.challengejavaspringboot.controller;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext context;


    @GetMapping("/generate")
    public void createJson(@RequestParam(name = "count") int count,
                           HttpServletRequest request, HttpServletResponse response)

    {
        List<User> users = userService.generateUsers(count);
        boolean isFlag = userService.createJson(users, context);
        if (isFlag)
        {
            String fullPath = request.getServletContext().getRealPath("/tmp/files/"+"users.json");
            userService.fileDownload(fullPath, context, response, "users.json");
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile)
    {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        String fileCode = userService.saveFile(fileName, multipartFile);
        String dir = context.getRealPath("/tmp/uploaded");
        boolean exists = new File(dir).exists();
        if(!exists)
        {
            new File(dir).mkdirs();
        }
        String fullPath = dir + fileCode;

        return new ResponseEntity<>(fullPath, HttpStatus.OK);

    }




}
