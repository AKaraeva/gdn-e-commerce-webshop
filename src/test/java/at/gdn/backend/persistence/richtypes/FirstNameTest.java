package at.gdn.backend.persistence.richtypes;

import at.gdn.backend.richtypes.Firstname;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstNameTest {

    @Test
    void ensureIsValidFirstNameTest(){
        assertTrue(Firstname.isValidFirstName.test("Aurelie"));
        assertTrue(Firstname.isValidFirstName.test("A".repeat(64)));
        assertTrue(Firstname.isValidFirstName.test("Aurelie M. L."));
        assertTrue(Firstname.isValidFirstName.test("Aurelie-Lisa"));
        assertTrue(Firstname.isValidFirstName.test("أوريلي")); //arabisch
        assertTrue(Firstname.isValidFirstName.test("Орели")); // russisch
        assertTrue(Firstname.isValidFirstName.test("Aurélie")); //dänisch
        assertTrue(Firstname.isValidFirstName.test("奧雷利")); //chinesisch
        assertTrue(Firstname.isValidFirstName.test("اوریلی")); //paschtu Afghanistan
        assertTrue(Firstname.isValidFirstName.test("اورلی")); //persisch
        assertTrue(Firstname.isValidFirstName.test("ਔਰੇਲੀ")); //punjabi Pakistan

    }

}