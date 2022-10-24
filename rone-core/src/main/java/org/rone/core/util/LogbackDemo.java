package org.rone.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * logback，详细查看 logback.xml 文件
 * @author rone
 */
public class LogbackDemo {

    private static Logger logger = LoggerFactory.getLogger(LogbackDemo.class);

    public static void main(String[] args) {
        logger.debug("logback的debug级别日志");
        logger.info("logback的info级别日志");
        logger.warn("logback的warn级别日志");
        logger.error("logback的error级别日志");

        logger.info("信息记录：{}", "具体的信息");
    }
}
