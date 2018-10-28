package com.sanshengshui.iot.web;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;

/**
 * @author jamesmsw
 * @date 2018年10月24号 21:24
 */
@RestController
@RequestMapping("/groza/v1")
@Slf4j
public class
AuthApiController {

    @RequestMapping(value = "/{username}/auth",method = RequestMethod.GET,produces = "application/json")
    public String getPwd(@PathVariable("username") String username){
        RSAPrivateKey privateKey = IoUtil.readObj(AuthApiController.class.getClassLoader().getResourceAsStream("keystore/auth-private.key"));
        RSA rsa = new RSA(privateKey, null);
        return rsa.encryptBcd(username, KeyType.PrivateKey);
    }


}
