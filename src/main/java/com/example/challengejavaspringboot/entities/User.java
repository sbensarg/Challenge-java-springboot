package com.example.challengejavaspringboot.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
//@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames ={"username", "email"})})
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long        id;
    @Column(name = "firstName")
    private String      firstName;
    @Column(name = "lastName")
    private String      lastName;
    @Column(name = "birthDate")
    private String      birthDate;
    @Column(name = "city")
    private String      city;
    @Column(name = "country")
    private String      country;
    @Column(name = "avatar")
    private String      avatar;
    @Column(name = "company")
    private String      company;
    @Column(name = "jobPosition")
    private String      jobPosition;
    @Column(name = "mobile")
    private String      mobile;
    @Column(name = "username")
    private String      username;
    @Column(name = "email")
    private String      email;
    @Column(name = "password")
    private String      password;
    @Column(name = "role")
    private String      role;

    @Transient
    private int total_records;


    public User() {
    }

    public User(Long id, String firstName, String lastName, String birthDate, String city, String country, String avatar, String company, String jobPosition, String mobile, String username, String email, String password, String role) {
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

    public User(String firstName, String lastName, String birthDate, String city, String country, String avatar, String company, String jobPosition, String mobile, String username, String email, String password, String role) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
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
