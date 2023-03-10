package cn.hackzone.netty.websocket;

import cn.hackzone.netty.websocket.concurrent.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Netty WebSocket 应用程序启动入口
 *
 * @author maxwell
 * @date 2023/03/10
 */
@SpringBootApplication
@Slf4j
public class NettyWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyWebsocketApplication.class, args);
    }

    @Bean
    ThreadPool threadPool() {
        return new ThreadPool(2, 10);
    }

}
