// package org.rone.web.config;
//
// import org.eclipse.jetty.server.NCSARequestLog;
// import org.eclipse.jetty.server.Server;
// import org.eclipse.jetty.util.thread.QueuedThreadPool;
// import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
// import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * jetty服务器的配置
//  * https://blog.csdn.net/m0_37034294/article/details/80800075
//  * @author rone
//  */
// @Configuration
// public class JettyConfig {
//
//     @Bean
//     public JettyServletWebServerFactory jettyServletWebServerFactory(JettyServerCustomizer jettyServerCustomizer) {
//         JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
//         factory.addServerCustomizers(jettyServerCustomizer);
//         return factory;
//     }
//
//     @Bean
//     public JettyServerCustomizer jettyServerCustomizer() {
//         return server -> {
//             threadPool(server);
//             requestLog(server);
//         };
//     }
//
//     private void threadPool(Server server) {
//         final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
//         // 最大并发值
//         threadPool.setMaxThreads(1000);
//         // 常驻线程数
//         threadPool.setMinThreads(5);
//         // 线程最大空闲时间，超时销毁
//         threadPool.setIdleTimeout(60 * 1000);
//         // 线程名
//         threadPool.setName("rone");
//     }
//
//     private void requestLog(Server server) {
//         // jetty日志
//         NCSARequestLog requestLog = new NCSARequestLog("/home/log/rone/jetty.log");
//         requestLog.setAppend(true);
//         requestLog.setExtended(false);
//         requestLog.setLogTimeZone("GMT+08");
//         requestLog.setLogLatency(true);
//         requestLog.setRetainDays(30);
//         server.setRequestLog(requestLog);
//     }
// }
