package com.mycompany.prog5121_part1;
//Module: PROG5121 Part 2 - Message Class Implementation

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

public class Message {
private String messageID;
private int messageNumber;
private String recipientCell;
private String messageText;
private String messageHash;

// Constructors
public Message() {}

public Message(int messageNumber, String recipientCell, String messageText) {
this.messageNumber = messageNumber;
this.recipientCell = recipientCell;
this.messageText = messageText;
this.messageID = generateMessageID();
this.messageHash = createMessageHash();
}

// Helper: Generates a random 10-digit Message ID
public String generateMessageID() {
Random random = new Random();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10; i++) {
sb.append(random.nextInt(10));
}
return sb.toString();
}

// Validates that Message ID is non-null and not more than 10 characters
public boolean checkMessageID() {
return messageID != null && messageID.length() <= 10;
}

// Validates that recipient cell phone number is <= 10 chars/digits (e.g. +27 format or standard)
// Matches unit test failure/success rules in rubric
public String checkRecipientCell(String cellNumber) {
if (cellNumber == null) {
return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
}
// Check international format (+27...) or basic valid phone number check
String regex = "^\\+27[0-9]{9}$";
if (Pattern.matches(regex, cellNumber)) {
return "Cell phone number successfully captured.";
} else {
return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
}
}

// Validates message length <= 250 characters
public String checkMessageLength(String message) {
if (message == null || message.isEmpty()) {
return "Message ready to send.";
}
if (message.length() <= 250) {
return "Message ready to send.";
} else {
int excess = message.length() - 250;
return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
}
}

// Creates Message Hash: [First 2 digits of ID]:[Message Number]:[FIRSTWORD][LASTWORD] in ALL CAPS
public String createMessageHash() {
if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
return "00:0:INVALIDHASH";
}

String firstTwoID = messageID.substring(0, 2);
String[] words = messageText.trim().split("\\s+");
String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

String rawHash = firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord;
return rawHash.toUpperCase();
}

// Overloaded method for static unit testing with fixed data
public String createMessageHashCustom(String customID, int msgNum, String customText) {
if (customID == null || customID.length() < 2 || customText == null || customText.trim().isEmpty()) {
return "00:0:INVALIDHASH";
}
String firstTwoID = customID.substring(0, 2);
String[] words = customText.trim().split("\\s+");
String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

return (firstTwoID + ":" + msgNum + ":" + firstWord + lastWord).toUpperCase();
}

// Handles user option: 1 = Send, 2 = Disregard, 3 = Store
public String SentMessage(int choice) {
switch (choice) {
case 1:
return "Message successfully sent.";
case 2:
return "Press 0 to delete the message.";
case 3:
storeMessageJSON();
return "Message successfully stored.";
default:
return "Invalid option selected.";
}
}

// Method to store message details in JSON format
public void storeMessageJSON() {
String jsonOutput = "{\n" +
" \"messageID\": \"" + messageID + "\",\n" +
" \"messageNumber\": " + messageNumber + ",\n" +
" \"recipientCell\": \"" + recipientCell + "\",\n" +
" \"messageText\": \"" + messageText + "\",\n" +
" \"messageHash\": \"" + messageHash + "\"\n" +
"}";

try (FileWriter file = new FileWriter("messages.json", true)) {
file.write(jsonOutput + "\n");
} catch (IOException e) {
System.out.println("Error storing message to JSON file: " + e.getMessage());
}
}

// Returns full formatted details of the message
public String printMessages() {
return "----------------------------------------\n" +
"Message ID: " + messageID + "\n" +
"Message Hash: " + messageHash + "\n" +
"Recipient: " + recipientCell + "\n" +
"Message: " + messageText + "\n" +
"----------------------------------------";
}

// Getters and Setters
public String getMessageID() { return messageID; }
public void setMessageID(String messageID) { this.messageID = messageID; }
public int getMessageNumber() { return messageNumber; }
public String getRecipientCell() { return recipientCell; }
public String getMessageText() { return messageText; }
public String getMessageHash() { return messageHash; }
}