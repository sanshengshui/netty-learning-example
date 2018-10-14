package com.sanshengshui.mqtt.auth;

import com.sanshengshui.mqtt.common.auth.GrozaAuthService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户名和密码认证服务
 */
@Service
public class AuthService implements GrozaAuthService {
    @Override
    public boolean checkValid(String username, String password) {
        if (StringUtils.isEmpty(username)){
            return false;
        }
        if (StringUtils.isEmpty(password)){
            return false;
        }


    }
}
