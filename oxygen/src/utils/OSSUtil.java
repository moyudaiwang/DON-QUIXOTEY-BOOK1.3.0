package com.example.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 阿里云OSS工具类
 * 封装了文件上传、下载、删除等常用操作
 */
@Component
public class OSSUtil {
    private static final Logger logger = LoggerFactory.getLogger(OSSUtil.class);
    
    private static OSSUtil instance;
    private final OSS ossClient;
    
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    
    private OSSUtil() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
    
    public static synchronized OSSUtil getInstance() {
        if (instance == null) {
            instance = new OSSUtil();
        }
        return instance;
    }
    
    /**
     * 生成文件路径
     * @param fileName 原始文件名
     * @param dir 存储目录
     * @return 完整的文件路径
     */
    private String generateFilePath(String fileName, String dir) {
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        return dir + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;
    }
    
    /**
     * 上传单个文件
     * @param file 文件对象
     * @param dir 存储目录
     * @return 文件访问URL
     */
    public String uploadFile(File file, String dir) {
        String fileName = file.getName();
        String filePath = generateFilePath(fileName, dir);
        
        try {
            PutObjectResult result = ossClient.putObject(bucketName, filePath, file);
            if (result != null) {
                return getFileUrl(filePath);
            }
        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
        return null;
    }
    
    /**
     * 批量上传文件
     * @param files 文件列表
     * @param dir 存储目录
     * @return 文件访问URL列表
     */
    public List<String> uploadFiles(List<File> files, String dir) {
        List<String> urls = new ArrayList<>();
        for (File file : files) {
            String url = uploadFile(file, dir);
            if (url != null) {
                urls.add(url);
            }
        }
        return urls;
    }
    
    /**
     * 下载文件
     * @param objectName 文件路径
     * @param localFile 本地存储路径
     */
    public void downloadFile(String objectName, String localFile) {
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            try (InputStream is = ossObject.getObjectContent();
                 FileOutputStream fos = new FileOutputStream(localFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            logger.error("文件下载失败: {}", e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    /**
     * 删除单个文件
     * @param objectName 文件路径
     */
    public void deleteFile(String objectName) {
        try {
            ossClient.deleteObject(bucketName, objectName);
        } catch (Exception e) {
            logger.error("文件删除失败: {}", e.getMessage());
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    /**
     * 批量删除文件
     * @param objectNames 文件路径列表
     */
    public void deleteFiles(List<String> objectNames) {
        try {
            ossClient.deleteObjects(bucketName, objectNames);
        } catch (Exception e) {
            logger.error("批量删除文件失败: {}", e.getMessage());
            throw new RuntimeException("批量删除文件失败", e);
        }
    }
    
    /**
     * 获取文件访问URL
     * @param objectName 文件路径
     * @return 文件访问URL
     */
    public String getFileUrl(String objectName) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            return url.toString();
        } catch (Exception e) {
            logger.error("获取文件URL失败: {}", e.getMessage());
            throw new RuntimeException("获取文件URL失败", e);
        }
    }
    
    /**
     * 判断文件是否存在
     * @param objectName 文件路径
     * @return 是否存在
     */
    public boolean doesObjectExist(String objectName) {
        try {
            return ossClient.doesObjectExist(bucketName, objectName);
        } catch (Exception e) {
            logger.error("检查文件是否存在失败: {}", e.getMessage());
            throw new RuntimeException("检查文件是否存在失败", e);
        }
    }
    
    /**
     * 关闭OSS客户端
     */
    public void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}