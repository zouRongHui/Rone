package org.rone.core.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA非对称加密
 * hutool工具包中有更为完整切全面的加密工具
 * @author rone
 */
public class RsaUtils {

    public static void main(String[] args) throws Exception {
        // String seed = "123456";
        // String content = "假设这是要加密的客户数据";
        // KeyPairString keyPairString = generateKey(seed.getBytes(), 512);
        // System.out.println(keyPairString);
        //
        // //获得摘要
        // byte[] source = digestSHA(content);
        // //使用私钥对摘要进行加密 获得密文 即数字签名
        // byte[] sign = sign(keyPairString.getPrivateKeyStr(), source);
        // //使用公钥对密文进行解密,解密后与摘要进行匹配
        // if (verify(keyPairString.getPublicKeyStr(), source, sign)) {
        //     System.out.println("匹配成功 合法的签名!");
        // }
        //
        // //公钥加密私钥解密
        // //使用公钥对摘要进行加密 获得密文
        // byte[] publicKeyEncryptBytes = encryptByPublicKey(keyPairString.getPublicKeyStr(), content.getBytes("UTF-8"));
        // System.out.println("公钥加密后的密文：" + Base64.getEncoder().encodeToString(publicKeyEncryptBytes));
        // //使用私钥对密文进行解密 返回解密后的数据
        // byte[] privateKeyDecryptBytes = decryptByPrivateKey(keyPairString.getPrivateKeyStr(), publicKeyEncryptBytes);
        // System.out.println("私钥解密后的明文：" + new String(privateKeyDecryptBytes, "UTF-8"));
        //
        // //私钥加密公钥解密
        // //使用私钥对摘要进行加密 获得密文
        // byte[] privateKeyEncryptBytes = encryptByPrivateKey(keyPairString.getPrivateKeyStr(), content.getBytes("UTF-8"));
        // System.out.println("私钥加密后的密文：" + Base64.getEncoder().encodeToString(privateKeyEncryptBytes));
        // //使用公钥对密文进行解密 返回解密后的数据
        // byte[] pubilcKeyDecryptBytes = decryptByPublicKey(keyPairString.getPublicKeyStr(), privateKeyEncryptBytes);
        // System.out.println("公钥解密：" + new String(pubilcKeyDecryptBytes, "UTF-8"));


        //自定义的加密因子
        String seed = "xxx";
        KeyPairString keyPairString = generateKey(seed.getBytes(), 512);
        String content = "xxx";
        // String content = "xxx";
        System.out.println("明文：" + new String(decryptByPrivateKey(keyPairString.getPrivateKeyStr(), Base64.getDecoder().decode(content.getBytes())), "UTF-8"));

    }

    /**
     * 获取密钥对
     * @param seed  加密因子，同一加密因子，同一长度密钥对相同
     * @param keySize   密钥长度，最少512
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static KeyPairString generateKey(byte[] seed, int keySize) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        secureRandom.setSeed(seed);
        keyPairGenerator.initialize(keySize, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        KeyPairString keyPairString = new KeyPairString();
        keyPairString.setPrivateKeyStr(privateKeyStr);
        keyPairString.setPublicKeyStr(publicKeyStr);
        return keyPairString;
    }

    /**
     * 使用RSA公钥加密数据 - 私钥解密
     * @param publicKeyStr    公钥(字符串形式)
     * @param dataBytes     源数据字节码数组
     * @return 密文
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static byte[] encryptByPublicKey(String publicKeyStr, byte[] dataBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(dataBytes);
    }

    /**
     * 使用RSA私钥加密数据 - 公钥解密
     * @param privateKeyStr 私钥(字符串形式)
     * @param dataBytes     源数据字节码数组
     * @return 加密数据
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static byte[] encryptByPrivateKey(String privateKeyStr, byte[] dataBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(dataBytes);
    }

    /**
     * 用RSA私钥解密数据
     * @param privateKeyStr   私钥(字符串形式)
     * @param dataBytes     密文字节码数组
     * @return 明文
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static byte[] decryptByPrivateKey(String privateKeyStr, byte[] dataBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(dataBytes);
    }

    /**
     * 用RSA公钥解密
     * @param publicKeyStr  公钥(字符串形式)
     * @param dataBytes     密文字节码数组
     * @return 解密数据
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static byte[] decryptByPublicKey(String publicKeyStr, byte[] dataBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(dataBytes);
    }

    /**
     * 计算字符串的SHA数字摘要，以byte[]形式返回
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digestSHA(String content) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        return messageDigest.digest(content.getBytes("UTF-8"));
    }

    /**
     * 用私钥计算数字签名
     * @param privateKeyStr 私钥(字符串形式)
     * @param digestBytes    digestSHA() 获取的数字摘要
     * @return  签名
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] sign(String privateKeyStr, byte[] digestBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(privateKey);
        sig.update(digestBytes);
        return sig.sign();
    }

    /**
     * 验证数字签名
     * @param publicKeyStr  公钥(字符串形式)
     * @param digestBytes    原文的数字摘要
     * @param signBytes      签名（对原文的数字摘要的签名）
     * @return 是否证实 boolean
     */
    public static boolean verify(String publicKeyStr, byte[] digestBytes, byte[] signBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Signature sig = Signature.getInstance("SHA1withRSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr.getBytes()));
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            sig.initVerify(publicKey);
            sig.update(digestBytes);
            return sig.verify(signBytes);
        } catch (Exception e) {
            System.out.println(String.format("验证数字签名失败，出错信息：%s" , e.toString()));
            return false;
        }
    }

    /**
     * 字符串形式的密钥对
     * @author Rone
     */
    public static class KeyPairString {
        private String privateKeyStr;
        private String publicKeyStr;

        public String getPrivateKeyStr() {
            return privateKeyStr;
        }

        public void setPrivateKeyStr(String privateKeyStr) {
            this.privateKeyStr = privateKeyStr;
        }

        public String getPublicKeyStr() {
            return publicKeyStr;
        }

        public void setPublicKeyStr(String publicKeyStr) {
            this.publicKeyStr = publicKeyStr;
        }

        @Override
        public String toString() {
            return "KeyPairString{" +
                    "privateKeyStr='" + privateKeyStr + '\'' +
                    ", publicKeyStr='" + publicKeyStr + '\'' +
                    '}';
        }
    }
}
