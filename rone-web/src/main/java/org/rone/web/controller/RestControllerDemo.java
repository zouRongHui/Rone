package org.rone.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.rone.web.model.entity.User;
import org.rone.web.model.vo.DateVO;
import org.rone.web.model.vo.Result;
import org.rone.web.service.AsyncMethodService;
import org.rone.web.service.RoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * spring boot 接口
 * @RestController  声明该类下的接口为rest风格，返回json格式的数据
 * @RequestMapping("/") 声明该类下的接口地址的父地址
 * @Api(value = "接口", description = "Demo") 声明swagger2文档中该类的说明
 * @EnableAsync 声明该类下的方法可以调用异步的方法，详见 {@link AsyncMethodService}
 * @author Rone
 */
@RestController
@RequestMapping
@Api(tags = "REST风格的请求示例")
@EnableAsync
public class RestControllerDemo {

    private static Logger logger = LoggerFactory.getLogger(RestControllerDemo.class);

    private final RoneService roneService;
    private final AsyncMethodService asyncMethodService;

    public RestControllerDemo(RoneService roneService, AsyncMethodService asyncMethodService) {
        this.roneService = roneService;
        this.asyncMethodService = asyncMethodService;
    }

    /**
     * @RequestMapping(value = "/test", method = RequestMethod.GET) 声明该接口的请求地址以及支持的请求方法，method 未声明的话支持所有方式
     * @ApiOperation(value = "纯测试请求", notes = "仅仅是为了测试请求是否能通")  声明swagger2中该接口的说明
     * @PathVariable("no")  将URL中的占位符参数绑定到控制器处理方法的参数中，若未声明"no"则参数名和占位符名称需要一致
     * @ApiParam("编号")    声明swagger2文档中该请求参数的说明
     * @return
     */
    @RequestMapping(value = "/test/{no}", method = RequestMethod.GET)
    @ApiOperation(value = "纯测试请求", notes = "仅仅是为了测试请求是否能通")
    public Result test(HttpServletRequest request, HttpServletResponse response, @PathVariable("no") @ApiParam("编号") String no) throws UnsupportedEncodingException {
        logger.info("/test/{no} 的请求编号为：{}", no);
        // Cookie 中不支持中文，中文会报错。如果非要中文需要进行再编码
        Cookie cookie = new Cookie(URLEncoder.encode("中文Cookie", "utf-8"), "123");
        response.addCookie(cookie);
        // 后续使用需要解码，第一次请求该方法时会有NOP异常
        for (Cookie c : request.getCookies()) {
            logger.info(URLDecoder.decode(c.getName(), "utf-8"));
        }
        roneService.initValue();
        return Result.success("测试请求成功！");
    }

    @RequestMapping(value = "/asyncTest")
    @ApiOperation(value = "异步线程", notes = "异步线程的请求测试")
    public Result testAsync() {
        int length = 15;
        for (int i = 0; i < length; i++) {
            logger.info(i + " 主线程调用....");
            asyncMethodService.test(i);
            logger.info(i + " 调用完成....");
        }
        return Result.success("调用成功...");
    }

    @RequestMapping(value = "/getDate", method = RequestMethod.GET)
    @ApiOperation(value = "获取时间对象")
    public Result<?> getDate() {
       return Result.success(new DateVO("test.", new Date(), new Date()));
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ApiOperation(value = "上传图片")
    public Result<List<String>> uploadImage(MultipartFile[] files) {
        return files == null ? Result.fault("没有文件上传！") : Result.success(String.format("成功上传 %s 张", files.length));
    }

    @RequestMapping(value = "/downloadPDF", method = RequestMethod.POST)
    @ApiOperation(value = "下载PDF文件")
    public void downloadPDF(HttpServletResponse response, @ApiParam("图片base64编码")String[] graphData) throws Exception {
        roneService.downloadPDF(response, graphData);
    }

    @RequestMapping(value = "/testFilterInterceptorAdviceAspect", method = RequestMethod.POST)
    @ApiOperation(value = "filter -> interceptor -> advice -> aspect 示例")
    public Result testFilterInterceptorAdviceAspect(@RequestBody User user) throws Exception {
        logger.info("filter -> interceptor -> advice -> aspect 示例, user:{}", user);
        return Result.success();
    }
}