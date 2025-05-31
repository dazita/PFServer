package model;

import structures.BinaryTree;
import com.google.gson.*;

public class User {

    private String username;
    private String password;
    private String email;
    private String location;
    private BinaryTree<Integer> listingIds;
    private String phoneNumber;


    public User(String email){
        this.email = email;
    }
    
    public static User fromJson(String json) throws IllegalArgumentException {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        user.listingIds = new BinaryTree<>(new IntegerComparator());
        return user;
    }

    public BinaryTree<Integer> getListingIds() {
        return listingIds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLocation() {
        return location;
    }
    
}