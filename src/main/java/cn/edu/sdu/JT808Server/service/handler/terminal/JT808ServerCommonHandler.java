package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalCommonResponseMsg;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerCommonHandler extends SimpleChannelInboundHandler<TerminalCommonResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalCommonResponseMsg msg) throws Exception {
        log.info("【终端通用应答】　" + msg);
        int replyFlowId = msg.getReplyFlowId();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);
    }

}
