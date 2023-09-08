package net.setlog.example;

import lombok.RequiredArgsConstructor;
import net.setlog.example.netty.socket.NettyServerSocket;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringNettyServerApplication {

    private final NettyServerSocket nettyServerSocket;

    public static void main(String[] args) {
        SpringApplication.run(SpringNettyServerApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
                nettyServerSocket.start();
            }
        };
    }

}
