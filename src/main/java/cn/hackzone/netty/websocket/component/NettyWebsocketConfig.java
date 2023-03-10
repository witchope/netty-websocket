package cn.hackzone.netty.websocket.component;


import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Netty WebSocket 配置
 *
 * @author maxwell
 * @date 2023/03/10
 */
@Component
public class NettyWebsocketConfig {

    private NettyWebsocketConfig() {
    }

    /**
     * 定义一个channel组，管理所有的channel
     * <p>
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存放用户与Chanel的对应信息，用于给指定用户发送消息
     */
    private static final ConcurrentHashMap<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();


    /**
     * 获取通道组
     *
     * @return {@link ChannelGroup}
     */
    public static ChannelGroup getChannelGroup() {
        return CHANNEL_GROUP;
    }

    /**
     * 获取用户信道映射
     *
     * @return {@link ConcurrentHashMap}<{@link String}, {@link Channel}>
     */
    public static ConcurrentMap<String, Channel> getUserChannelMap() {
        return USER_CHANNEL_MAP;
    }

}
