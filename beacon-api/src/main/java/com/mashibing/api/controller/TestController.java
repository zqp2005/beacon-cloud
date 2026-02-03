package com.mashibing.api.controller;

import com.mashibing.api.filter.CheckFilterContext;
import com.mashibing.common.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    private CheckFilterContext checkFilterContext;
    @GetMapping("/api/test")
    public void test() {

        System.out.println("---------");
        checkFilterContext.check( new StandardSubmit());
    }
}
