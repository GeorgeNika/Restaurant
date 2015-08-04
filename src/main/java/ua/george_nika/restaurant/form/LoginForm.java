package ua.george_nika.restaurant.form;

import java.io.Serializable;


public class LoginForm implements Serializable {

    private String j_username;
    private String j_password;

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String login) {
        this.j_username = login;
    }

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String password) {
        this.j_password = password;
    }

}
