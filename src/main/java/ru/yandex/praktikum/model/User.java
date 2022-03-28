package ru.yandex.praktikum.model;
import org.apache.commons.lang3.RandomStringUtils;

public class User {

    public String email;
    public String password;
    public String name;


    public User (String email, String password, String userName){
        this.email=email;
        this.password=password;
        this.name=userName;

    }

    public User () {}

    public static User getRandom(){
        String email = RandomStringUtils.randomAlphabetic(10)+"@"+ RandomStringUtils.randomAlphabetic(10)+".ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email,password,name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
