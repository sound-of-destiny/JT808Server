package cn.edu.sdu.JT808Server.server;

import cn.edu.sdu.JT808Server.util.JT808Config;
import com.beust.jcommander.JCommander;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class Main {

    public static void init(String config) {
        if (config == null) {
            config = "conf.yml";
        }

        File configFile = Paths.get(config).toFile();
        if (!configFile.isFile()) {
            System.out.printf("%s 文件不存在.%n%n", config);
            return;
        }
        if (!configFile.canRead()) {
            System.out.printf("无法读取 %s.%n%n", config);
            return;
        }

        File file = new File("JT808ServerOriginData");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("创建原始数据文件夹成功");
            } else {
                System.out.println("创建原始数据文件夹失败");
            }
        }

        File pid = new File("jt808.pid");
        try (FileWriter fos = new FileWriter(pid)) {
            fos.write("" + ProcessHandle.current().pid());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JT808Config conf = JT808Server.loadProperties(configFile.toPath());
        new JT808Server(conf).setup().startServer();

        // 观察者模式 弃用
        //JT808Server.ServerListener serverListener = server.new ServerListener();
        //new Thread(JT808Server.ServerListener::new);
        //serverListener.addPropertyChangeListene r(serverListener);
    }

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander jc = JCommander.newBuilder()
                .addObject(args)
                .build();
        jc.parse(argv);
        if (args.help || args.config == null) {
            jc.usage();
            return;
        }
        init(args.config);
    }
}
