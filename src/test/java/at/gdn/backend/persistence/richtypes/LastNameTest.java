package at.gdn.backend.persistence.richtypes;

import at.gdn.backend.richtypes.Lastname;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNameTest {

    @Test
    void ensureIsValidFirstNameTest(){
        assertTrue(Lastname.isValidLastName.test("Huber"));
        assertTrue(Lastname.isValidLastName.test("H".repeat(64)));
        assertTrue(Lastname.isValidLastName.test("ہیوبر")); //arabisch
        assertTrue(Lastname.isValidLastName.test("胡伯")); //kantonesisch
        assertTrue(Lastname.isValidLastName.test("Хубер")); //kirgisch
    }

}