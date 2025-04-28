package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class FormattingTest {

  @Test
  public void testFormatting() {
    // This file has intentionally poor formatting
    // with extra blank lines and inconsistent indentation
    List<String> items = new ArrayList<>();
    items.add("Item 1");
    items.add("Item 2");
    items.add("Item 3");

    Map<String, Integer> counts = new HashMap<>();
    counts.put("Item 1", 1);
    counts.put("Item 2", 2);
    counts.put("Item 3", 3);

    assertEquals(3, items.size());
    assertEquals(3, counts.size());

    // More poorly formatted code
    for (int i = 0; i < 10; i++) {
      if (i % 2 == 0) {
        System.out.println("Even: " + i);
      } else {
        System.out.println("Odd: " + i);
      }
    }
  }
}
