package com.mycompany.prog5121_part1;

import java.util.regex.Pattern;

public class Login {
private String registeredUsername;
private String registeredPassword;
private String registeredCellNumber;
private String firstName;
private String lastName;

// Default Constructor
public Login() {}

// Constructor with parameters
public Login(String firstName, String lastName) {
this.firstName = firstName;
this.lastName = lastName;
}

// Checks if username contains an underscore and is <= 5 characters
public boolean checkUserName(String username) {
return username != null && username.contains("_") && username.length() <= 5;
}

// Checks password complexity rules
public boolean checkPasswordComplexity(String password) {
if (password == null || password.length() < 8) {
return false;
}

boolean hasCapital = false;
boolean hasDigit = false;
boolean hasSpecial = false;

for (char c : password.toCharArray()) {
if (Character.isUpperCase(c)) hasCapital = true;
else if (Character.isDigit(c)) hasDigit = true;
else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
}

return hasCapital && hasDigit && hasSpecial;
}

// Regex-based cell phone checker (Attributed according to brief instructions)
// Reference: Regular Expression for International Phone Numbers in South Africa (+27)
public boolean checkCellPhoneNumber(String cellNumber) {
if (cellNumber == null) return false;
// Matches +27 followed by 9 digits (total length 12 characters, country code + 9 digits)
String regex = "^\\+27[0-9]{9}$";
return Pattern.matches(regex, cellNumber);
}

// Method to handle registration return messaging
public String registerUser(String username, String password, String cellNumber, String firstName, String lastName) {
this.firstName = firstName;
this.lastName = lastName;

if (!checkUserName(username)) {
return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
}

if (!checkPasswordComplexity(password)) {
return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
}

if (!checkCellPhoneNumber(cellNumber)) {
return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
}

this.registeredUsername = username;
this.registeredPassword = password;
this.registeredCellNumber = cellNumber;

return "Username and password successfully captured.";
}

// Verifies if login credentials match
public boolean loginUser(String username, String password) {
if (this.registeredUsername == null || this.registeredPassword == null) {
return false;
}
return this.registeredUsername.equals(username) && this.registeredPassword.equals(password);
}

// Returns status message for login attempt
public String returnLoginStatus(boolean isLoggedIn) {
if (isLoggedIn) {
return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
} else {
return "Username or password incorrect, please try again.";
}
}

// Getters and Setters
public String getFirstName() { return firstName; }
public void setFirstName(String firstName) { this.firstName = firstName; }

public String getLastName() { return lastName; }
public void setLastName(String lastName) { this.lastName = lastName; }
}