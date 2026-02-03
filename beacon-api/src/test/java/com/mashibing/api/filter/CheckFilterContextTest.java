package com.mashibing.api.filter;

import com.mashibing.common.model.StandardSubmit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CheckFilterContextTest {
@Autowired
private CheckFilterContext checkFilterContext;
    @Test
    public void check() {
//        String xxx="【1234】";
//        String sign=xxx.substring(1,xxx.indexOf("】"));
//        System.out.println(sign);
        checkFilterContext.check(new StandardSubmit());
    }
}