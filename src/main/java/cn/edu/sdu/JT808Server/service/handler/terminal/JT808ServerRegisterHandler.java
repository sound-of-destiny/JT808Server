package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.downMsg.TerminalRegisterResponseMsg;
import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalRegisterMsg;
import cn.edu.sdu.JT808Server.server.ChannelGroupManager;
import cn.edu.sdu.JT808Server.util.JT808Mysql;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

@Slf4j
public class JT808ServerRegisterHandler extends SimpleChannelInboundHandler<TerminalRegisterMsg> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalRegisterMsg msg) throws Exception {

        log.info("【终端注册】 {}", ctx.channel().remoteAddress());

        TerminalRegisterResponseMsg responseMsg = new TerminalRegisterResponseMsg();
        responseMsg.setReplyFlowId(msg.getMsgHeader().getFlowId());
        String authenticationCode = UUID.randomUUID().toString();

        if (ChannelGroupManager.getInstance().findByChannelGroupName("TerminalMsg") == null)
            ChannelGroupManager.getInstance().put("TerminalMsg", channelGroup);
        ChannelGroupManager.getInstance().findByChannelGroupName("TerminalMsg").add(ctx.channel());

        int provinceId = msg.getProvinceId();
        int cityId =  msg.getCityId();
        String manufacturerId = msg.getManufacturerId().trim();
        String terminalType = msg.getTerminalType().trim();
        String terminalId = msg.getTerminalId().trim();
        int licensePlateColor = msg.getLicensePlateColor();
        String licensePlate = msg.getLicensePlate().trim();

        Connection connection = JT808Mysql.connect_145();
        Statement statement = connection.createStatement();
        String sql = "select * from jt808server_register_msg where licensePlate = \'" + licensePlate + "\'";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            responseMsg.setReplyCode(TerminalRegisterResponseMsg.car_already_registered);
        } else {
            String sql1 = "select * from jt808server.jt808server_register_msg where terminalId = \'" + terminalId + "\'";
            ResultSet resultSet1 = statement.executeQuery(sql1);
            if (resultSet1.next()) {
                responseMsg.setReplyCode(TerminalRegisterResponseMsg.terminal_already_registered);
            } else {
                String sql2 = "select * from jt808server.vehicle_info where plateNum = \'" + licensePlate + "\'";
                ResultSet resultSet2 = statement.executeQuery(sql2);
                if (!resultSet2.next()) {
                    responseMsg.setReplyCode(TerminalRegisterResponseMsg.car_not_found);
                } else {
                    String sql3= "select * from jt808server.terminal_info where terminalId = \'" + terminalId + "\'";
                    ResultSet resultSet3 = statement.executeQuery(sql3);
                    if (!resultSet3.next()) {
                        responseMsg.setReplyCode(TerminalRegisterResponseMsg.terminal_not_found);
                    } else {
                        String sql4 = String.format("insert into jt808server.jt808server_register_msg (authenticationCode, provinceId, cityId, " +
                                "manufacturerId, terminalType, terminalId, licensePlateColor, licensePlate) values ('%s', %d, %d, '%s', '%s', '%s', %d, '%s')",
                                authenticationCode, provinceId, cityId, manufacturerId, terminalType, terminalId, licensePlateColor, licensePlate);
                        statement.execute(sql4);
                        responseMsg.setReplyCode(TerminalRegisterResponseMsg.success);
                    }
                }
            }
        }
        statement.close();
        connection.close();

        String terminalPhone = msg.getMsgHeader().getTerminalPhone();

        JT808ProtocolUtils.sessionUpdate(ctx, msg, authenticationCode, terminalPhone);

        responseMsg.setReplyToken(authenticationCode);
        ctx.writeAndFlush(responseMsg);
    }

}
