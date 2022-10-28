package com.example.challengejavaspringboot.services;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

//    @Autowired
//    UserRepository userRepository;


//    public User createuser(User user)
//    {
//        return userRepository.save(user);
//    }

    public static User.UserRole generateRandomRole() {
        User.UserRole[] values = User.UserRole.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }

    public List<User> generateUsers(int count)
    {

        List<User> list = new ArrayList();

        while (count > 0)
        {
            Faker fakeuser = new Faker();
            User user = new User(fakeuser.name().firstName(), fakeuser.name().lastName(),
                    fakeuser.date().birthday(), fakeuser.address().city(), fakeuser.address().countryCode(),
                    fakeuser.avatar().image(), fakeuser.company().name(), fakeuser.job().position(),
                    fakeuser.phoneNumber().cellPhone(), fakeuser.name().username(), fakeuser.internet().emailAddress(),
                    fakeuser.internet().password(), this.generateRandomRole());
            list.add(user);
            count--;
        }
        return (list);
    }

    public boolean createJson(List<User> users, ServletContext context)
    {
        String filePath = context.getRealPath("/tmp/files");
        boolean exists = new File(filePath).exists();
        if(!exists)
        {
            new File(filePath).mkdirs();
        }
        File file = new File(filePath+"/"+File.separator+"users.json");
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.writeValue(file, users);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    public void fileDownload(String fullPath, ServletContext context, HttpServletResponse response, String fileName)
    {
        File file = new File(fullPath);
        final int BUFFER_SIZE = 4096;
        if (file.exists())
        {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                String mimeType = context.getMimeType(fullPath);
                response.setContentType(mimeType);
                response.setHeader("content-disposition", "attachment; filename="+fileName);
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                file.delete();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }
}
