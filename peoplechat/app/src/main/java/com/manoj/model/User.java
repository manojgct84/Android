package com.manoj.model;

import java.util.Objects;

public class User {


    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;
    private String phoneNo;
    private String password;

    public User(String id, String username, String password, String phoneNo, String imageURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNo = phoneNo;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public User() {

    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (!phoneNo.equals(user.getPhoneNo())) {
            return false;
        }
        return phoneNo.equals(user.phoneNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNo);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", status='" + status + '\'' +
                ", search='" + search + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
