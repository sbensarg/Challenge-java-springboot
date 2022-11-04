package com.example.challengejavaspringboot.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.challengejavaspringboot.entity.User;
import com.example.challengejavaspringboot.services.JwtUserService;
import com.example.challengejavaspringboot.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private JwtUserService jwtUserService;

    @MockBean
    private ServletContext servletContext;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#createJson(int, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testCreateJson() throws Exception {
        doNothing().when(userService)
                .fileDownload((String) any(), (ServletContext) any(), (HttpServletResponse) any(), (String) any());
        when(userService.createJson((List<Object>) any(), (ServletContext) any())).thenReturn(true);
        when(userService.generateUsers(anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/users/generate");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("count", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#createJson(int, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testCreateJson2() throws Exception {
        doThrow(new EntityNotFoundException("An error occurred")).when(userService)
                .fileDownload((String) any(), (ServletContext) any(), (HttpServletResponse) any(), (String) any());
        when(userService.createJson((List<Object>) any(), (ServletContext) any())).thenReturn(true);
        when(userService.generateUsers(anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/users/generate");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("count", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#createJson(int, HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testCreateJson3() throws Exception {
        doNothing().when(userService)
                .fileDownload((String) any(), (ServletContext) any(), (HttpServletResponse) any(), (String) any());
        when(userService.createJson((List<Object>) any(), (ServletContext) any())).thenReturn(false);
        when(userService.generateUsers(anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/users/generate");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("count", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#getUserInfo(String, Authentication)}
     */
    @Test
    void testGetUserInfo() throws Exception {
        User user = new User();
        user.setAvatar("Avatar");
        user.setBirthDate("2020-03-01");
        user.setCity("Oxford");
        user.setCompany("Company");
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(123L);
        user.setJobPosition("Job Position");
        user.setLastName("Doe");
        user.setMobile("Mobile");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        when(jwtUserService.getJwtUserByUsername((String) any())).thenReturn(user);

        User user1 = new User();
        user1.setAvatar("Avatar");
        user1.setBirthDate("2020-03-01");
        user1.setCity("Oxford");
        user1.setCompany("Company");
        user1.setCountry("GB");
        user1.setEmail("jane.doe@example.org");
        user1.setFirstName("Jane");
        user1.setId(123L);
        user1.setJobPosition("Job Position");
        user1.setLastName("Doe");
        user1.setMobile("Mobile");
        user1.setPassword("iloveyou");
        user1.setRole("Role");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthDate\":\"2020-03-01\",\"city\":\"Oxford\",\"country\":\"GB"
                                        + "\",\"avatar\":\"Avatar\",\"company\":\"Company\",\"jobPosition\":\"Job Position\",\"mobile\":\"Mobile\",\"username\":"
                                        + "\"janedoe\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"role\":\"Role\"}"));
    }

    /**
     * Method under test: {@link UserController#getUserInfo(String, Authentication)}
     */
    @Test
    void testGetUserInfo2() throws Exception {
        when(jwtUserService.getJwtUserByUsername((String) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        User user = new User();
        user.setAvatar("Avatar");
        user.setBirthDate("2020-03-01");
        user.setCity("Oxford");
        user.setCompany("Company");
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(123L);
        user.setJobPosition("Job Position");
        user.setLastName("Doe");
        user.setMobile("Mobile");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{username}", "janedoe");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User not found by usernamejanedoe"));
    }

    /**
     * Method under test: {@link UserController#getUserInfo(String, Authentication)}
     */
    @Test
    void testGetUserInfo3() throws Exception {
        User user = new User();
        user.setAvatar("Avatar");
        user.setBirthDate("2020-03-01");
        user.setCity("Oxford");
        user.setCompany("Company");
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(123L);
        user.setJobPosition("Job Position");
        user.setLastName("Doe");
        user.setMobile("Mobile");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        when(jwtUserService.getJwtUserByUsername((String) any())).thenReturn(user);

        User user1 = new User();
        user1.setAvatar("Avatar");
        user1.setBirthDate("2020-03-01");
        user1.setCity("Oxford");
        user1.setCompany("Company");
        user1.setCountry("GB");
        user1.setEmail("jane.doe@example.org");
        user1.setFirstName("Jane");
        user1.setId(123L);
        user1.setJobPosition("Job Position");
        user1.setLastName("Doe");
        user1.setMobile("Mobile");
        user1.setPassword("iloveyou");
        user1.setRole("admin");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthDate\":\"2020-03-01\",\"city\":\"Oxford\",\"country\":\"GB"
                                        + "\",\"avatar\":\"Avatar\",\"company\":\"Company\",\"jobPosition\":\"Job Position\",\"mobile\":\"Mobile\",\"username\":"
                                        + "\"janedoe\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"role\":\"Role\"}"));
    }

    /**
     * Method under test: {@link UserController#getUserInfo(String, Authentication)}
     */
    @Test
    void testGetUserInfo4() throws Exception {
        User user = new User();
        user.setAvatar("Avatar");
        user.setBirthDate("2020-03-01");
        user.setCity("Oxford");
        user.setCompany("Company");
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(123L);
        user.setJobPosition("Job Position");
        user.setLastName("Doe");
        user.setMobile("Mobile");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        when(jwtUserService.getJwtUserByUsername((String) any())).thenReturn(user);

        User user1 = new User();
        user1.setAvatar("Avatar");
        user1.setBirthDate("2020-03-01");
        user1.setCity("Oxford");
        user1.setCompany("Company");
        user1.setCountry("GB");
        user1.setEmail("jane.doe@example.org");
        user1.setFirstName("Jane");
        user1.setId(123L);
        user1.setJobPosition("Job Position");
        user1.setLastName("Doe");
        user1.setMobile("Mobile");
        user1.setPassword("iloveyou");
        user1.setRole("Role");
        user1.setUsername("?");
        Optional<User> ofResult = Optional.of(user1);
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"firstName\":null,\"lastName\":null,\"birthDate\":null,\"city\":null,\"country\":null,\"avatar\""
                                        + ":null,\"company\":null,\"jobPosition\":null,\"mobile\":null,\"username\":null,\"email\":null,\"password\":null"
                                        + ",\"role\":null}"));
    }

    /**
     * Method under test: {@link UserController#uploadFile(MultipartFile)}
     */
    @Test
    void testUploadFile() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/users/batch");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("multipartFile", String.valueOf((Object) null));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test: {@link UserController#userEndpoint(Authentication)}
     */
    @Test
    void testUserEndpoint() throws Exception {
        User user = new User();
        user.setAvatar("Avatar");
        user.setBirthDate("2020-03-01");
        user.setCity("Oxford");
        user.setCompany("Company");
        user.setCountry("GB");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(123L);
        user.setJobPosition("Job Position");
        user.setLastName("Doe");
        user.setMobile("Mobile");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/me");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthDate\":\"2020-03-01\",\"city\":\"Oxford\",\"country\":\"GB"
                                        + "\",\"avatar\":\"Avatar\",\"company\":\"Company\",\"jobPosition\":\"Job Position\",\"mobile\":\"Mobile\",\"username\":"
                                        + "\"janedoe\",\"email\":\"jane.doe@example.org\",\"password\":\"iloveyou\",\"role\":\"Role\"}"));
    }

    /**
     * Method under test: {@link UserController#userEndpoint(Authentication)}
     */
    @Test
    void testUserEndpoint2() throws Exception {
        when(userService.findAuthenticatedUser((Authentication) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/me");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    /**
     * Method under test: {@link UserController#userEndpoint(Authentication)}
     */
    @Test
    void testUserEndpoint3() throws Exception {
        when(userService.findAuthenticatedUser((Authentication) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/me");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Something wrong in the server"));
    }
}

