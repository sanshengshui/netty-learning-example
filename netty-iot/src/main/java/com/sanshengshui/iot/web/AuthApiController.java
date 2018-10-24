package com.sanshengshui.iot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groza/v1")
@Slf4j
public class AuthApiController {

    @RequestMapping(value = "/test",method = RequestMethod.GET,produces = "application/json")
    public String getAuthByUsername(){
        return "Hello,World!";
    }
}
