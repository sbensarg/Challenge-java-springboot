package com.example.challengejavaspringboot.controller;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @GetMapping("/generate")
    public void createJson(@RequestParam(name = "count") int count,
                           HttpServletRequest request, HttpServletResponse response)

    {
        List<Object> users = userService.generateUsers(count);
        boolean isFlag = userService.createJson(users, context);
        if (isFlag)
        {
            String fullPath = request.getServletContext().getRealPath("/tmp/files/"+"users.json");
            userService.fileDownload(fullPath, context, response, "users.json");
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        int ret = userService.saveDataFromUploadfile(multipartFile);
        ObjectNode objectNode = null;
        if (ret != -1) {
            long total = userService.getTotalRecords();
            ObjectMapper mapper = new ObjectMapper();
            objectNode = mapper.createObjectNode();
            objectNode.put("total_nbr_records", total);
            objectNode.put("imported", total - ret);
            objectNode.put("not_imported", ret);
        }
        return new ResponseEntity<>(objectNode, HttpStatus.OK);
    }

}
