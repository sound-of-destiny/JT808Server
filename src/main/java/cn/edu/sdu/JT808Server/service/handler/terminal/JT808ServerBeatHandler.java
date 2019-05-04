package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.downMsg.ServerCommonResponseMsg;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JT808ServerBeatHandler extends SimpleChannelInboundHandler<MsgHeader> {

    private static final Logger log = LoggerFactory.getLogger(JT808ServerBeatHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgHeader msg) throws Exception {

        int msgId = msg.getMsgId();
        switch (msgId) {
            case JT808Const.msg_id_terminal_heart_beat:
                log.info("【　心跳　】　来自 ====================>> {}", ctx.channel().remoteAddress());
                JT808ProtocolUtils.commonResponse(ctx, msg.getFlowId(),
                        JT808Const.msg_id_terminal_heart_beat, ServerCommonResponseMsg.success);
                break;
            case JT808Const.msg_id_terminal_log_out:
                log.info("【终端注销】　来自 ====================>> {}", ctx.channel().remoteAddress());
                JT808ProtocolUtils.commonResponse(ctx, msg.getFlowId(),
                        JT808Const.msg_id_terminal_log_out, ServerCommonResponseMsg.success);
                // todo 终端注销处理

                break;
            default:
        }
    }
}
