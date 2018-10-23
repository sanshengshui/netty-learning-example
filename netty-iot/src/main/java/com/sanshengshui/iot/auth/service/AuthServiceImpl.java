package com.sanshengshui.iot.auth.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.sanshengshui.iot.common.auth.GrozaAuthService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;

/**
 * 用户名和密码认证服务
 * @author 穆书伟
 */
@Service
public class AuthServiceImpl implements GrozaAuthService {
    private RSAPrivateKey privateKey;

    @Override
    public boolean checkValid(String username, String password) {
        if (StringUtils.isEmpty(username)){
            return false;
        }
        if (StringUtils.isEmpty(password)){
            return false;
        }
        RSA rsa = new RSA(privateKey,null);
        String value = rsa.encryptBcd(username, KeyType.PrivateKey);
        return value.equals(password) ? true : false;
    }

    @PostConstruct
    public void init() {
        privateKey = IoUtil.readObj(AuthServiceImpl.class.getClassLoader().getResourceAsStream("keystore/auth-private.key"));
    }
}
