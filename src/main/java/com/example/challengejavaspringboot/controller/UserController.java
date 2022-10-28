package com.example.challengejavaspringboot.controller;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext context;


    @GetMapping("/generate/{count}")
    public void createJson(@PathVariable(value = "count") int count,
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




}
