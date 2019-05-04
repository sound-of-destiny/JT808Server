package cn.edu.sdu.JT808Server.server;

import cn.edu.sdu.JT808Server.service.codec.terminal.JT808ServerDecoder;
import cn.edu.sdu.JT808Server.service.codec.terminal.JT808ServerRegisterEncoder;
import cn.edu.sdu.JT808Server.service.handler.terminal.*;
import cn.edu.sdu.JT808Server.util.JT808Const;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class JT808ServerTerminalInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new JT808ServerIpFilterHandler(new IpSubnetFilterRule("39.107.231.159", 32, IpFilterRuleType.REJECT)));
        pipeline.addLast(new IdleStateHandler(JT808Const.tcp_client_idle_seconds, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new DelimiterBasedFrameDecoder(1240,
                Unpooled.copiedBuffer(new byte[] {JT808Const.pkg_delimiter}),
                Unpooled.copiedBuffer(new byte[] {JT808Const.pkg_delimiter, JT808Const.pkg_delimiter})));
        pipeline.addLast(new JT808ServerDecoder());
        pipeline.addLast(new JT808ServerRegisterEncoder());
        pipeline.addLast(new JT808ServerAuthenticationHandler());

        pipeline.addLast(new JT808ServerCommonHandler());
        pipeline.addLast(new JT808ServerBeatHandler());
        pipeline.addLast(new JT808ServerLocationHandler());
        pipeline.addLast(new JT808ServerBulkLocationHandler());

        pipeline.addLast(new JT808ServerLocationRequestHandler());
        pipeline.addLast(new JT808ServerDriverIdentityHandler());
        pipeline.addLast(new JT808ServerParamQueryHandler());
        pipeline.addLast(new JT808ServerAttributeQueryResponseHandler());
        pipeline.addLast(new JT808ServerCarControlResponseHandler());
        pipeline.addLast(new JT808ServerCameraPhotoHandler());

        pipeline.addLast(new JT808ServerMsgRequestHandler());
        pipeline.addLast(new JT808ServerDigitWaybillHandler());

        pipeline.addLast(new JT808ServerMediaDataHandler());
        pipeline.addLast(new JT808ServerMediaEventHandler());
        pipeline.addLast(new JT808ServerSavedMediaResponseHandler());

        pipeline.addLast(new JT808ServerMediaAttributeHandler());
        pipeline.addLast(new JT808ServerMediaResourcesQueryHandler());

        pipeline.addLast(new JT808ServerRegisterHandler());

    }
}
