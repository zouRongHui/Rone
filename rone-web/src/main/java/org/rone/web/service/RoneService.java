package org.rone.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author rone
 */
@Service
public class RoneService {

    /** 通过 @Value 获取springboot配置文件中的配置信息 */
    @Value("${custom.demo}")
    private String rone;
    /** 通过 @Value 无法给static变量赋值 */
    @Value("${custom.demo}")
    private static String RONE;

    private final Environment environment;

    public RoneService(Environment environment) {
        this.environment = environment;
    }

    public void rone() {
        // 通过 Environment 获取配置文件里的配置信息
        System.out.println("通过Environment获取配置内容, " + environment.getProperty("custom.demo"));
        System.out.println("通过 @Value 获取springboot配置文件中的配置信息, " + rone);
        System.out.println("通过 @Value 无法给static变量赋值, " + RONE);
    }

}
