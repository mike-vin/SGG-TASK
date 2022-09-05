package com.sgg.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private ConfigurableApplicationContext context;
	
    @Test
    void contextLoads() {
        assertTrue(context.isRunning());
    }

}
