package com.example.challengejavaspringboot.entities;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User  {
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

}
