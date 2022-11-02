package com.example.challengejavaspringboot.controller;

import com.example.challengejavaspringboot.entities.User;
import com.example.challengejavaspringboot.services.JwtUserService;
import com.example.challengejavaspringboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUserService jwtUserService;
    @Autowired
    private ServletContext context;

    /*Endpoint 1 -----------------------------------------------------------------------------------------*/

    @Operation(summary = "Generate users json file, you must specify count number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)})
    @GetMapping("/generate")
    public void createJson(@Parameter(description = "count element of users to be generated")@RequestParam(name = "count") @Valid int count,
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

    /*Endpoint 2 -----------------------------------------------------------------------------------------*/

    @Operation(summary = "Upload of user file and creation of users in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Empty File",
                    content = @Content),
            @ApiResponse(responseCode = "415", description = "File Type Not Allowed",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)})
    @PostMapping(path = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@Parameter(description = "json file of users")@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload",HttpStatus.NOT_FOUND);
        }
        int ret = userService.saveDataFromUploadfile(multipartFile);
        ObjectNode objectNode = null;
        if (ret != -1) {
            long total = userService.getTotalRecords();
            ObjectMapper mapper = new ObjectMapper();
            objectNode = mapper.createObjectNode();
            objectNode.put("total_nbr_records", total);
            objectNode.put("imported", total - ret);
            objectNode.put("not_imported", ret);
            return new ResponseEntity<>(objectNode, HttpStatus.OK);
        }
        return new ResponseEntity<>("File Type Not Allowed", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /*Endpoint 4 -----------------------------------------------------------------------------------------*/

    @Operation(summary = "Consult my profile {you must your JWT token for secure access}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public ResponseEntity<Object> userEndpoint(Authentication authentication) {
        Optional<User> u = this.userService.findAuthenticatedUser(authentication);
        if (u.isPresent())
            return new ResponseEntity<>(u, HttpStatus.OK);
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    /*Endpoint 5 -----------------------------------------------------------------------------------------*/

    @Operation(summary = "Consult my profile or consult the profiles of other users if the user has a role admin {you must your JWT token for secure access}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid parameters supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{username}")
    public ResponseEntity<Object> getUserInfo(@Parameter(description = "username of user to be retrieved")@PathVariable(value="username") @Valid String username, Authentication authentication) {
        try
        {
            User found = null;
            Optional<User> u = this.userService.findAuthenticatedUser(authentication);
            if (u.get().getRole().equals("admin"))
            {
                found = this.jwtUserService.getJwtUserByUsername(username);
                if (found != null)
                    return new ResponseEntity<>(found, HttpStatus.OK);
            }
            else if (u.get().getUsername().equals(username))
            {
                found = this.jwtUserService.getJwtUserByUsername(username);
                if (found != null)
                    return new ResponseEntity<>(found, HttpStatus.OK);
            }
        }
        catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
