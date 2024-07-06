package com.example.roombookingsys.model;

import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "user_database")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "user_id")
    private  int userID;
    //@Column(unique = true,nullable = false)
    private String email;
    //@Column(nullable = false)
    private String password;
    //@Column(nullable = false)
    private String name;


    public Account(){}

    public Account( String email, String password, String name)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        //this.userid = userid;
    }


    public int getUserid()
    {
        return userID;
    }
    public String getEmail()
    {
        return this.email;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getName()
    {
        return name;
    }

    public void setUserid(int userid)
    {
        this.userID = userid;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setName(String name)
    {
        this.name = name;
    }
}
