package org.rone.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot启动器
 * @author Rone
 */
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("==========================================================================================");
        System.out.println("----------------------------------------程序已启动----------------------------------------");
        System.out.println("==========================================================================================");
        System.out.println("------------------------------------------------------------------------------------------");
    }
}
