package org.rone.web.controller;

import io.swagger.annotations.Api;
import org.rone.web.model.entity.User;
import org.rone.web.service.RoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

/**
 * spring mvc 接口定义处
 * 传统的spring mvc项目请查看 https://github.com/zouRongHui/program_old/blob/master/spring/src/main/resources/springmvc.xml
 * @Controller  表明这是个接口类
 * @RequestMapping("/rone") 标注该接口类下所有接口地址的父节点路径
 * springMVC具体接收到一个请求的处理流程：
 *     1).前端发送请求到DispatcherServlet前端控制器；
 *     2).DispatcherServlet调用HandlerMapping处理器映射器，去查找相应的Handler（可根据xml配置、注解查找）；
 *     3).HandlerMapping返回返回一个HandlerExecutionChain对象(包含一个Handler处理器和多个HandlerIntercepter处理器拦截器）给DispatcherServlet；
 *     4).DispatcherServlet调用HandlerAdapter处理配适器，去执行相应的Handler；
 *     5).HandlerAdapter调用具体的Handler；
 *     6).Handler处理完后返回一个ModelAndView视图模型给HandlerAdapter；
 *     7).HandlerAdapter返回ModelAndView给DispatcherServlet；
 *     8).DispatcherServlet调用ViewResolver视图解析器，去解析具体的视图；
 *     9).ViewResolver返回具体的View给DispatcherServlet；
 *     10).DispatcherServlet渲染View，填充需要的数据；
 *     11).DispatcherServlet响应客户。
 * @author rone
 */
@Controller
@RequestMapping("/rone")
@SessionAttributes(value="user", types= User.class)
@Api(hidden = true)
public class RoneController {

    private static Logger logger = LoggerFactory.getLogger(RoneController.class);

    private final RoneService roneService;

    public RoneController(RoneService roneService) {
        this.roneService = roneService;
    }

    /**
     * 单纯的请求一个静态页面
     * @RequestMapping("/success")    配置请求的地址
     * 请求方式限制为get，不限定的话则所有的请求方式都可以请求到该方法
     * @return
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success() {
        return "success";
    }

    /**
     * 转发请求
     * @return
     */
    @RequestMapping("/redirect")
    public String redirect() {
        return "redirect:https://www.baidu.com/";
    }

    /**
     * 在页面上展示后端返回的结果
     * @param model
     * @return
     */
    @RequestMapping(value = "/showResultInView")
    public String showResultInView(Model model) {
        model.addAttribute("content", "在页面上展示后端返回的结果");
        return "success";
    }

    /**
     * 使用Map(Model、ModelMap)将数据返回
     * @param map
     * @return
     */
    @RequestMapping("/map")
    public String map(Map<String, Object> map) {
        map.put("time", new Date());
        return "success";
    }

    /**
     * 请求地址、请求方式 多重配置
     * @RequestMapping(value = {"/get", "post"}, method = {RequestMethod.GET,RequestMethod.POST})
     *  value = {"/get", "post"}: /get或者/post 都会请求到该方法
     *  method = {RequestMethod.GET,RequestMethod.POST}: 同时支持get和post请求
     * @param model
     * @return
     */
    @RequestMapping(value = {"/get", "/post"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String getOrPost(Model model) {
        model.addAttribute("content", "多个请求地址、多种请求方式 请求到同一个方法");
        return "success";
    }

    /**
     * ant风格的请求
     *  ?: 匹配任意一个字符
     *  *: 匹配一个字符串
     *  **:匹配人一个字符串，包含层级
     * 例如：/ant_1_rone/test/rone/ant、/ant_2_1/rone/ant
     * @param model
     */
    @RequestMapping("/ant_?_*/**/ant")
    public String ant(Model model) {
        model.addAttribute("content", "ant风格的请求");
        return "success";
    }

    /**
     * 带参数请求
     * @RequestParam    将参数绑定到形参
     *  value: 标注参数名字，如果不配置该参数则认为请求的参数名和形参变量名一致
     *  required: 该参数是否输入，默认true
     *  defaultValue: 当没有参数时的默认值
     * @param userName
     * @param passWord
     * @param channel
     * @param model
     * @return
     */
    @RequestMapping("/requestWithParam")
    public String requestWithParam(String userName,
                                   @RequestParam String passWord,
                                   @RequestParam(value="channel", required=false, defaultValue="web") String channel,
                                   Model model) {
        logger.debug("带参数请求 userName: {}, passWord: {}, channel: {}", userName, passWord, channel);
        return "success";
    }

    /**
     * 接收json格式的参数
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value="/requestWithJsonParam", method=RequestMethod.POST)
    public String requestWithJsonParam(@RequestBody User user, Model model) {
        model.addAttribute("content", user.toString());
        return "success";
    }

    /**
     * 通过@PathVariable可以将URL中的占位符参数绑定到控制器处理方法的参数中
     * @param param
     * @param model
     * @return
     */
    @RequestMapping("/pathVariable/{param}")
    public String pathVariable(@PathVariable("param") String param, Model model) {
        model.addAttribute("content", "URL中的占位符参数: " + param);
        return "success";
    }

    /**
     * REST(Representational State Transfer 资源表现层状态转化)风格的请求，通过定义请求的方式来明确该请求的功能
     * get：获取数据
     * post：新增数据
     * delete：删除数据
     * put：更新数据
     * @return
     */
    @RequestMapping(value="/rest", method=RequestMethod.GET)
    public String restGet(Model model) {
        model.addAttribute("content", "REST 的 GET 请求");
        return "success";
    }
    @RequestMapping(value="/rest", method=RequestMethod.POST)
    public String restPost(Model model) {
        model.addAttribute("content", "REST 的 POST 请求");
        return "success";
    }
    @RequestMapping(value="/rest", method=RequestMethod.DELETE)
    public String restDelete(Model model) {
        model.addAttribute("content", "REST 的 DELETE 请求");
        return "success";
    }
    @RequestMapping(value="/rest", method=RequestMethod.PUT)
    public String restPut(Model model) {
        model.addAttribute("content", "REST 的 PUT 请求");
        return "success";
    }

    /**
     * 返回值为ModelAndView，其中可以包含视图和模型数据，SpringMVC会把ModelAndView对象中的数据放入到request域对象中。
     * @return
     */
    @RequestMapping("/modelAndView")
    public ModelAndView modelAndView() {
        String viewName = "success";
        ModelAndView mv = new ModelAndView(viewName);
        mv.addObject("time", new Date());
        return mv;
    }

    /**
     * 在类上用@SessionAttributes(value="user", types= User.class)
     * 表明需要存在在session域中的数据
     * @param map
     * @return
     */
    @RequestMapping("/sessionAttributes")
    public String sessionAttributes(Map<String, Object> map) {
        User user = new User("123", "rone", "demo");
        map.put("user", user);
        map.put("others", "it is me!");
        return "success";
    }

    /**
     * 支持pojo类型的参数，属性名自动进行匹配，支持级联属性
     * @param user
     * @return
     */
    @RequestMapping("/pojo")
    public String pojo(User user, Model model) {
        model.addAttribute("content", "pojo: " + user);
        return "success";
    }

    /**
     * 使用原生的ServletAPI作为参数
     * @param req
     * @param resp
     * @param writer
     * @throws IOException
     */
    @RequestMapping("/servletAPI")
    public void servletAPI(HttpServletRequest req, HttpServletResponse resp, Writer writer) throws IOException {
        logger.debug("testServletAPI: {}, {}", req, resp);
        writer.write("hello world!");

        // 或者通过IOC的上下文对象来获取
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        HttpServletRequest request = servletRequestAttributes.getRequest();
    }

    /**
     * 直接返回json数据，也可在controller类上使用@RestController注解，表明所有的请求方法皆返回json数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/returnData")
    public User returnData() {
        return new User("123456", "rone@foxmail.com", "demo");
    }

    /**
     * @ModelAttribute 会在每个请求执行之前触发执行
     * tips：
     *  1.多个@ModelAttribute顺序执行
     *  2.@ModelAttribute不可添加在已有@RequestMapping注解的方法上，不然已有的请求会报404
     */
    @ModelAttribute
    public void modelAttribute() {
        // logger.debug("@ModelAttribute：在拦截器之后、业务方法之前 执行");
    }

    /**
     * 下载文件
     * @param response
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response) {
        try {
            roneService.downloadFile(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.reset();
            try {
                response.sendRedirect("/error.html");
            } catch (IOException e1) {
                logger.error(e1.getMessage(), e1);
            }
        }
    }
}
