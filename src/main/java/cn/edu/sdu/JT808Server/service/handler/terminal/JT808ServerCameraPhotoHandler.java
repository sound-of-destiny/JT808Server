package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerCameraPhotoHandler extends SimpleChannelInboundHandler<ServerData.TerminalCameraPhotoResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalCameraPhotoResponseMsg msg) throws Exception {
        log.info("【摄像头立即拍照应答】");
        int replyFlowId = msg.getReplyFlowId();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);
    }
}
