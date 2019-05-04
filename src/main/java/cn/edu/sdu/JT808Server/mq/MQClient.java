package cn.edu.sdu.JT808Server.mq;

import java.io.File;

public class MQClient {
    public static void main(String[] args) {
        try {
            File file = new File("JT808ServerOriginData");
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    System.out.print("【创建JT808ServerOriginData文件夹失败】");
                }
            }
            File datePath = new File("JT808ServerShortLocationData");
            if (!datePath.exists()) {
                if (!datePath.mkdirs()) {
                    System.out.print("【创建JT808ServerOriginData文件夹失败】");
                }
            }

            new Thread(new ReceiveOriginDataWorker1()).start();
            new Thread(new ReceiveOriginDataWorker2()).start();
            new Thread(new ReceiveLocationDataWorker()).start();
            // TODO 从数据库取terminalPone循环赋值
            new Thread(new ReceiveShortLocationDataWorker("13827715822")).start();

        } catch (Exception e) {
            System.out.println("【程序退出】");
        }
    }
}
