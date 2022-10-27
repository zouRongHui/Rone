package org.rone.web;

import org.mybatis.spring.annotation.MapperScan;
import org.rone.web.filter.RoneFilter;
import org.rone.web.filter.XssFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication   springBoot启动器
 * @EnableScheduling
 * @ServletComponentScan	声明可通过注解注册Servlet、Filter、Listener，通过配置的方式注册见下文
 * @author Rone
 */
@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("==========================================================================================");
        System.out.println("----------------------------------------程序已启动----------------------------------------");
        System.out.println("==========================================================================================");
        System.out.println("------------------------------------------------------------------------------------------");
    }

	// /**
	//  * 通过配置类注册filter
	//  * @return
	//  */
	// @Bean
	// public FilterRegistrationBean registerFirstFilter() {
	// 	FilterRegistrationBean bean = new FilterRegistrationBean(new XssFilter());
	// 	bean.addUrlPatterns("/*");
	// 	bean.setOrder(1);
	// 	return bean;
	// }
    //
	// /**
	//  * 通过配置类注册filter
	//  * @return
	//  */
	// @Bean
	// public FilterRegistrationBean registerSecondFilter() {
	// 	FilterRegistrationBean bean = new FilterRegistrationBean(new RoneFilter());
	// 	bean.addUrlPatterns("/*");
	// 	bean.setOrder(1);
	// 	return bean;
	// }
}
