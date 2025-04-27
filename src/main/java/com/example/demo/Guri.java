package com.example.demo;

public class Guri {
    /**
     * Returns a greeting message for the given name.
     * 
     * @param name the name to greet
     * @return a greeting message
     */
    public String greet(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Hello, World!";
        }
        return "Hello, " + name + "!";
    }
}
