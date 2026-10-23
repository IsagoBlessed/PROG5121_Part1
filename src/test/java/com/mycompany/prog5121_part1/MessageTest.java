package com.mycompany.prog5121_part1;
//Unit tests for message validation logic

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
private Message message;

@BeforeEach
public void setUp() {
message = new Message();
}

@Test
public void testMessageLengthSuccess() {
String testMsg = "Hi Mike, can you join us for dinner tonight?";
assertEquals("Message ready to send.", message.checkMessageLength(testMsg));
}

@Test
public void testMessageLengthFailure() {
StringBuilder longText = new StringBuilder();
for (int i = 0; i < 260; i++) {
longText.append("a");
}
String expected = "Message exceeds 250 characters by 10; please reduce the size.";
assertEquals(expected, message.checkMessageLength(longText.toString()));
}

@Test
public void testRecipientCellSuccess() {
assertEquals("Cell phone number successfully captured.", message.checkRecipientCell("+27718693002"));
}

@Test
public void testRecipientCellFailure() {
String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
assertEquals(expected, message.checkRecipientCell("08575975889"));
}

@Test
public void testMessageHashCreation() {
// Test data matching brief: Message 1 -> "Hi Mike, can you join us for dinner tonight?"
// ID prefix 00, Msg num 0 -> 00:0:HITONIGHT
String hash = message.createMessageHashCustom("0012345678", 0, "Hi Mike, can you join us for dinner tonight?");
assertEquals("00:0:HITONIGHT", hash);
}

@Test
public void testMessageIDCreated() {
String id = message.generateMessageID();
assertNotNull(id);
assertEquals(10, id.length());
}

@Test
public void testSentMessageOptions() {
assertEquals("Message successfully sent.", message.SentMessage(1));
assertEquals("Press 0 to delete the message.", message.SentMessage(2));
assertEquals("Message successfully stored.", message.SentMessage(3));
}
}