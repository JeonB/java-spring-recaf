package com.example.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for MyBatis.
 * This enables scanning for MyBatis mapper interfaces.
 */
@Configuration
@MapperScan("com.example.demo.mapper")
public class MyBatisConfig {
    // The @MapperScan annotation tells Spring to scan for MyBatis mapper interfaces
    // in the specified package. This is an alternative to adding @Mapper to each interface.
    
    // Additional MyBatis configuration can be added here if needed.
    // Most configuration is done in application.properties.
}