package com.example.exception;

/**
 * A class demonstrating exception handling in Java.
 */
public class ExceptionGuri {

    /**
     * Custom exception for invalid name inputs.
     */
    public static class InvalidNameException extends Exception {
        public InvalidNameException(String message) {
            super(message);
        }
    }

    /**
     * Returns a greeting message for the given name.
     * Throws an exception if the name is invalid.
     * 
     * @param name the name to greet
     * @return a greeting message
     * @throws InvalidNameException if the name is null or empty
     */
    public String greetWithException(String name) throws InvalidNameException {
        if (name == null) {
            throw new InvalidNameException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new InvalidNameException("Name cannot be empty or whitespace");
        }
        return "Hello, " + name + "!";
    }

    /**
     * Demonstrates try-catch-finally exception handling.
     * 
     * @param name the name to greet
     * @return a greeting message or an error message
     */
    public String safeGreet(String name) {
        try {
            return greetWithException(name);
        } catch (InvalidNameException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error occurred: " + e.getMessage();
        } finally {
            System.out.println("Greeting attempt completed for input: " + (name == null ? "null" : "'" + name + "'"));
        }
    }

    /**
     * Demonstrates handling multiple exceptions and exception chaining.
     * 
     * @param names array of names to process
     * @return the first valid greeting
     * @throws RuntimeException if no valid names are found
     */
    public String processNames(String[] names) {
        if (names == null) {
            throw new IllegalArgumentException("Names array cannot be null");
        }

        if (names.length == 0) {
            throw new IllegalArgumentException("Names array cannot be empty");
        }

        StringBuilder errors = new StringBuilder();

        for (String name : names) {
            try {
                return greetWithException(name);
            } catch (InvalidNameException e) {
                errors.append(name).append(": ").append(e.getMessage()).append("; ");
            }
        }

        // If we get here, none of the names were valid
        RuntimeException ex = new RuntimeException("No valid names found");
        ex.addSuppressed(new InvalidNameException(errors.toString()));
        throw ex;
    }
}
