package cn.hackzone.netty.websocket.service;

import cn.hackzone.netty.websocket.component.NettyWebsocketConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * 推送消息服务
 *
 * @author maxwell
 * @date 2023/03/10
 */
@Component
public class PushMsgService {

    public void pushMsgToAll(String msg) {
        NettyWebsocketConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void pushMsg(String username, String msg) {
        Channel channel = NettyWebsocketConfig.getUserChannelMap().get(username);
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

}
