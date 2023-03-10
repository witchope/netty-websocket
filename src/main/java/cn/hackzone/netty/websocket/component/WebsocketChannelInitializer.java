package cn.hackzone.netty.websocket.component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * WebSocket 通道初始化器
 *
 * @author maxwell
 * @date 2023/03/10
 */
@Component
public class WebsocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PROTOCOL = "Websocket";

    @Value("${websocket.netty.path:/websocket}")
    private String webSocketPath;

    private final WebsocketHandler websocketHandler;

    public WebsocketChannelInitializer(WebsocketHandler websocketHandler) {
        this.websocketHandler = websocketHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(8192))
                .addLast(new WebSocketServerProtocolHandler(webSocketPath, WEBSOCKET_PROTOCOL))
                .addLast(websocketHandler)
        ;
    }

}
