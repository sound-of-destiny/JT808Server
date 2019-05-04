package cn.edu.sdu.JT808Server.server;

import cn.edu.sdu.JT808Server.util.JT808Config;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.MongoUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JT808Server {

    private static final Logger log = LoggerFactory.getLogger(JT808Server.class);

    private final JT808Config conf;

    private int terminalPort;
    private int webPort;
    private int testPort;

    public JT808Server(JT808Config config) {
        this.conf = config;
    }

    static JT808Config loadProperties(Path config) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(config)) {
            return yaml.loadAs(inputStream, JT808Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void bindTerminal() {
        EventLoopGroup bossGroup = new EpollEventLoopGroup();
        EventLoopGroup workerGroup = new EpollEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerTerminalInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(terminalPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("--->> JT808终端服务器启动成功, 端口号: " + terminalPort);
                } else {
                    log.info("====================JT808终端服务器启动失败====================");
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("JT808终端服务器启动出错", e);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void bindBrowser() {
        EventLoopGroup bossGroup = new EpollEventLoopGroup();
        EventLoopGroup workerGroup = new EpollEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerBrowserInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(testPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("--->> JT808WebSocket服务器启动成功, 端口号: " + testPort);
                } else {
                    log.info("====================JT808WebSocket服务器启动失败====================");
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("JT808WebSocket服务器启动出错", e);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void bindClient() {
        EventLoopGroup bossGroup = new EpollEventLoopGroup();
        EventLoopGroup workerGroup = new EpollEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerWebInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(webPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("--->> JT808Web服务器启动成功, 端口号: " + webPort);
                } else {
                    log.info("====================JT808Web服务器启动失败====================");
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("JT808Web服务器启动出错", e);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    synchronized void startServer() {
        new Thread(this::bindTerminal).start();
        new Thread(this::bindBrowser).start();
        new Thread(this::bindClient).start();
    }

    JT808Server setup() {
        terminalPort = conf.getTerminalPort();
        webPort = conf.getWebPort();
        testPort = conf.getTestPort();

        MQUtil.host = conf.getMqHost();
        MQUtil.virtualHost = conf.getMqVirtualHost();
        MQUtil.username = conf.getMqUsername();
        MQUtil.password = conf.getMqPassword();

        RedisUtil.REDIS_HOST = conf.getRedisHost();

        MongoUtil.mongoServer = conf.getMongoServer();
        MongoUtil.mongodb = conf.getMongodb();
        return this;
    }
}
