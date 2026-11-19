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

        String currentSender = firstName + " " + lastName;
        System.out.println("\nWelcome to QuickChat.");

        // Dynamic lists updated during session and converted to arrays when needed
        List<Message> sentList = new ArrayList<>();
        List<Message> disregardedList = new ArrayList<>();
        List<Message> storedList = new ArrayList<>();
        List<String> hashList = new ArrayList<>();
        List<String> idList = new ArrayList<>();

        // Load any previously stored JSON messages into storedList
        Message[] initialStored = Message.loadStoredMessagesFromJSON("messages.json");
        for (Message m : initialStored) {
            storedList.add(m);
            hashList.add(m.getMessageHash());
            idList.add(m.getMessageID());
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Select an option (1-4): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("How many messages would you like to enter? ");
                    int numMessages;
                    try {
                        numMessages = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                        break;
                    }

                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                        System.out.print("Enter Recipient Cell Number: ");
                        String recipient = scanner.nextLine().trim();

                        Message validator = new Message();
                        String cellCheck = validator.checkRecipientCell(recipient);
                        if (!cellCheck.equals("Cell phone number successfully captured.")) {
                            System.out.println(cellCheck);
                            i--;
                            continue;
                        }

                        System.out.print("Enter Message Text (max 250 characters): ");
                        String text = scanner.nextLine();

                        String lengthCheck = validator.checkMessageLength(text);
                        if (!lengthCheck.equals("Message ready to send.")) {
                            System.out.println(lengthCheck);
                            i--;
                            continue;
                        }

                        Message msg = new Message(i, currentSender, recipient, text, "Pending");

                        System.out.println("\nChoose an action for this message:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");
                        System.out.print("Option: ");

                        int actionChoice;
                        try {
                            actionChoice = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid option. Message disregarded.");
                            actionChoice = 2;
                        }

                        String actionResult = msg.SentMessage(actionChoice);
                        System.out.println(actionResult);

                        hashList.add(msg.getMessageHash());
                        idList.add(msg.getMessageID());

                        if (actionChoice == 1) {
                            sentList.add(msg);
                            totalMessagesSent++;
                            System.out.println("\nGenerated Message Details:");
                            System.out.println(msg.printMessages());
                        } else if (actionChoice == 2) {
                            disregardedList.add(msg);
                        } else if (actionChoice == 3) {
                            storedList.add(msg);
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

                case 4:
                    // Refresh stored messages array from JSON file as required
                    Message[] refreshedStored = Message.loadStoredMessagesFromJSON("messages.json");
                    storedList.clear();
                    for (Message m : refreshedStored) {
                        storedList.add(m);
                    }

                    Message[] sentArray = sentList.toArray(new Message[0]);
                    Message[] disregardedArray = disregardedList.toArray(new Message[0]);
                    Message[] storedArray = storedList.toArray(new Message[0]);
                    String[] messageHashArray = hashList.toArray(new String[0]);
                    String[] messageIDArray = idList.toArray(new String[0]);

                    System.out.println("\n--- STORED MESSAGES MENU ---");
                    System.out.println("a) Display sender and recipient of all stored messages");
                    System.out.println("b) Display the longest stored message");
                    System.out.println("c) Search for a message ID and display recipient and message");
                    System.out.println("d) Search for all messages stored/sent for a particular recipient");
                    System.out.println("e) Delete a message using the message hash");
                    System.out.println("f) Display full report of sent messages");
                    System.out.print("Choose an option (a-f): ");
                    String subChoice = scanner.nextLine().trim().toLowerCase();

                    switch (subChoice) {
                        case "a":
                            if (storedArray.length == 0) {
                                System.out.println("No stored messages found.");
                            } else {
                                for (Message m : storedArray) {
                                    System.out.println("Sender: " + m.getSender() + " | Recipient: " + m.getRecipientCell());
                                }
                            }
                            break;

                        case "b":
                            System.out.println("Longest stored message: " + Message.getLongestMessage(storedArray));
                            break;

                        case "c":
                            System.out.print("Enter Message ID to search: ");
                            String searchId = scanner.nextLine().trim();
                            Message[] allKnown = combineMessages(sentArray, storedArray, disregardedArray);
                            System.out.println(Message.searchByMessageID(searchId, allKnown));
                            break;

                        case "d":
                            System.out.print("Enter recipient number: ");
                            String targetRecipient = scanner.nextLine().trim();
                            Message[] combined = combineMessages(sentArray, storedArray, disregardedArray);
                            List<String> found = Message.searchMessagesByRecipient(targetRecipient, combined);
                            if (found.isEmpty()) {
                                System.out.println("No messages found for " + targetRecipient);
                            } else {
                                for (String f : found) {
                                    System.out.println(f);
                                }
                            }
                            break;

                        case "e":
                            System.out.print("Enter Message Hash to delete: ");
                            String targetHash = scanner.nextLine().trim();
                            String delResult = Message.deleteMessageByHash(targetHash, storedList);
                            System.out.println(delResult);
                            break;

                        case "f":
                            System.out.println(Message.buildSentMessagesReport(sentArray));
                            break;

                        default:
                            System.out.println("Invalid stored messages option.");
                    }
                    break;

                default:
                    System.out.println("Invalid menu option. Please select 1, 2, 3, or 4.");
            }
        }

        scanner.close();
    }

    private static Message[] combineMessages(Message[] a, Message[] b, Message[] c) {
        List<Message> all = new ArrayList<>();
        if (a != null) for (Message m : a) all.add(m);
        if (b != null) for (Message m : b) all.add(m);
        if (c != null) for (Message m : c) all.add(m);
        return all.toArray(new Message[0]);
    }
}