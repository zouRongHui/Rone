package org.rone.core.util.tencent;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.rone.core.jdk.exception.RoneException;
import org.rone.core.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;

/**
 * @author rone
 */
public class WeChatApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(WeChatApi.class);

    /**
     * 微信小程序appid
     */
    public static final String APP_ID = "xxxxx";
    /**
     * 微信小程序secret
     */
    public static final String SECRET = "xxxxx";
    /**
     * 微信的登录
     */
    public static final String JSCODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 获取 ACCESS_TOKEN
     */
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 获取小程序码，有数量限制，参数最大 128 字节
     */
    public static final String GET_WX_APP_CODE = "https://api.weixin.qq.com/wxa/getwxacode";
    /**
     * 获取小程序码，无数量限制，参数最大 64 字节
     */
    public static final String GET_WX_APP_CODE_UNLIMIT = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

    public static void main(String[] args) throws Exception {
        String jsCode = "xxxx";
        System.out.println(jscode2session(jsCode));
        System.out.println();

        System.out.println(accessToken());
        System.out.println();

        String accessToken = "xxxx";
        byte[] appCodeBytes = getWxAppCode(accessToken, "pages/index/index?name=rone");
        File file = new File("E:\\WxAppCode.png");
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(appCodeBytes);
        outputStream.flush();
        outputStream.close();

        byte[] appCodeBytes2 = getWxAppCodeUnlimit(accessToken, "p=xxxx&w=xxxx", "pages/index/index");
        File file2 = new File("E:\\WxAppCodeUnlimit.png");
        OutputStream outputStream2 = new FileOutputStream(file2);
        outputStream2.write(appCodeBytes2);
        outputStream2.flush();
        outputStream2.close();

        String encryptedData = "xxxx"
                ;
        String iv = "xxxx";
        WxUserInfo wxUserInfo = decryptWxUserInfo(encryptedData, "xxxx", iv);
        System.out.println(wxUserInfo);
        System.out.println();
    }


    /**
     * 置换微信小程序的openId
     * @param js_code   小程序前端 wx.login 获取
     * @return Jscode2sessionResult
     * @throws RoneException    微信服务器返回错误信息
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Jscode2sessionResult jscode2session(String js_code) throws RoneException, IOException, URISyntaxException {
        Map<String, String> paramMap = new HashMap<>(4);
        paramMap.put("appid", APP_ID);
        paramMap.put("secret", SECRET);
        paramMap.put("js_code", js_code);
        paramMap.put("grant_type", "authorization_code");

        String resultString = executeGetWithFormParam(JSCODE_2_SESSION, paramMap);
        Jscode2sessionResult result = JSON.parseObject(resultString, Jscode2sessionResult.class);
        return result;
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据
     * @return 返回 access_token
     * @throws RoneException    微信服务器返回错误信息
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String accessToken() throws RoneException, IOException, URISyntaxException {
        Map<String, String> paramMap = new HashMap<>(3);
        paramMap.put("grant_type", "client_credential");
        paramMap.put("appid", APP_ID);
        paramMap.put("secret", SECRET);

        String resultString = executeGetWithFormParam(ACCESS_TOKEN, paramMap);
        return JSON.parseObject(resultString).getString("access_token");
    }

    /**
     * 获取小程序码，有数量限制，参数最大 128 字节
     * @param accessToken   accessToken()获取
     * @param path  扫码进入的小程序页面路径，若要携带参数在页面路径后面添加
     *              例如"pages/index/index?name=rone"
     * @return 返回图片的字节码数组
     * @throws RoneException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static byte[] getWxAppCode(String accessToken, String path) throws RoneException, IOException, URISyntaxException {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("path", path);

        return executeGetWxAppCode(GET_WX_APP_CODE + "?access_token=" + accessToken, paramMap);
    }

    /**
     * 获取小程序码，无数量限制，参数最大 64 字节
     * @param accessToken 通过 accessToken() 获取的凭证
     * @param param 自定义参数，例如"p=YDJ2020050123456789012&w=07657"
     * @param page  转跳的页面，必须是已经发布的小程序存在的页面，如果不填写这个字段默认跳主页面
     *              例如"pages/index/index"
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws RoneException
     */
    public static byte[] getWxAppCodeUnlimit(String accessToken, String param, String page) throws URISyntaxException, IOException, RoneException {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("scene", param);
        if (StrUtil.isNotEmpty(page)) {
            paramMap.put("page", page);
        }
        return executeGetWxAppCode(GET_WX_APP_CODE_UNLIMIT + "?access_token=" + accessToken, paramMap);
    }

    /**
     * 获取小程序码
     * @param url   请求地址
     * @param paramMap  参数
     * @return
     * @throws IOException
     * @throws RoneException
     * @throws URISyntaxException
     */
    private static byte[] executeGetWxAppCode(String url, Map<String, Object> paramMap) throws IOException, RoneException, URISyntaxException {
        HttpPost httpPost = new HttpPost((new URIBuilder(url)).build());
        httpPost.setEntity(new StringEntity(JSON.toJSONString(paramMap), "utf-8"));
        CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        byte[] resultBytes = EntityUtils.toByteArray(responseEntity);
        String result = new String(resultBytes, "utf-8");
        LOGGER.info("微信API，\n请求URL：{} \n参数为：{} \n", url, JSON.toJSONString(paramMap));
        if (StrUtil.isEmpty(result)) {
            LOGGER.error("微信API，获取小程序码出错，返回数据为空");
            throw new RoneException("微信API，获取小程序码出错，返回数据为空");
        } else if (result.indexOf("errcode") > 0) {
            LOGGER.error("微信API，获取小程序码出错，出错提示：{}", result);
            throw new RoneException(String.format("微信API，获取小程序码出错，出错提示：%s", JSON.parseObject(result).getString("errmsg")));
        }
        return resultBytes;
    }

    /**
     * 解密微信的用户数据
     * @param encryptedData 微信小程序前端 wx.getUserInfo() 获取的数据
     * @param sessionKey    jscode2session() 获取的用户的session_key
     * @param iv    微信小程序前端 wx.getUserInfo() 获取的数据
     * @throws InvalidAlgorithmParameterException
     * @throws RoneException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidParameterSpecException
     * @throws UnsupportedEncodingException
     */
    public static WxUserInfo decryptWxUserInfo(String encryptedData, String sessionKey, String iv) throws InvalidAlgorithmParameterException, RoneException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, UnsupportedEncodingException {
        byte[] resultByte = WeChatUserInfoDecryptUtils.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
        if ( null != resultByte && resultByte.length > 0){
            WxUserInfo wxUserInfo = JSON.parseObject(new String(WeChatUserInfoDecryptUtils.decode(resultByte), "utf-8"), WxUserInfo.class);
            if (!APP_ID.equals(wxUserInfo.getWatermark().getAppid())) {
                throw new RoneException("解密微信的用户数据出错：该加密数据并非本小程序的的加密数据");
            }
            return wxUserInfo;
        }
        throw new RoneException("解密微信的用户数据出错：解析出来的数据为空");
    }

    /**
     * 与微信小程序服务端get请求的公共方法
     * @param url   地址
     * @param paramMap  参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws RoneException    微信小程序服务端提示请求出错
     */
    private static String executeGetWithFormParam(String url, Map<String, String> paramMap) throws URISyntaxException, IOException, RoneException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (paramMap != null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        }

        HttpGet httpGet = new HttpGet(new URIBuilder(url).setParameters(nameValuePairs).build());
        CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity responseEntity = httpResponse.getEntity();
        String resultString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        LOGGER.info("微信API \n请求URL：{} \n参数为：{} \n返回结果：{} \n", url, JSON.toJSONString(paramMap), resultString);
        JSONObject resultJson = JSON.parseObject(resultString);
        Integer errCode = resultJson.getInteger("errcode");
        if (errCode == null || errCode == 0) {
            return resultString;
        } else {
            throw new RoneException(resultJson.getString("errcode"));
        }
    }

    /**
     * 微信小程序服务端 - Jscode2session返回的数据内容
     * @author: Rone
     */
    public static class Jscode2sessionResult {

        /** 用户唯一标识 */
        private String openid;
        /** 会话密钥 */
        private String session_key;
        /** 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。 */
        private String unionid;
        /** 错误码 */
        private String errcode;
        /** 错误信息 */
        private String errmsg;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        @Override
        public String toString() {
            return "Jscode2sessionResult{" +
                    "openid='" + openid + '\'' +
                    ", session_key='" + session_key + '\'' +
                    ", unionid='" + unionid + '\'' +
                    ", errcode='" + errcode + '\'' +
                    ", errmsg='" + errmsg + '\'' +
                    '}';
        }
    }

    /**
     * 微信小程序 - 用户信息解密后的数据格式
     * @author Rone
     */
    public static class WxUserInfo {
        /**
         * openId
         */
        private String openId;
        /**
         * 用户昵称
         */
        private String nickName;
        /**
         * 用户性别，0:未知;1:男性;2:女性
         */
        private String gender;
        /**
         * 显示 country，province，city 所用的语言
         *      en	英文
         *      zh_CN	简体中文
         *      zh_TW	繁体中文
         */
        private String language;
        /**
         * 用户所在城市
         */
        private String city;
        /**
         * 用户所在省份
         */
        private String province;
        /**
         * 用户所在国家
         */
        private String country;
        /**
         * 用户头像图片的 URL
         */
        private String avatarUrl;
        /**
         * 同一用户，对同一个微信开放平台下的不同应用，UnionID是相同的
         */
        private String unionId;
        /**
         * 数据水印
         */
        private Watermark watermark;

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public Watermark getWatermark() {
            return watermark;
        }

        public void setWatermark(Watermark watermark) {
            this.watermark = watermark;
        }

        @Override
        public String toString() {
            return "WxUserInfo{" +
                    "openId='" + openId + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", language='" + language + '\'' +
                    ", city='" + city + '\'' +
                    ", province='" + province + '\'' +
                    ", country='" + country + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", unionId='" + unionId + '\'' +
                    ", watermark=" + watermark +
                    '}';
        }

        /**
         * 微信小程序 - 用户信息水印
         * @author Rone
         */
        public class Watermark {
            private String appid;
            private Date timestamp;

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public Date getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(Date timestamp) {
                this.timestamp = timestamp;
            }

            @Override
            public String toString() {
                return "Watermark{" +
                        "appid='" + appid + '\'' +
                        ", timestamp=" + timestamp +
                        '}';
            }
        }
    }
}
