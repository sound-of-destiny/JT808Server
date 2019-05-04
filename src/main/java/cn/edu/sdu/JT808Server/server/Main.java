package cn.edu.sdu.JT808Server.server;

import cn.edu.sdu.JT808Server.util.JT808Config;
import com.beust.jcommander.JCommander;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] argv) {
        Args args = new Args();
        JCommander jc = JCommander.newBuilder()
                .addObject(args)
                .build();
        jc.parse(argv);
        if (args.help) {
            jc.usage();
            return;
        }

        File configFile = Paths.get(args.config).toFile();
        if (!configFile.isFile()) {
            System.out.printf("%s 文件不存在.%n%n", args.config);
            jc.usage();
            return;
        }
        if (!configFile.canRead()) {
            System.out.printf("无法读取 %s.%n%n", args.config);
            jc.usage();
            return;
        }
        File file = new File("JT808ServerOriginData");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("【创建文件夹成功】");
            } else {
                System.out.println("【创建文件夹失败】");
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

    }
}
