package test;

import org.junit.jupiter.api.Test;
import utils.FastDFSClient;

public class FastDfsTest {

   @Test
    public void uploadFast() throws Exception {
        // 1、把FastDFS提供的jar包添加到工程中
        // 2、初始化全局配置。加载一个配置文件。

          FastDFSClient fastDFSClient = new FastDFSClient();

        String filePath = fastDFSClient.uploadFile("C:\\Users\\Administrator\\Desktop\\blxflw.docx");
        System.out.println("返回路径：" + filePath);
    }
}