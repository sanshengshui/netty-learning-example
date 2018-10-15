package com.sanshengshui.iot.auth.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.SecureUtil;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * 私钥
 * @author 穆书伟
 */
public class RsaKeyUtil {
    /**
     * 生成私钥文件
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.print("输入保存密钥文件的路径(如: f:/rsa/): ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 512, LocalDateTime.now().toString().getBytes());
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String privatePath = path + "auth-private.key";
        IoUtil.writeObjects(FileUtil.getOutputStream(privatePath), true, privateKey);
    }
}
