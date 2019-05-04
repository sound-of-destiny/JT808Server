package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.downMsg.ServerCommonResponseMsg;
import cn.edu.sdu.JT808Server.server.SessionManager;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;


public class JT808ServerDriverIdentityHandler extends SimpleChannelInboundHandler<ServerData.TerminalDriverIdentityMsg> {

    private static final Logger log = LoggerFactory.getLogger(JT808ServerDriverIdentityHandler.class);

    private static final String QUEUE_NAME = "JT808Server_DriverIdentity_Queue";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalDriverIdentityMsg msg) throws Exception {
        JT808ProtocolUtils.commonResponse(ctx, msg.getFlowId(),
                JT808Const.msg_id_Identity_information_upload, ServerCommonResponseMsg.success);

        log.info("【驾驶员身份信息上报】");

        String terminalPhone = SessionManager.getInstance().findBySessionId(ctx.channel().id().asLongText()).getTerminalPhone();
        ServerData.TerminalDriverIdentityMsg terminalDriverIdentityMsg = ServerData.TerminalDriverIdentityMsg.newBuilder()
                .setState(msg.getState())
                .setDriverName(msg.getDriverName())
                .setICCardInfo(msg.getICCardInfo())
                .setAuthorityName(msg.getAuthorityName())
                .setCardValidityTerm(msg.getCardValidityTerm())
                .setTime(msg.getTime()).build();
        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.select(RedisUtil.TerminalDriverIdentityMsg);
            jedis.set(terminalPhone.getBytes(), terminalDriverIdentityMsg.toByteArray());
        }

        MQUtil.toQueue(QUEUE_NAME, terminalDriverIdentityMsg.toByteArray());
    }
}
