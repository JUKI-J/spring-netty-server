package net.setlog.example.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketFrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if( byteBuf.readableBytes() < 1 ) {
            return;
        }

        list.add(byteBuf.readBytes( byteBuf.readableBytes() ));
    }

}
