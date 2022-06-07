package com.example.etransect;

public class User {

    public String Name,Surname,Phone,Email,Address;

    public User(){
        //New user just signed in
    }

    public User(String name,String s_name,String phone,String email,String address){
        this.Name= name;
        this.Surname= s_name;
//        this.Password= password;
        this.Phone= phone;
        this.Email= email;
        this.Address= address;
    }
}
