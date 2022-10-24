package org.rone.core.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author rone
 */
public class JwtUtils {

    /** 签名秘钥 */
    public static final String BASE64_SECRET = "xxxx";
    /** 超时毫秒数（默认30分钟） */
    public static final int EXPIRE_MILLISECOND = 1800000;

    private JwtUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        String jwt = JwtUtils.createJWT("110", "issuer", "subject", "audience", 5 * 1000, null);
        System.out.println(jwt);
        System.out.println();
        System.out.println(JwtUtils.parseJWT(jwt));
    }

    /**
     * 签发JWT，隐私数据请提前加密
     * @param jti JWT ID用于标识该JWT
     * @param issuer    签发人
     * @param subject   主题
     * @param audience  受众
     * @param otherData 其他数据
     * @return java.lang.String
     *      格式  header.payload.signature
     *          header(JWT头)：是一个描述JWT元数据的JSON对象，例如{"alg": "HS256","typ": "JWT"}。用Base64 URL算法转换成字符串。
     *          payload(有效载荷)：JWT的主体内容部分，也是一个JSON对象，包含其他需要传递的数据，包含JWT默认的7个数据还要自定义的数据。用Base64 URL算法转换成字符串。
     *          signature(签名信息)：将header和payload通过指定的算法生成哈希，以确保数据不会被篡改。
     */
    public static String createJWT(String jti, String issuer, String subject, String audience, Map<String, Object> otherData) {
        return createJWT(jti, issuer, subject, audience, EXPIRE_MILLISECOND, otherData);
    }

    /**
     * 签发JWT，隐私数据请提前加密
     * @param jti JWT ID用于标识该JWT
     * @param issuer    签发人
     * @param subject   主题
     * @param audience  受众
     * @param expireMilliseconds    过期时间，单位毫秒
     * @param otherData 其他数据
     * @return java.lang.String
     *      格式  header.payload.signature
     *          header(JWT头)：是一个描述JWT元数据的JSON对象，例如{"alg": "HS256","typ": "JWT"}。用Base64 URL算法转换成字符串。
     *          payload(有效载荷)：JWT的主体内容部分，也是一个JSON对象，包含其他需要传递的数据，包含JWT默认的7个数据还要自定义的数据。用Base64 URL算法转换成字符串。
     *          signature(签名信息)：将header和payload通过指定的算法生成哈希，以确保数据不会被篡改。
     */
    public static String createJWT(String jti, String issuer, String subject, String audience, int expireMilliseconds, Map<String, Object> otherData){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Map<String, Object> headerMap = new LinkedHashMap<>(4);
        headerMap.put("alg", signatureAlgorithm.getValue());
        headerMap.put("typ", "JWT");

        //将BASE64SECRET常量字符串使用base64解码成字节数组
        byte[] apiKeySecretBytes = TextCodec.BASE64.decode(BASE64_SECRET);
        //使用HmacSHA256签名算法生成一个HS256的签名秘钥Key
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        long nowTimeMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowTimeMillis);
        Date expirationDate;
        if (expireMilliseconds > 0) {
            expirationDate = new Date(nowTimeMillis + expireMilliseconds);
        } else {
            expirationDate = null;
        }

        JwtBuilder jwtBuilder = Jwts.builder()
                //算法，密钥
                .signWith(signatureAlgorithm, signingKey)
                //JWT头
                .setHeader(headerMap)
                //标识
                .setId(jti)
                //签发人
                .setIssuer(issuer)
                //主题
                .setSubject(subject)
                //受众
                .setAudience(audience)
                //签发时间
                .setIssuedAt(nowDate)
                //生效时间
                .setNotBefore(nowDate)
                //失效时间
                .setExpiration(expirationDate);
        if (otherData != null) {
            for (Map.Entry<String, Object> entry : otherData.entrySet()) {
                String key = entry.getKey();
                if (key != null) {
                    Object value = entry.getValue();
                    //配置自定义的数据
                    jwtBuilder.claim(key, value);
                }
            }
        }
        return jwtBuilder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr    this.createJWT() 签发的JWT
     * @return io.jsonwebtoken.Claims
     * @throws ExpiredJwtException  过期
     * @throws SignatureException   签名信息被篡改
     * @throws MalformedJwtException    畸形，JWT头、有效载荷被篡改
     * @throws IllegalArgumentException jwtStr为null、空
     */
    public static Claims parseJWT(String jwtStr) throws ExpiredJwtException,SignatureException,MalformedJwtException,IllegalArgumentException  {
        return Jwts.parser().setSigningKey(TextCodec.BASE64.decode(BASE64_SECRET)).parseClaimsJws(jwtStr).getBody();
    }

    /**
     * 验证，后续补充
     * @param jwtStr
     * @return void
     */
    public static final void validateJWT(String jwtStr) {

    }
}
