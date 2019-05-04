package redis;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;

public class JT808ServerRedisClient implements Runnable {
    private static final String HOST = "211.87.225.206";
    private static final int PORT = 6379;
    private static EventLoopGroup group = new EpollEventLoopGroup();
    public static Channel channel = null;

    public Channel getChannel() {
        if (channel != null)
            return channel;
        /*else {
            try {
                Bootstrap b = new Bootstrap();
                b.group(group).channel(EpollSocketChannel.class)
                        .handler(new JT808ServerRedisInitializer());
                channel = b.connect(HOST, PORT).sync().channel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return channel;
    }

    public void run() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(EpollSocketChannel.class)
                    .handler(new JT808ServerRedisInitializer());
            channel = b.connect(HOST, PORT).sync().channel();
            for (;;) {
                Thread.sleep(100000);
                channel.writeAndFlush("ping");
            }
        } catch (Exception e) {
            System.out.println("JT808WebRedis客户端启动出错");
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void destroyConnection() {
        group.shutdownGracefully();
    }
}
