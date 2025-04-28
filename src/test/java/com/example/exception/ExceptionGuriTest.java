package com.example.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Test class for the ExceptionGuri class. */
public class ExceptionGuriTest {

  /** Test the greetWithException method with a valid name. */
  @Test
  public void testGreetWithExceptionValidName() throws ExceptionGuri.InvalidNameException {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.greetWithException("John");
    assertEquals("Hello, John!", result);
  }

  /** Test that greetWithException throws an exception for null name. */
  @Test
  public void testGreetWithExceptionNullName() {
    ExceptionGuri guri = new ExceptionGuri();
    Exception exception =
        assertThrows(
            ExceptionGuri.InvalidNameException.class,
            () -> {
              guri.greetWithException(null);
            });
    assertEquals("Name cannot be null", exception.getMessage());
  }

  /** Test that greetWithException throws an exception for empty name. */
  @Test
  public void testGreetWithExceptionEmptyName() {
    ExceptionGuri guri = new ExceptionGuri();
    Exception exception =
        assertThrows(
            ExceptionGuri.InvalidNameException.class,
            () -> {
              guri.greetWithException("");
            });
    assertEquals("Name cannot be empty or whitespace", exception.getMessage());
  }

  /** Test that greetWithException throws an exception for whitespace name. */
  @Test
  public void testGreetWithExceptionWhitespaceName() {
    ExceptionGuri guri = new ExceptionGuri();
    Exception exception =
        assertThrows(
            ExceptionGuri.InvalidNameException.class,
            () -> {
              guri.greetWithException("   ");
            });
    assertEquals("Name cannot be empty or whitespace", exception.getMessage());
  }

  /** Test the safeGreet method with a valid name. */
  @Test
  public void testSafeGreetValidName() {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.safeGreet("John");
    assertEquals("Hello, John!", result);
  }

  /** Test the safeGreet method with a null name. */
  @Test
  public void testSafeGreetNullName() {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.safeGreet(null);
    assertEquals("Error: Name cannot be null", result);
  }

  /** Test the safeGreet method with an empty name. */
  @Test
  public void testSafeGreetEmptyName() {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.safeGreet("");
    assertEquals("Error: Name cannot be empty or whitespace", result);
  }

  /** Test the processNames method with a valid name. */
  @Test
  public void testProcessNamesValidName() {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.processNames(new String[] {"John"});
    assertEquals("Hello, John!", result);
  }

  /** Test the processNames method with a mix of invalid and valid names. */
  @Test
  public void testProcessNamesMixedValidity() {
    ExceptionGuri guri = new ExceptionGuri();
    String result = guri.processNames(new String[] {null, "", "   ", "John"});
    assertEquals("Hello, John!", result);
  }

  /** Test that processNames throws an exception when all names are invalid. */
  @Test
  public void testProcessNamesAllInvalid() {
    ExceptionGuri guri = new ExceptionGuri();
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              guri.processNames(new String[] {null, "", "   "});
            });
    assertEquals("No valid names found", exception.getMessage());
    assertTrue(exception.getSuppressed().length > 0);
    assertTrue(exception.getSuppressed()[0] instanceof ExceptionGuri.InvalidNameException);
  }

  /** Test that processNames throws an exception for null array. */
  @Test
  public void testProcessNamesNullArray() {
    ExceptionGuri guri = new ExceptionGuri();
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              guri.processNames(null);
            });
    assertEquals("Names array cannot be null", exception.getMessage());
  }

  /** Test that processNames throws an exception for empty array. */
  @Test
  public void testProcessNamesEmptyArray() {
    ExceptionGuri guri = new ExceptionGuri();
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              guri.processNames(new String[] {});
            });
    assertEquals("Names array cannot be empty", exception.getMessage());
  }
}
