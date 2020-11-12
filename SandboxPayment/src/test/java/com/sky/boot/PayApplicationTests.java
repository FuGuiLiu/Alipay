package com.sky.boot;

import com.sky.boot.util.AlipayTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayApplicationTests {
    @Autowired
    @Qualifier(value = "AlipayTool")
    private AlipayTool alipayTool;

    @Test
    void contextLoads() {
        System.out.println(alipayTool.toString());
    }

}
