package cn.hackzone.netty.websocket.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WebSocket 处理程序
 *
 * @author maxwell
 * @date 2023/03/10
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ObjectMapper objectMapper;

    public WebsocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("服务器添加信道 id : {}", ctx.channel().id().asLongText());
        NettyWebsocketConfig.getChannelGroup().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.info("服务器收到消息: {}", msg.text());
        // 获取用户名称
        User user = objectMapper.readValue(text, User.class);
        String name = user.getName();
        // 缓存信道
        NettyWebsocketConfig.getUserChannelMap().put(name, ctx.channel());
        // 给信道绑定用户标签
        AttributeKey<String> key = AttributeKey.valueOf("username");
        ctx.channel().attr(key).setIfAbsent(name);

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器连接成功！"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("服务器移除信道 id : {}", ctx.channel().id().asLongText());
        // 移除相关信道绑定关系
        NettyWebsocketConfig.getChannelGroup().remove(ctx.channel());
        removeUserChannel(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("信道异常: ",
                cause);
        NettyWebsocketConfig.getChannelGroup().remove(ctx.channel());
        removeUserChannel(ctx);
        ctx.close();
    }

    /**
     * 删除用户信道
     *
     * @param ctx 上下文
     */
    private static void removeUserChannel(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("username");
        String username = ctx.channel().attr(key).get();
        // 移除相关信道绑定关系
        NettyWebsocketConfig.getUserChannelMap().remove(username);
    }


}
