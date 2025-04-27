package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Guri class.
 */
public class GuriTest {

    /**
     * Test the greet method with a valid name.
     */
    @Test
    public void testGreetWithName() {
        Guri guri = new Guri();
        String result = guri.greet("John");
        assertEquals("Hello, John!", result);
    }

    /**
     * Test the greet method with a null name.
     */
    @Test
    public void testGreetWithNullName() {
        Guri guri = new Guri();
        String result = guri.greet(null);
        assertEquals("Hello, World!", result);
    }

    /**
     * Test the greet method with an empty name.
     */
    @Test
    public void testGreetWithEmptyName() {
        Guri guri = new Guri();
        String result = guri.greet("");
        assertEquals("Hello, World!", result);
    }

    /**
     * Test the greet method with a name containing only whitespace.
     */
    @Test
    public void testGreetWithWhitespaceName() {
        Guri guri = new Guri();
        String result = guri.greet("   ");
        assertEquals("Hello, World!", result);
    }
}