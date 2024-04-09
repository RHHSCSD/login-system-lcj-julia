/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;


/**
 * All information about each user
 * @author julialiu
 */
public class UserInfo {

    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private String age;

    /**
     * To construct a user
     * @param uPhone
     * @param uEmail
     * @param uAge
     * @param name
     * @param pass
     */
    public UserInfo(String name, String pass, String uAge, String uPhone, String uEmail) {
        username = name;
        password = pass;
        age = uAge;
        phoneNum = uPhone;
        email = uEmail;
    }

    /**
     * Get user's username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * get user's password
     * @return password
     */
    public String getPassword(){
        return password;
    }
    
    /**
     * Get user's age
     * @return age
     */
    public String getAge(){
        return age;
    }
    
    /**
     * Get user's phone number
     * @return phone number
     */
    public String getPhoneNum(){
        return phoneNum;
    }
    
    /**
     * Get user's email
     * @return email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Print out user's information
     * @return the user's information
     */
    public String toString() {
        return username + "|" + password + "|" + phoneNum + "|" + email + "|" + age;
    }

}
