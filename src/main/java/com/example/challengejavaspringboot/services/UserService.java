package com.example.challengejavaspringboot.services;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
        AtomicInteger uniqueId=new AtomicInteger();

        while (count > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Faker fakeuser = new Faker();
            String dob = sdf.format(fakeuser.date().birthday());
            User user = new User((long) uniqueId.getAndIncrement(),fakeuser.name().firstName(), fakeuser.name().lastName(),
                    dob, fakeuser.address().city(), fakeuser.address().countryCode(),
                    fakeuser.avatar().image(), fakeuser.company().name(), fakeuser.job().position(),
                    fakeuser.phoneNumber().cellPhone(), fakeuser.name().username(), fakeuser.internet().emailAddress(),
                    fakeuser.internet().password(6, 10), this.generateRandomRole());
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

    public String saveFile(String fileName, MultipartFile multipartFile)
    {
        Path uploadDirectory = Paths.get("Files-Upload");

        String fileCode = RandomStringUtils.randomAlphabetic(8);
        try(InputStream inputStream = multipartFile.getInputStream())
        {
            Path filePath = uploadDirectory.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error saving uploaded file: " + fileName, e);
        }
        return (fileCode);
    }
}
