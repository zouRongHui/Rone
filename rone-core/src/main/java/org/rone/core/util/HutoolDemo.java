package org.rone.core.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyPair;

/**
 * hutool工具包的示例
 * @author rone
 */
public class HutoolDemo {

    public static void main(String[] args) {
        rsaSecure();
    }

    /**
     * RSA非对称加密
     */
    public static void rsaSecure() {
        String data = "我是个文本，我要加密！";
        String seed = "yhgjndjflaj2jfdla=3";
        KeyPair keyPair = SecureUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue(), 512, seed.getBytes());
        RSA rsa = SecureUtil.rsa(keyPair.getPrivate().getEncoded(), keyPair.getPublic().getEncoded());
        System.out.println(rsa.getPrivateKeyBase64());
        System.out.println(rsa.getPublicKeyBase64());
        String encryptData = rsa.encryptBase64(data, KeyType.PrivateKey);
        System.out.println(encryptData);
        String decryptData = rsa.decryptStr(encryptData, KeyType.PublicKey);
        System.out.println(decryptData);
    }
}
