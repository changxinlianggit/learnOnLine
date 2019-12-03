package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepostiory;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    @Autowired
    FileSystemRepostiory repostiory;

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;  //连接超时时间

    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;  //两个程序通信超时时间

    @Value("${xuecheng.fastdfs.charset}")
    String charset;  //字符集

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers; // 连接地址


    //文件上传
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //上传文件到FastdfsR
        String fileid = uploadRowbackId(multipartFile);
        if (fileid == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //把文件的信息保存到mongDB
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileid);//文件的id
        fileSystem.setFileName(multipartFile.getOriginalFilename());//文件的名称
        fileSystem.setFilePath(fileid);  //文件的路径
        fileSystem.setFiletag(filetag); //标签
        fileSystem.setBusinesskey(businesskey);  //业务标识
        fileSystem.setFileSize(multipartFile.getSize()); //文件的大小
        fileSystem.setFileType(multipartFile.getContentType());// 文件的类型
        //元数据
        if(StringUtils.isNotEmpty(metadata)){
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        FileSystem save = repostiory.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,save);
    }

    //把文件上传到图片服务器中返回上传完成之后生成的id
    public String uploadRowbackId(MultipartFile multipartFile) {
        try {
            initstory();
            //创建客户端
            TrackerClient trackerClient = new TrackerClient();
            //创建TrackerServer 服务端
            TrackerServer connection = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(connection);
            StorageClient1 storageClient1 = new StorageClient1(connection, storeStorage);
            //获取文件的信息
            byte[] bytes = multipartFile.getBytes();
            //获取后缀
            String originalFilename = multipartFile.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //返回文件id
            String s = storageClient1.upload_file1(bytes, substring, null);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //初始化客户端 连接参数
    private void initstory() {
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
