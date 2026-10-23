package com.mycompany.prog5121_part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
private static int totalMessagesSent = 0;

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

System.out.print("Enter Cell Phone Number (+27...): ");
String cellNumber = scanner.nextLine();

String registrationResult = userLogin.registerUser(username, password, cellNumber, firstName, lastName);
System.out.println(registrationResult);

if (!registrationResult.equals("Username and password successfully captured.")) {
System.out.println("Registration failed. Exiting application.");
return;
}

System.out.println("\n=== LOGIN ===");
System.out.print("Enter Username: ");
String loginUsername = scanner.nextLine();

System.out.print("Enter Password: ");
String loginPassword = scanner.nextLine();

boolean loginSuccess = userLogin.loginUser(loginUsername, loginPassword);
System.out.println(userLogin.returnLoginStatus(loginSuccess));

if (!loginSuccess) {
System.out.println("Login failed. You must log in successfully to send messages.");
return;
}

// Welcome Message after successful login
System.out.println("\nWelcome to QuickChat.");

boolean running = true;
List<Message> sentMessagesList = new ArrayList<>();

while (running) {
System.out.println("\n--- MENU ---");
System.out.println("1) Send Messages");
System.out.println("2) Show recently sent messages");
System.out.println("3) Quit");
System.out.print("Select an option (1-3): ");

int choice;
try {
choice = Integer.parseInt(scanner.nextLine());
} catch (NumberFormatException e) {
System.out.println("Invalid input. Please enter a number.");
continue;
}

switch (choice) {
case 1:
System.out.print("How many messages would you like to enter? ");
int numMessages = Integer.parseInt(scanner.nextLine());

for (int i = 1; i <= numMessages; i++) {
System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

System.out.print("Enter Recipient Cell Number: ");
String recipient = scanner.nextLine();

Message tempMsg = new Message();
String cellCheck = tempMsg.checkRecipientCell(recipient);
if (!cellCheck.equals("Cell phone number successfully captured.")) {
System.out.println(cellCheck);
i--; // Re-try this message
continue;
}

System.out.print("Enter Message Text (max 250 characters): ");
String text = scanner.nextLine();

String lengthCheck = tempMsg.checkMessageLength(text);
if (!lengthCheck.equals("Message ready to send.")) {
System.out.println(lengthCheck);
i--; // Re-try this message
continue;
}

Message msg = new Message(i, recipient, text);

System.out.println("\nChoose an action for this message:");
System.out.println("1. Send Message");
System.out.println("2. Disregard Message");
System.out.println("3. Store Message to send later");
System.out.print("Option: ");
int actionChoice = Integer.parseInt(scanner.nextLine());

String actionResult = msg.SentMessage(actionChoice);
System.out.println(actionResult);

if (actionChoice == 1) {
sentMessagesList.add(msg);
totalMessagesSent++;
System.out.println("\nGenerated Message Details:");
System.out.println(msg.printMessages());
}
}

System.out.println("\nTotal messages sent in this session: " + totalMessagesSent);
break;

case 2:
System.out.println("Coming Soon.");
break;

case 3:
running = false;
System.out.println("Thank you for using QuickChat. Goodbye!");
break;

default:
System.out.println("Invalid menu option. Please select 1, 2, or 3.");
}
}

scanner.close();
}
}