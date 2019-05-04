package cn.edu.sdu.JT808Server.service.handler.terminal;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;

import java.net.InetSocketAddress;

public class JT808ServerIpFilterHandler extends AbstractRemoteAddressFilter<InetSocketAddress> {

    private final IpFilterRule[] rules;

    public JT808ServerIpFilterHandler(IpFilterRule... rules) {
        if (rules == null) {
            throw new NullPointerException("rules");
        }

        this.rules = rules;
    }

    @Override
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
        for (IpFilterRule rule : rules) {
            if (rule == null) {
                break;
            }
            if (rule.matches(remoteAddress)) {
                return rule.ruleType() == IpFilterRuleType.ACCEPT;
            }
        }

        return true;
    }

    @Override
    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        return ctx.channel().close();
    }
}
