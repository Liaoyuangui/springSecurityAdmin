package com.example.springsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("system:menu:list".contains("system:menu:list"));
    }

}
