package com.mycompany.prog5121_part1;

import java.util.Scanner;

public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
Login userLogin = new Login();

System.out.println("=== REGISTRATION ===");
System.out.print("Enter First Name: ");
String firstName = scanner.nextLine();

System.out.print("Enter Last Name: ");
String lastName = scanner.nextLine();

System.out.print("Enter Username: ");
String username = scanner.nextLine();

System.out.print("Enter Password: ");
String password = scanner.nextLine();

System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
String cellNumber = scanner.nextLine();

String registrationResult = userLogin.registerUser(username, password, cellNumber, firstName, lastName);
System.out.println(registrationResult);

// Repeat login prompt until valid registration occurs
if (!registrationResult.equals("Username and password successfully captured.")) {
System.out.println("Registration failed due to invalid input. Exiting program.");
return;
}

System.out.println("\n=== LOGIN ===");
System.out.print("Enter Username: ");
String loginUsername = scanner.nextLine();

System.out.print("Enter Password: ");
String loginPassword = scanner.nextLine();

boolean loginSuccess = userLogin.loginUser(loginUsername, loginPassword);
System.out.println(userLogin.returnLoginStatus(loginSuccess));

scanner.close();
}
}