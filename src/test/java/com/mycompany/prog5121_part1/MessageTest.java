package com.mycompany.prog5121_part1;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;

    @BeforeEach
    public void setUp() {
        m1 = new Message(1, "Developer", "+27834557896", "Did you get the cake?", "Sent");
        m1.setMessageID("1000000001");
        m1.setMessageHash("10:1:DIDCAKE");

        m2 = new Message(2, "Developer", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.", "Stored");
        m2.setMessageID("1000000002");
        m2.setMessageHash("10:2:WHERETIME");

        m3 = new Message(3, "Developer", "+27834484567",
                "Yohoooo, I am at your gate.", "Disregard");
        m3.setMessageID("1000000003");
        m3.setMessageHash("10:3:YOHOOOOGATE");

        m4 = new Message(4, "0838884567", "0838884567",
                "It is dinner time !", "Sent");
        m4.setMessageID("0838884567");
        m4.setMessageHash("08:4:ITTIME");

        m5 = new Message(5, "Developer", "+27838884567",
                "Ok, I am leaving without you.", "Stored");
        m5.setMessageID("1000000005");
        m5.setMessageHash("10:5:OKYOU");
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Message[] sentArray = { m1, m4 };
        String actualSent = sentArray[0].getMessageText() + ", " + sentArray[1].getMessageText();
        assertEquals("Did you get the cake?, It is dinner time !", actualSent);
    }

    @Test
    public void testDisplayLongestMessage() {
        Message[] msgs1to4 = { m1, m2, m3, m4 };
        String longest = Message.getLongestMessage(msgs1to4);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByMessageID() {
        Message[] allMsgs = { m1, m2, m3, m4, m5 };
        String result = Message.searchByMessageID("0838884567", allMsgs);
        assertEquals("Recipient: 0838884567 | Message: It is dinner time !", result);
    }

    @Test
    public void testSearchMessagesByRecipient() {
        Message[] allMsgs = { m1, m2, m3, m4, m5 };
        List<String> matches = Message.searchMessagesByRecipient("+27838884567", allMsgs);
        assertEquals(2, matches.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", matches.get(0));
        assertEquals("Ok, I am leaving without you.", matches.get(1));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        List<Message> list = new ArrayList<>();
        list.add(m1);
        list.add(m2);
        list.add(m5);

        String result = Message.deleteMessageByHash(m2.getMessageHash(), list);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
        assertEquals(2, list.size());
    }

    @Test
    public void testDisplayReport() {
        Message[] sentArray = { m1, m4 };
        String report = Message.buildSentMessagesReport(sentArray);
        assertTrue(report.contains("Message Hash: 10:1:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message Hash: 08:4:ITTIME"));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time !"));
    }
}