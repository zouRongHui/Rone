package org.rone.web.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ApplicationEvent 事件监听处理
 * @author zouRongHui
 * @date 2023/4/10
 */
@Component
public class DemoEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DemoEventListener.class);

    /**
     * 事件监听的处理处理方法
     * 异步线程池使用 {@link org.rone.web.config.ThreadAsyncConfig} 的配置
     * @param event
     */
    @Async  // 该监听事件的处理逻辑为异步处理
    @EventListener
    public void handleEvent(DemoEvent event) {
        logger.info("监听到 {} 事件啦...", event.getClass());
    }

    /**
     * 被监听的事件对象
     */
    public static class DemoEvent {

    }
}
