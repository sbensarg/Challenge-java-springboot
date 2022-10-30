package com.example.challengejavaspringboot.services;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private int nbr_total = 0;
    public User createuser(User user)
    {

        return userRepository.save(user);
    }

    public long countrecords()
    {
        return userRepository.count();
    }

    public String encode(String passWord) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        return bCryptPasswordEncoder.encode(passWord);

    }
    public static String generateRandomRole() {
        String[] values = {"user", "admin"};
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }

    public List<Object> generateUsers(int count)
    {

        List<Object> list = new ArrayList();
        AtomicLong uniqueId = new AtomicLong();

        while (count > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Faker fakeuser = new Faker();
            String dob = sdf.format(fakeuser.date().birthday());

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode user = mapper.createObjectNode();
            user.put("firstName",fakeuser.name().firstName());
            user.put("lastName", fakeuser.name().lastName());
            user.put("birthDate", dob);
            user.put("city", fakeuser.address().city());
            user.put("country", fakeuser.address().countryCode());
            user.put("avatar", fakeuser.avatar().image());
            user.put("company", fakeuser.company().name());
            user.put("jobPosition", fakeuser.job().position());
            user.put("mobile", fakeuser.phoneNumber().cellPhone());
            user.put("username", fakeuser.name().username());
            user.put("email", fakeuser.internet().emailAddress());
            user.put("password", fakeuser.internet().password(6, 10));
            user.put("role", this.generateRandomRole());

            list.add(user);
            count--;
        }
        return (list);
    }

    public boolean createJson(List<Object> users, ServletContext context)
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

    public int saveDataFromUploadfile(MultipartFile multipartFile)
    {
        int ret = -1;
        String extention = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (extention.equalsIgnoreCase("json"))
            ret = readDataFromJson(multipartFile);
        return (ret);
    }

    public int  getTotalRecords()
    {
        return this.nbr_total;
    }

    void setTotalRecords(int t)
    {
        this.nbr_total = t;
    }
    private int readDataFromJson(MultipartFile multipartFile)
    {
        int not_imp = 0;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            List<User> users = Arrays.asList(mapper.readValue(inputStream, User[].class));
            this.setTotalRecords(users.size());
            if (users != null && users.size() > 0)
            {
                for (User user : users)
                {
                    Optional<User> optRecord = userRepository.findEmailorUsername(user);
                    if(optRecord.isPresent()){
                        not_imp++;
                    }else{
                        String encryptedPassword = this.encode(user.getPassword());
                        user.setPassword(encryptedPassword);
                        this.createuser(user);
                    }
                }
            }
        }
        catch (Exception e)
        {
            return (-1);
        }
        return (not_imp);
    }

}
