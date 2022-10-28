package com.example.challengejavaspringboot.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    public enum UserRole {
        admin,
        user
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long        id;
    private String      firstName;
    private String      lastName;
    private String        birthDate;
    private String      city;
    private String      country;
    private String      avatar;
    private String      company;
    private String      jobPosition;
    private String      mobile;
    private String      username;
    private String      email;
    private String      password;
    private UserRole      role;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String birthDate, String city, String country, String avatar, String company, String jobPosition, String mobile, String username, String email, String password, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.city = city;
        this.country = country;
        this.avatar = avatar;
        this.company = company;
        this.jobPosition = jobPosition;
        this.mobile = mobile;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String firstName, String lastName, String birthDate, String city, String country, String avatar, String company, String jobPosition, String mobile, String username, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.city = city;
        this.country = country;
        this.avatar = avatar;
        this.company = company;
        this.jobPosition = jobPosition;
        this.mobile = mobile;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
////    @Basic
////    @Column(name = "firstName")
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
////    @Basic
////    @Column(name = "lastName")
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
////    @Basic
////    @Column(name = "birthDate")
//    public String getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(String birthDate) {
//        this.birthDate = birthDate;
//    }
//
////    @Basic
////    @Column(name = "city")
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
////    @Basic
////    @Column(name = "country")
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
////    @Basic
////    @Column(name = "avatar")
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
////    @Basic
////    @Column(name = "company")
//    public String getCompany() {
//        return company;
//    }
//
//    public void setCompany(String company) {
//        this.company = company;
//    }
//
////    @Basic
////    @Column(name = "jobPosition")
//    public String getJobPosition() {
//        return jobPosition;
//    }
//
//    public void setJobPosition(String jobPosition) {
//        this.jobPosition = jobPosition;
//    }
//
////    @Basic
////    @Column(name = "mobile")
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
////    @Basic
////    @Column(name = "username")
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
////    @Basic
////    @Column(name = "email")
//    public String getEmail() {
//        return email;
//    }
//
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
////    @Basic
////    @Column(name = "password")
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
////    @Basic
////    @Enumerated(EnumType.STRING)
////    @Column(name = "role")
//    public UserRole getRole() {
//        return role;
//    }
//
//    public void setRole(UserRole role) {
//        this.role = role;
//    }
}
