package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.util.MQUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerDigitWaybillHandler extends SimpleChannelInboundHandler<ServerData.TerminalDigitWaybill> {

    private static final String QUEUE_NAME = "JT808Server_DigitWaybill_Queue";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalDigitWaybill msg) throws Exception {
        log.info("【电子运单上报】");
        MQUtil.toQueue(QUEUE_NAME, msg.toByteArray());
    }
}
