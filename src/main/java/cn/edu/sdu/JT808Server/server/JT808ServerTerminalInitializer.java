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

        pipeline.addLast("ipFilter", new JT808ServerIpFilterHandler(new IpSubnetFilterRule("39.107.231.159", 32, IpFilterRuleType.REJECT)));
        pipeline.addLast("idle", new IdleStateHandler(JT808Const.tcp_client_idle_seconds, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast("delimiter", new DelimiterBasedFrameDecoder(1240,
                Unpooled.copiedBuffer(new byte[] {JT808Const.pkg_delimiter}),
                Unpooled.copiedBuffer(new byte[] {JT808Const.pkg_delimiter, JT808Const.pkg_delimiter})));

        pipeline.addLast("mainDecoder", new JT808ServerDecoder());

        pipeline.addLast("common", new JT808ServerCommonHandler());
        pipeline.addLast("heartBeat", new JT808ServerBeatHandler());
        pipeline.addLast("location", new JT808ServerLocationHandler());
        pipeline.addLast("bulkLocation", new JT808ServerBulkLocationHandler());

        pipeline.addLast("authentication", new JT808ServerAuthenticationHandler());

        pipeline.addLast("locationRequest", new JT808ServerLocationRequestHandler());
        pipeline.addLast("driver", new JT808ServerDriverIdentityHandler());
        pipeline.addLast("parameter", new JT808ServerParamQueryHandler());
        pipeline.addLast("attribute", new JT808ServerAttributeQueryResponseHandler());
        pipeline.addLast("carControl", new JT808ServerCarControlResponseHandler());
        pipeline.addLast("photo", new JT808ServerCameraPhotoHandler());

        pipeline.addLast("message", new JT808ServerMsgRequestHandler());
        pipeline.addLast("digitWayBill", new JT808ServerDigitWaybillHandler());

        pipeline.addLast("mediaData", new JT808ServerMediaDataHandler());
        pipeline.addLast("mediaEvent", new JT808ServerMediaEventHandler());
        pipeline.addLast("savedMedia", new JT808ServerSavedMediaResponseHandler());

        pipeline.addLast("mediaAttribute", new JT808ServerMediaAttributeHandler());
        pipeline.addLast("mediaResource", new JT808ServerMediaResourcesQueryHandler());

        pipeline.addLast("registerEncoder", new JT808ServerRegisterEncoder());
        pipeline.addLast("register", new JT808ServerRegisterHandler());

    }
}
