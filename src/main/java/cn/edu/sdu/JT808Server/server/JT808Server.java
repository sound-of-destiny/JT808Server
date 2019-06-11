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
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JT808Server {

    private final JT808Config conf;

    private int terminalPort;
    private int clientPort;
    private int testPort;

    //private Channel terminalServerSocketChannel;
    //private Channel clientServerSocketChannel;
    //private Channel testServerSocketChannel;

    /*private PropertyChangeSupport changes = new PropertyChangeSupport(JT808Server.class);
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }*/

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup terminalWorkerGroup;
    private final EventLoopGroup clientWorkerGroup;
    private final EventLoopGroup webSocketWorkerGroup;

    public JT808Server(JT808Config config) {
        this.conf = config;
        bossGroup = new EpollEventLoopGroup(3);
        terminalWorkerGroup = new EpollEventLoopGroup();
        clientWorkerGroup = new EpollEventLoopGroup();
        webSocketWorkerGroup = new EpollEventLoopGroup();
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
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, terminalWorkerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerTerminalInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(terminalPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("---> JT808终端服务器启动成功, 端口号: " + terminalPort);
                } else {
                    log.info("---> JT808终端服务器启动失败");
                }
            });
            //terminalServerSocketChannel = channelFuture.channel();
        } catch (Exception e) {
            log.error("JT808终端服务器启动出错", e);
            /*workerGroup.shutdownGracefully();
            changes.firePropertyChange("terminalServerStarted", true, false);*/
        }

    }

    private void bindBrowser() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, webSocketWorkerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerBrowserInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(testPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("---> JT808WebSocket服务器启动成功, 端口号: " + testPort);
                } else {
                    log.info("---> JT808WebSocket服务器启动失败");
                }
            });
            //testServerSocketChannel = channelFuture.channel();
        } catch (Exception e) {
            log.error("JT808WebSocket服务器启动出错", e);
        }
    }

    private void bindClient() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, clientWorkerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new JT808ServerWebInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(clientPort).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("---> JT808Web服务器启动成功, 端口号: " + clientPort);
                } else {
                    log.info("---> JT808Web服务器启动失败");
                }
            });

            //clientServerSocketChannel = channelFuture.channel();
            /*ChannelFuture closeFuture = clientServerSocketChannel.closeFuture().sync();
            closeFuture.addListener(future -> {
               if (future.isSuccess()) bindClient();
            });*/
        } catch (InterruptedException e) {
            log.error("JT808Web服务器启动出错", e);
        }
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    synchronized void startServer() {
        executorService.submit(this::bindTerminal);
        executorService.submit(this::bindBrowser);
        executorService.submit(this::bindClient);
    }

    JT808Server setup() {
        terminalPort = conf.getTerminalPort();
        clientPort = conf.getClientPort();
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

    private synchronized void stopServer() {
        terminalWorkerGroup.shutdownGracefully();
        clientWorkerGroup.shutdownGracefully();
        webSocketWorkerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        executorService.shutdown();
    }

    /*private synchronized void restartServer() {
        terminalWorkerGroup.shutdownGracefully();
        clientWorkerGroup.shutdownGracefully();
        webSocketWorkerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        startServer();
    }

    private void rebindClient() {
        bindClient();
    }

    private void rebindBrowser() {
        bindBrowser();
    }

    private void rebindTerminal() {
        bindTerminal();
    }


    */
    /**
     * 观察者模式 弃用
     */
    /*
    public class ServerListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case "stopServer":
                    stopServer();
                    break;
                case "restartServer":
                    restartServer();
                    break;
                case "restartTerminalServer":
                    rebindTerminal();
                    break;
                case  "restartWebServer":
                    rebindClient();
                    break;
                case  "restartWebsocketServer":
                    rebindBrowser();
                    break;
                default:
                    break;
            }
        }
    }*/
}
