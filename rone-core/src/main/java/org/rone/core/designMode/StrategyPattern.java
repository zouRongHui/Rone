package org.rone.core.designMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略模式
 * ●.设计一些算法，把它们封装起来，让它们可以相互替换，这样就可以轻松地切换不同的算法来解决同一个问题。
 * ●.避免了使用多重if{}else{}、switch语句，同时有具有良好的扩展性
 * ●.实例
 *     java中 Comparator 接口，List.sort(Comparator) 排序方法
 * @author rone
 */
public class StrategyPattern {

    /**
     * 示例为：业务分享(分享到微信、微博、QQ等)
     */
    public static void main(String[] args) {
        Share.shareOptions(ShareChannel.SINA);
        Share.shareOptions(ShareChannel.WE_CHAT);
    }

    /**
     * 定义策略接口
     */
    interface DealStrategy{
        void dealMethod(String option);
    }

    /**
     * 定义具体的策略1
     */
    static class DealSina implements DealStrategy{
        @Override
        public void dealMethod(String option){
            System.out.println("分享到新浪微博.....");
        }
    }

    /**
     * 定义具体的策略2
     */
    static class DealWeChat implements DealStrategy{
        @Override
        public void dealMethod(String option){
            System.out.println("分享到腾讯微信.....");
        }
    }

    /**
     * 分享渠道
     */
    enum ShareChannel {
        /** 新浪微博 */
        SINA,
        /** 微信 */
        WE_CHAT
    }

    /**
     * 定义上下文，负责使用DealStrategy角色
     */
    static class DealContext{
        private final ShareChannel shareChannel;
        private final DealStrategy deal;
        public  DealContext(ShareChannel shareChannel,DealStrategy deal){
            this.shareChannel = shareChannel;
            this.deal = deal;
        }
        public DealStrategy getDeal(){
            return deal;
        }
        public boolean options(ShareChannel shareChannel){
            return this.shareChannel.equals(shareChannel);
        }
    }

    /**
     * 具体的分享聚合对象
     */
    static class Share{
        private static final List<DealContext> DEAL_CONTEXT_LIST = new ArrayList<>();
        //静态代码块,先加载所有的策略
        static {
            DEAL_CONTEXT_LIST.add(new DealContext(ShareChannel.SINA,new DealSina()));
            DEAL_CONTEXT_LIST.add(new DealContext(ShareChannel.WE_CHAT,new DealWeChat()));
        }
        public static void shareOptions(ShareChannel shareChannel){
            DealStrategy dealStrategy = null;
            for (DealContext deal : DEAL_CONTEXT_LIST) {
                if (deal.options(shareChannel)) {
                    dealStrategy = deal.getDeal();
                    break;
                }
            }
            if (dealStrategy != null) {
                dealStrategy.dealMethod("参数");
            }
        }
    }
}
