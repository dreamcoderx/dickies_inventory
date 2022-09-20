package com.btw.guess.sql;

public class ModUser {
    int user_id;
    String user_name;
    String user_email;
    String user_password;
    int user_type;
    String user_type_desc;

    public String getUser_type_desc() {
        return user_type_desc;
    }

    public void setUser_type_desc(String user_type_desc) {
        this.user_type_desc = user_type_desc;
    }

    public ModUser() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
