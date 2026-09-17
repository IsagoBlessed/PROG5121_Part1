package com.mycompany.prog5121_part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
private Login login;

@BeforeEach
public void setUp() {
login = new Login("Kyle", "Smith");
}

@Test
public void testUsernameCorrectlyFormatted() {
assertTrue(login.checkUserName("kyl_1"));
}

@Test
public void testUsernameIncorrectlyFormatted() {
assertFalse(login.checkUserName("kyle!!!!!!"));
}

@Test
public void testPasswordComplexitySuccess() {
assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
}

@Test
public void testPasswordComplexityFailure() {
assertFalse(login.checkPasswordComplexity("password"));
}

@Test
public void testCellPhoneNumberCorrectlyFormatted() {
assertTrue(login.checkCellPhoneNumber("+27838968976"));
}

@Test
public void testCellPhoneNumberIncorrectlyFormatted() {
assertFalse(login.checkCellPhoneNumber("08966553"));
}

@Test
public void testLoginSuccessful() {
login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
}

@Test
public void testLoginFailed() {
login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
assertFalse(login.loginUser("kyl_1", "wrongPassword"));
}
}