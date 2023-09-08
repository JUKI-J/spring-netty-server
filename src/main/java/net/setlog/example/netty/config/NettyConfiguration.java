package net.setlog.example.netty.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import net.setlog.example.netty.socket.BaseChannelInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NettyConfiguration {

    @Value("${server.netty.boss-count}")
    private int bossCount;
    @Value("${server.netty.worker-count}")
    private int workerCount;
    @Value("${server.netty.keep-alive}")
    private boolean keepAlive;
    @Value("${server.netty.backlog}")
    private int backlog;

    @Bean
    public ServerBootstrap serverBootstrap(BaseChannelInitializer baseChannelInitializer) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        if ( Epoll.isAvailable() ) {
            serverBootstrap.group(new EpollEventLoopGroup(bossCount), new EpollEventLoopGroup(workerCount))
                    .channel(EpollServerSocketChannel.class);
        } else {
            serverBootstrap.group(new NioEventLoopGroup(bossCount), new NioEventLoopGroup(workerCount))
                    .channel(NioServerSocketChannel.class);
        }

        serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(baseChannelInitializer)
                .option(ChannelOption.SO_BACKLOG, backlog);

        return serverBootstrap;
    }

}