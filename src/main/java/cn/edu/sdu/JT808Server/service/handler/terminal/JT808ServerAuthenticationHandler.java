package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalAuthenticationMsg;
import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalRegisterMsg;
import cn.edu.sdu.JT808Server.server.ChannelGroupManager;
import cn.edu.sdu.JT808Server.server.PackageManager;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerAuthenticationHandler extends SimpleChannelInboundHandler<TerminalAuthenticationMsg> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalAuthenticationMsg msg) throws Exception {

        log.info("【鉴权处理】 {}", ctx.channel().remoteAddress());

        int replyCode = 0;

        String terminalPhone = msg.getMsgHeader().getTerminalPhone();
        String authenticationCode = msg.getAuthCode();

        log.info("【鉴权成功】 {}", authenticationCode);
        TerminalRegisterMsg terminalRegisterMsg = new TerminalRegisterMsg();

        if (ChannelGroupManager.getInstance().findByChannelGroupName("Terminal") == null)
            ChannelGroupManager.getInstance().put("Terminal", channelGroup);
        ChannelGroupManager.getInstance().findByChannelGroupName("Terminal").add(ctx.channel());

        JT808ProtocolUtils.sessionUpdate(ctx, terminalRegisterMsg, authenticationCode, terminalPhone);
        PackageManager.getInstance().removeByTerminalPhone(terminalPhone);

        JT808ProtocolUtils.commonResponse(ctx, msg.getMsgHeader().getFlowId(), msg.getMsgHeader().getMsgId(), replyCode);
    }

}
