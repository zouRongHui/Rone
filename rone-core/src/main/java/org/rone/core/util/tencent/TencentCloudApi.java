package org.rone.core.util.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.*;
import org.rone.core.jdk.exception.RoneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rone
 * @date 2022年02月24日
 */
public class TencentCloudApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(TencentCloudApi.class);

    private static final String SECRET_ID = "xxx";
    private static final String SECRET_KEY = "xxx";
    public static final String RULE_ID = "1";
    private static FaceidClient client;

    static {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("faceid.tencentcloudapi.com");
        // 设置代理
        // httpProfile.setProxyHost();
        // httpProfile.setProxyPort();
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        clientProfile.setDebug(true);
        // 实例化要请求产品的client对象,clientProfile是可选的
        client = new FaceidClient(cred, "", clientProfile);
    }

    /**
     * 申请活体校验的BizToken
     * @param idCard    身份证号
     * @param name  客户姓名
     * @return
     */
    public static String detectAuth(String idCard, String name) {
        try{
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DetectAuthRequest req = new DetectAuthRequest();
            // 业务流程ID，在腾讯云慧眼人脸核身控制台自助接入里创建
            req.setRuleId(RULE_ID);
            // 待验证的客户的身份证号
            req.setIdCard(idCard);
            // 待验证的客户的姓名
            req.setName(name);
            // 如果是自上传照片比对的话，此处需要上传待对比人的照片base64
            req.setImageBase64("");
            // 返回的resp是一个DetectAuthResponse的实例，与请求对象对应
            DetectAuthResponse resp = client.DetectAuth(req);
            LOGGER.info("向腾讯云申请人脸核身BizToken，姓名：{}，身份证号：{}，申请结果：{}", name, idCard, DetectAuthResponse.toJsonString(resp));
            return resp.getBizToken();
        } catch (TencentCloudSDKException e) {
            LOGGER.error("向腾讯云申请人脸核身BizToken出错，客户姓名：{}， 身份证号：{}，错误说明：{}", name, idCard, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取活体校验的结果
     * @param bizToken  {@link TencentCloudApi#detectAuth(String, String)} 获取
     * @return
     * @throws RoneException
     */
    public static GetDetectInfoEnhancedResponse getDetectInfo(String bizToken) throws RoneException {
        try{
            // 实例化一个请求对象,每个接口都会对应一个request对象
            GetDetectInfoEnhancedRequest req = new GetDetectInfoEnhancedRequest();
            // 流程标志，DetectAuth接口返回
            req.setBizToken(bizToken);
            // 业务流程ID，在腾讯云慧眼人脸核身控制台自助接入里创建
            req.setRuleId(RULE_ID);
            // 从活体视频中截取的最佳帧照片的数据，默认0，最大10
            req.setBestFramesCount(1L);
            // 返回的resp是一个GetDetectInfoEnhancedResponse的实例，与请求对象对应
            GetDetectInfoEnhancedResponse resp = client.GetDetectInfoEnhanced(req);
            LOGGER.info("向腾讯云获取人脸核身的结果，BizToken：{}，返回结果：{}", bizToken, DetectAuthResponse.toJsonString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            LOGGER.info("向腾讯云获取人脸核身的结果出错，BizToken：{}，错误说明：{}", bizToken, e.getMessage(), e);
            throw new RoneException("无法获取人脸核身的结果，请稍后重新操作！", e);
        }
    }

    public static void main(String[] args) {
        try {
            String bizToken = TencentCloudApi.detectAuth("xxxxx", "xxxxxxx");
            // TencentCloudApi.getDetectInfo("[bizToken]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
