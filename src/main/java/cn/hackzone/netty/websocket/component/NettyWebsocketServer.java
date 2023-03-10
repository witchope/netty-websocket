package cn.hackzone.netty.websocket.component;

import cn.hackzone.netty.websocket.concurrent.ThreadPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * Netty WebSocket 服务器
 *
 * @author maxwell
 * @date 2023/03/08
 */
@Component
@Slf4j
public class NettyWebsocketServer {
    /**
     * 端口
     */
    @Value("${websocket.netty.port:58080}")
    private int port;
    /**
     * 老板组
     */
    private EventLoopGroup bossGroup;
    /**
     * 工作组
     */
    private EventLoopGroup workGroup;
    private final ThreadPool threadPool;
    private final WebsocketChannelInitializer websocketChannelInitializer;

    public NettyWebsocketServer(WebsocketChannelInitializer websocketChannelInitializer, ThreadPool threadPool) {
        this.websocketChannelInitializer = websocketChannelInitializer;
        this.threadPool = threadPool;
    }


    public void start() throws InterruptedException {
        log.info("websocket 服务器启动中");
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(new InetSocketAddress(port));
        bootstrap.childHandler(websocketChannelInitializer);

        ChannelFuture future = bootstrap.bind().sync();
        future.channel().closeFuture().sync();
    }

    @PreDestroy
    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }


    @PostConstruct
    public void init() {
        threadPool.submit(() -> {
            try {
                start();
            } catch (InterruptedException e) {
                log.error("websocket 服务器启动异常", e);
                Thread.currentThread().interrupt();
            }
        });
    }


}
