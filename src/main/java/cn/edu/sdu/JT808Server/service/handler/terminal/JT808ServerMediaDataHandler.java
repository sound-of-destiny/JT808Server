package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.util.MQUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Slf4j
public class JT808ServerMediaDataHandler extends SimpleChannelInboundHandler<ServerData.TerminalMediaDataMsg> {

    private final static String EXCHANGE_NAME = "JT808Server_Photo_Exchange";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalMediaDataMsg msg) throws Exception {
        log.info(msg.toString());
        File dir = new File("photos/" + msg.getTerminalPhone());
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("【创建图片文件夹失败】");
                return;
            }
        }
        File file = new File("photos/" + msg.getTerminalPhone() + "/" +
                msg.getTerminalLocationMsg().getTime() + ".jpg");
        DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
        fos.write(msg.getMediaData().toByteArray());
        fos.flush();
        fos.close();


    }
}
