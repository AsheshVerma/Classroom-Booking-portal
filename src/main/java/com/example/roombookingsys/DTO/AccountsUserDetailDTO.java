package com.example.roombookingsys.DTO;

public class AccountsUserDetailDTO
{
    private String email;
    private String name;
    private int userID;

    public AccountsUserDetailDTO(String email, int userID, String name)
    {
        this.email = email;
        //this.password = password;
        this.name = name;
        this.userID = userID;
    }

    public AccountsUserDetailDTO(){

    }

    public String getEmail()
    {
        return this.email;
    }

    public int getUserID() {
        return userID;
    }

    public String getName()
    {
        return name;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
