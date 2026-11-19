package com.mycompany.prog5121_part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Message {
    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String flag; // "Sent", "Stored", "Disregard"

    public Message() {}

    public Message(int messageNumber, String recipientCell, String messageText) {
        this(messageNumber, "SystemUser", recipientCell, messageText, "Sent");
    }

    public Message(int messageNumber, String sender, String recipientCell, String messageText, String flag) {
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.flag = flag;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell(String cellNumber) {
        if (cellNumber == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        String regex = "^\\+27[0-9]{9}$";
        if (Pattern.matches(regex, cellNumber)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength(String message) {
        if (message == null) {
            return "Message ready to send.";
        }
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = message.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            return "00:0:INVALIDHASH";
        }
        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

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

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.flag = "Sent";
                return "Message successfully sent.";
            case 2:
                this.flag = "Disregard";
                return "Press 0 to delete the message.";
            case 3:
                this.flag = "Stored";
                storeMessageJSON();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // Stores a single JSON object per line in messages.json for reliable line-based parsing
    public void storeMessageJSON() {
        String escapedText = escapeJson(messageText);
        String escapedSender = escapeJson(sender);
        String jsonLine = "{\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"sender\":\"" + escapedSender + "\","
                + "\"recipientCell\":\"" + escapeJson(recipientCell) + "\","
                + "\"messageText\":\"" + escapedText + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                + "\"flag\":\"" + escapeJson(flag) + "\"}";

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(jsonLine + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error storing message to JSON file: " + e.getMessage());
        }
    }

    // Reads messages.json into an array of Message objects
    // Reference: Standard Java file I/O & JSON string parsing pattern
    public static Message[] loadStoredMessagesFromJSON(String filePath) {
        List<Message> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int idx = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !line.startsWith("{")) {
                    continue;
                }
                String id = extractJsonField(line, "messageID");
                String numStr = extractJsonField(line, "messageNumber");
                String snd = extractJsonField(line, "sender");
                String rec = extractJsonField(line, "recipientCell");
                String txt = extractJsonField(line, "messageText");
                String hsh = extractJsonField(line, "messageHash");
                String flg = extractJsonField(line, "flag");

                int msgNum = idx++;
                if (numStr != null && !numStr.isEmpty()) {
                    try {
                        msgNum = Integer.parseInt(numStr.trim());
                    } catch (NumberFormatException ignored) {
                    }
                }

                Message m = new Message(
                        msgNum,
                        snd != null ? snd : "Unknown Sender",
                        rec != null ? rec : "",
                        txt != null ? txt : "",
                        flg != null ? flg : "Stored"
                );
                if (id != null && !id.isEmpty()) {
                    m.setMessageID(id);
                }
                if (hsh != null && !hsh.isEmpty()) {
                    m.messageHash = hsh;
                }
                loaded.add(m);
            }
        } catch (IOException e) {
            // File may not exist yet on first run
        }
        return loaded.toArray(new Message[0]);
    }

    private static String extractJsonField(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) {
            return null;
        }
        int start = idx + search.length();
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        if (start >= json.length()) {
            return null;
        }
        if (json.charAt(start) == '"') {
            start++;
            StringBuilder sb = new StringBuilder();
            boolean escaping = false;
            for (int i = start; i < json.length(); i++) {
                char c = json.charAt(i);
                if (escaping) {
                    if (c == 'n') sb.append('\n');
                    else if (c == 'r') sb.append('\r');
                    else if (c == 't') sb.append('\t');
                    else sb.append(c);
                    escaping = false;
                } else if (c == '\\') {
                    escaping = true;
                } else if (c == '"') {
                    break;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            int end = start;
            while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
                end++;
            }
            return json.substring(start, end).trim();
        }
    }

    private static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    // Part 3 helper methods operating on arrays
    public static String getLongestMessage(Message[] messages) {
        if (messages == null || messages.length == 0) {
            return "No messages available.";
        }
        Message longest = messages[0];
        for (Message m : messages) {
            if (m != null && m.getMessageText() != null
                    && m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    public static String searchByMessageID(String targetID, Message[] messages) {
        if (messages == null || targetID == null) {
            return "Message not found.";
        }
        for (Message m : messages) {
            if (m != null && targetID.equals(m.getMessageID())) {
                return "Recipient: " + m.getRecipientCell() + " | Message: " + m.getMessageText();
            }
        }
        return "Message not found.";
    }

    public static List<String> searchMessagesByRecipient(String recipient, Message[] messages) {
        List<String> matches = new ArrayList<>();
        if (messages == null || recipient == null) {
            return matches;
        }
        for (Message m : messages) {
            if (m != null && recipient.equals(m.getRecipientCell())) {
                matches.add(m.getMessageText());
            }
        }
        return matches;
    }

    public static String deleteMessageByHash(String targetHash, List<Message> messageList) {
        if (messageList == null || targetHash == null) {
            return "Message hash not found.";
        }
        for (int i = 0; i < messageList.size(); i++) {
            Message m = messageList.get(i);
            if (m != null && targetHash.equals(m.getMessageHash())) {
                String removedText = m.getMessageText();
                messageList.remove(i);
                return "Message: \"" + removedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String buildSentMessagesReport(Message[] sentMessages) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SENT MESSAGES REPORT ===\n");
        if (sentMessages == null || sentMessages.length == 0) {
            sb.append("No sent messages to display.\n");
            return sb.toString();
        }
        for (Message m : sentMessages) {
            if (m != null) {
                sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
                sb.append("Recipient: ").append(m.getRecipientCell()).append("\n");
                sb.append("Message: ").append(m.getMessageText()).append("\n");
                sb.append("----------------------------------------\n");
            }
        }
        return sb.toString().trim();
    }

    public String printMessages() {
        return "----------------------------------------\n"
                + "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Sender: " + sender + "\n"
                + "Recipient: " + recipientCell + "\n"
                + "Message: " + messageText + "\n"
                + "----------------------------------------";
    }

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
}