package com.ahnaftn.eduportal;

public class HelperClass {
    String username, email, password, cpassword,sid;


    public HelperClass() {
    }

    public HelperClass(String username, String email, String password, String cpassword, String sid) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.cpassword = cpassword;
        this.sid=sid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
