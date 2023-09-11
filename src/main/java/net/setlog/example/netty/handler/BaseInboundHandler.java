package net.setlog.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class BaseInboundHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buff;

    private final ChannelGroup channels;

    public BaseInboundHandler() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        for(Channel channel : channels){
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has joined!\r\n");
        }
        channels.add( incoming );
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        for(Channel channel : channels){
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has left!\r\n");
        }
        channels.remove( incoming );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("channel activated : {}", ctx.channel());
        ctx.writeAndFlush("channel activated : " + ctx.channel() + "\r\n");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("channel deactivated : {}", ctx.channel());
        ctx.writeAndFlush("channel deactivated : " + ctx.channel() + "\r\n");
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        log.debug("[{}] : {}", ctx.channel().remoteAddress(), msg);

        for(Channel channel : channels) {
            String message = "";
            if( ctx.channel().id() != null
                    && channel.id().equals(ctx.channel().id()) )
            {
                message = String.format("[ ME ] : " + msg + "\r\n");
            }else{
                message = String.format("["+ctx.channel().remoteAddress()+"] : " + msg + "\r\n");
            }

            channel.writeAndFlush( message );
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[{}] raised exception : {}", ctx.channel(), cause.getMessage());
        ctx.close();
        cause.printStackTrace();
    }
}
