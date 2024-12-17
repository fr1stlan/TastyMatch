package com.cao.tastymatch;

public class Users {

    private String nickname;
    private String login;
    private String password;
    private String image;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Users() {}

    public Users(String nickname, String login, String password, String image) {
        this.nickname = nickname;
        this.login = login;
        this.password = password;
        this.image = image;
    }
}
