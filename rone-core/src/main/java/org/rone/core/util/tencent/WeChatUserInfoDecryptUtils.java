package org.rone.core.util.tencent;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * 微信用户数据解密工具
 * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充
 * @author rone
 */
public class WeChatUserInfoDecryptUtils {

    /**
     * 解密微信小程序的用户数据
     * @param content
     * @param keyByte
     * @param ivByte
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidParameterSpecException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException e) {
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        }
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    /**
     * 删除解密后明文的补位字符
     * @param decrypted 解密后的明文
     * @return 删除补位字符后的明文
     */
    public static byte[] decode(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }
}
