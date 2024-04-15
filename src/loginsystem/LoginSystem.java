/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package loginsystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Login system
 * @author michael.roy-diclemen
 */
public class LoginSystem {

    private File userFile;
    private static ArrayList<UserInfo> users;
    private int logingin;
    private int saveNum;
    private String delimiter = "|";

    /**
     * Encrypt the password
     *
     * @param oPass
     * @return the encrypted password
     */
    public String encrypt(String oPass) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        md.update(oPass.getBytes());
        byte byteData[] = md.digest();
        String encryptedPass = "";
        for (int i = 0; i < byteData.length; ++i) {
            encryptedPass += (Integer.toHexString((byteData[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return encryptedPass;
    }

    /**
     * Register a user and save them to a file
     *
     * @param user
     * @return a number that shows whether registration was successful or not
     */
    public int register(UserInfo user) {
        // Get user info
        String userPass = user.getPassword();
        String userName = user.getUsername();
        String userAge = user.getAge();
        String userPhone = user.getPhoneNum();
        String userEmail = user.getEmail();

        // call saveUser with all the user info
        return saveUser(userName, userPass, userAge, userPhone, userEmail);

    }

    /**
     * Save a user to a text file
     *
     * @param name
     * @param pass
     * @param age
     * @param phone
     * @param mail
     * @return a number indicating whether the user could be saved and the
     * reason
     */
    public int saveUser(String name, String pass, String age, String phone, String mail) {

        // Create a file that holds all the users
        userFile = new File("user.txt");

        // Encrypt the password
        String encryptedPass = encrypt(pass);

        // Write into the user file
        try (PrintWriter pw = new PrintWriter(new FileWriter(userFile, true))) {

            // Only write the user into the file if it has a unique name and a strong password
            if ((isUniqueName(name) == true) && (isStrongPass(pass) == true)) {
                pw.println(name + delimiter + encryptedPass + delimiter + age + delimiter + phone + delimiter + mail);
                pw.close();
                saveNum = 0;
            } else if (isUniqueName(name) == false) {
                saveNum = 1;
            } else if (isStrongPass(pass) == false) {
                saveNum = 2;
            } else if (userFile == null) {
                pw.println(name + delimiter + encryptedPass + delimiter + age + delimiter + phone + delimiter + mail);
                pw.close();
                saveNum = 0;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveNum;

    }

    /**
     * Loading all users from the text file into an array list
     */
    public void loadUser() {

        // Array list that holds all the user objects
        users = new ArrayList<>();

        File readUserFile = new File("user.txt");
        // Read the user file
        try (Scanner scanner = new Scanner(readUserFile)) {
            while (scanner.hasNextLine()) {
                String oneUser = scanner.nextLine();

                // Split each line at the delimiter
                String[] userParts = oneUser.split("\\" + delimiter);

                if (userParts.length == 5) {
                    String name = userParts[0];
                    String pass = userParts[1];
                    String age = userParts[2];
                    String phoneNum = userParts[3];
                    String mail = userParts[4];

                    // Add each user from the file as an instance of UserInfo into the array list
                    UserInfo newUser = new UserInfo(name, pass, age, phoneNum, mail);
                    users.add(newUser);
                } else {
                    System.out.println("Invalid user data format: " + oneUser);
                }

            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking if the registered name is unique or not
     *
     * @param name
     * @return true or false to if it is a unique name
     */
    public boolean isUniqueName(String name) {

        // Check if the username already exists in the array list
        if (users == null) {
            return true;
        } else {
            for (UserInfo user : users) {
                System.out.println(user.getUsername());
                System.out.println(name);
                if (user.getUsername().equals(name)) {
                    // If it does exist, return false
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the password registered is strong or not
     * 
     * @param pass
     * @return true or false on if it's a strong password
     */
    public boolean isStrongPass(String pass) {
        File weakPassFile = new File("dictbadpass.txt");
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean num = false;
        boolean special = false;
        
        if (pass.length() < 6){
            return false;
        }
        
        for (int i = 0; i < pass.length(); i++){
            char passCh = pass.charAt(i);
            if (Character.isLowerCase(passCh)){
                lowerCase = true;
            } else if (Character.isUpperCase(passCh)){
                upperCase = true;
            } else if (Character.isDigit(passCh)){
                num = true;
            } else {
                special = true;
            }
        }
        

        
        // Read the file with weak passwords
        try {
            Scanner s = new Scanner(weakPassFile);
            while (s.hasNextLine()) {
                String weakPass = s.nextLine();
                
                // If password entered by user is found in the weak password file, then don't register the user
                if (pass.equals(weakPass)) {
                    return false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        
        if (lowerCase && upperCase && num && special == true){
            return true;
        } else {
            return false;
        }
               
        //return true;
    }

    /**
     * Login a user
     *
     * @param loginName
     * @param loginPass
     * @return an integer indicating whether or not login is successful and the
     * reason if it's not
     */
    public int loginSys(String loginName, String loginPass) {
        int registered = 0;
        int wrongPass = 0;
        
        // Encrypt the password to match with array and file
        String encryptedPass = encrypt(loginPass);

        // Check if user exist
        if (users == null) {
            logingin = 2;
        } else {
            for (UserInfo user : users) {               
                if ((user.getUsername().equals(loginName)) && (user.getPassword().equals(encryptedPass))) {
                    registered++;
                } else if ((user.getUsername().equals(loginName)) && (!user.getPassword().equals(encryptedPass))) {
                    wrongPass++;
                }
            }
        }

        if ((registered > 0) && (wrongPass < 1)) {
            // Is registered
            logingin = 0;
        } else if (wrongPass > 0) {
            // Registered but has the wrong pass
            logingin = 1;
        } else {
            // Not registered
            logingin = 2;
        }

        return logingin;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        UserInfo user1 = new UserInfo("PP", "@@QQWWRRww", "5", "Lily", "1234");
        UserInfo user2 = new UserInfo("4", "12!@qwWq", "4", "Bob", "ILoveBob");
        UserInfo user3 = new UserInfo("8", "!!22qwwQQ", "9", "hi", "123456");
        UserInfo user4 = new UserInfo("9", "!!QQw@22", "8", "Jeff", "fdhalhgsjfdgna");
        LoginSystem login = new LoginSystem();
        login.loadUser();
        login.register(user1);
        login.register(user2);
        login.register(user3);
        login.register(user4);

        if(login.loginSys("Bob", "ILoveBob") == 0){
            System.out.println("logged in");
        }
        System.out.println("Here are all registrants: " + users);

    }

}
