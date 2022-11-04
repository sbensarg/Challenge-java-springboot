package com.example.challengejavaspringboot.auth;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.challengejavaspringboot.entity.User;
import com.example.challengejavaspringboot.security.JwtTokenUtil;
import com.example.challengejavaspringboot.security.JwtUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthApi.class})
@ExtendWith(SpringExtension.class)
class AuthApiTest {
    @Autowired
    private AuthApi authApi;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Method under test: {@link AuthApi#login(AuthRequest)}
     */
    @Test
    void testLogin() throws Exception {
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authApi).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    /**
     * Method under test: {@link AuthApi#login(AuthRequest)}
     */
    @Test
    void testLogin2() throws Exception {
        when(jwtTokenUtil.generateAccessToken((JwtUserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken(new JwtUserDetails(new User()), "Credentials"));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authApi)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accessToken\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link AuthApi#login(AuthRequest)}
     */
    @Test
    void testLogin3() throws Exception {
        when(jwtTokenUtil.generateAccessToken((JwtUserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any())).thenReturn(null);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authApi).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    /**
     * Method under test: {@link AuthApi#login(AuthRequest)}
     */
    @Test
    void testLogin4() throws Exception {
        when(jwtTokenUtil.generateAccessToken((JwtUserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken(new JwtUserDetails(new User()), "Credentials"));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("?");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authApi).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AuthApi#login(AuthRequest)}
     */
    @Test
    void testLogin5() throws Exception {
        when(jwtTokenUtil.generateAccessToken((JwtUserDetails) any())).thenThrow(new BadCredentialsException("?"));
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken(new JwtUserDetails(new User()), "Credentials"));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("iloveyou");
        authRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authApi).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }
}

