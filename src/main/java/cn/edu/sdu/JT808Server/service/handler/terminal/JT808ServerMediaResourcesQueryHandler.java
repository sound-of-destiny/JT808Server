package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerMediaResourcesQueryHandler extends SimpleChannelInboundHandler<ServerData.TerminalUploadMediaResourcesMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalUploadMediaResourcesMsg msg) throws Exception {
        int replyFlowId = msg.getReplyFlowId();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);

        log.info("数量 {}", msg.getMediaResourcesNum());
        int num = msg.getMediaResourcesNum();
        for (int i = 0; i < num; i++) {
            log.info(msg.getMediaResourcesList().get(i).toString());
        }
    }
}
