package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastdfs {

    //上传文件
    @Test
    public void test()  {
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建客户端
            TrackerClient tc = new TrackerClient();
            //连接
            TrackerServer ts = tc.getConnection();
            //获取一个ts
            StorageServer ss = tc.getStoreStorage(ts);
            //创建一个存储的客户端
            StorageClient1 storageClient1 = new StorageClient1(ts, ss);
            //本地文件路径这个是图片的路径
            String path="F:\\图片\\下载.jpg";
            String jpg = storageClient1.upload_file1(path, "jpg", null);
            System.out.println(jpg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载文件
    @Test
    public void Test01(){
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建客户端
            TrackerClient tc = new TrackerClient();
            //连接
            TrackerServer ts = tc.getConnection();
            //获取存储系统  同是他需要指定一个调用他的，就是给他上传状态
            StorageServer ss = tc.getStoreStorage(ts);
            //建立连接
            StorageClient1 storageClient1 = new StorageClient1(ts, ss);
            byte[] bytes = storageClient1.download_file1("group1/M00/00/01/wKgZmV3mBdqAKYt1AAA_2vRnn9g541.jpg");
            FileOutputStream file=new FileOutputStream(new File("d:/logo.jpg"));
            file.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //查询文件
    @Test
    public void test003(){
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(connection);
            StorageClient1 storageClient1 = new StorageClient1(connection, storeStorage);

            FileInfo fileInfo = storageClient1.query_file_info1("group1/M00/00/01/wKgZmV3l-2-AcUryAAA_2vRnn9g872.jpg");
            System.out.println(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
