package cn.edu.sdu.JT808Server.service.codec.terminal;

import cn.edu.sdu.JT808Server.protocol.Session;
import cn.edu.sdu.JT808Server.protocol.downMsg.TerminalRegisterResponseMsg;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.server.SessionManager;
import cn.edu.sdu.JT808Server.util.BitOperator;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class JT808ServerRegisterEncoder extends MessageToByteEncoder<TerminalRegisterResponseMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TerminalRegisterResponseMsg msg, ByteBuf out) throws Exception {

        // 消息体字节数组
        byte[] msgBody;
        // 鉴权码(STRING) 只有在成功后才有该字段
        if (msg.getReplyCode() == TerminalRegisterResponseMsg.success) {
            log.info("【注册成功】 {}", msg.getReplyToken());
            msgBody = BitOperator.concatAll(Arrays.asList(
                    BitOperator.integerTo2Bytes(msg.getReplyFlowId()),          // 流水号(2)
                    BitOperator.integerTo1Bytes(msg.getReplyCode()),            // 结果
                    msg.getReplyToken().getBytes(JT808Const.string_charset)     // 鉴权码(STRING)
            ));
        } else {
            log.info("【注册失败】 {}", msg.getReplyCode());
            msgBody = BitOperator.concatAll(Arrays.asList(
                    BitOperator.integerTo2Bytes(msg.getReplyFlowId()),          // 流水号(2)
                    BitOperator.integerTo1Bytes(msg.getReplyCode())             // 错误代码
            ));
        }

        Session session = SessionManager.getInstance().findBySessionId(ctx.channel().id().asLongText());
        out.writeBytes(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminal(msgBody, BusinessManager.getInstance().currentFlowId(),
                session.getTerminalPhone(), JT808Const.cmd_terminal_register_resp)));
    }


}
