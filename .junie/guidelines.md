# Project Development Guidelines

This document provides guidelines and instructions for developing and maintaining this Spring Boot project.

## Build/Configuration Instructions

### Prerequisites
- Java 21 or higher
- Gradle 8.x or higher (or use the included Gradle wrapper)

### Building the Project
1. Clone the repository
2. Navigate to the project root directory
3. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```
   
   Or on Windows:
   ```bash
   gradlew.bat build
   ```

### Running the Application
To run the application locally:
```bash
./gradlew bootRun
```

## Testing Information

### Running Tests
To run all tests:
```bash
./gradlew test
```

To run a specific test class:
```bash
./gradlew test --tests "com.example.demo.GuriTest"
```

To run a specific test method:
```bash
./gradlew test --tests "com.example.demo.GuriTest.testGreetWithName"
```

### Adding New Tests
1. Create a new test class in the `src/test/java` directory
2. Use JUnit 5 annotations (`@Test`, etc.) for test methods
3. Follow the naming convention: `*Test.java` for test classes
4. Use assertions from `org.junit.jupiter.api.Assertions` to verify expected behavior

### Test Example
Here's a simple test example for the `Guri` class:

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuriTest {
    @Test
    public void testGreetWithName() {
        Guri guri = new Guri();
        String result = guri.greet("John");
        assertEquals("Hello, John!", result);
    }
}
```

### Spring Boot Test Configuration
For Spring Boot integration tests:
1. Use the `@SpringBootTest` annotation on the test class
2. Autowire components as needed
3. Use `@MockBean` to mock dependencies

Example:
```java
@SpringBootTest
class DemoApplicationTests {
    @Test
    void contextLoads() {
        // Verifies that the Spring context loads successfully
    }
}
```

## Additional Development Information

### Code Style
- Follow standard Java coding conventions
- Use 4 spaces for indentation
- Add JavaDoc comments for public methods and classes
- Keep methods focused on a single responsibility

### Project Structure
- `src/main/java`: Java source files
- `src/main/resources`: Configuration files and resources
- `src/test/java`: Test source files
- `src/test/resources`: Test configuration and resources

### Dependency Management
- Dependencies are managed in the `build.gradle` file
- Spring Boot dependencies are managed through the Spring Boot Dependency Management plugin

### Useful Gradle Tasks
- `./gradlew clean`: Removes build directories
- `./gradlew check`: Runs all checks (tests, linting, etc.)
- `./gradlew bootJar`: Creates an executable JAR file
- `./gradlew dependencies`: Shows project dependencies

### Debugging
- Use the `--debug` flag with Gradle commands for more detailed output
- For remote debugging, use:
  ```bash
  ./gradlew bootRun --debug-jvm
  ```