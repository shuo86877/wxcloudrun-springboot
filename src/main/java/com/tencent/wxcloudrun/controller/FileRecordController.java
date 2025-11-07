package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.FileRecordMapper;
import com.tencent.wxcloudrun.dto.FileRecord;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.tencent.wxcloudrun.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileRecordController {

    @Resource
    private COSClient cosClient;

    @Autowired
    private FileRecordService fileService;

    @Resource
    private FileRecordMapper fileRecordMapper;

    @Value("${tencent.cos.bucket}")
    private String bucket;

    @Value("${tencent.cos.base-url}")
    private String baseUrl;

    /**
     * 上传文件接口
     */
    @PostMapping("/upload")
    public ApiResponse upload(@RequestParam("file") MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        String key = "uploads/" + UUID.randomUUID() + "." + ext;

        // 自动检测文件类型
        String contentType = detectContentType(ext);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(file.getSize());

        try (InputStream input = file.getInputStream()) {
            PutObjectRequest request = new PutObjectRequest(bucket, key, input, metadata);
            cosClient.putObject(request);
        }

        String fileUrl = baseUrl + "/" + key;

        // 写入数据库
        FileRecord record = new FileRecord();
        record.setId(UUID.randomUUID().toString());
        record.setFileName(original);
        record.setFileUrl(fileUrl);
        record.setFileType(contentType);
        record.setFileSize(file.getSize());
        fileRecordMapper.insert(record);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", record.getId());
        hashMap.put("url", fileUrl);
        hashMap.put("type", contentType);

        return ApiResponse.ok(hashMap);
    }

    /**
     * 删除文件接口
     */
    @DeleteMapping("/delete")
    public ApiResponse delete(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isEmpty()) {
            return ApiResponse.error("url required");
        }

        // 从 URL 提取 key
        String key = url.substring(url.indexOf(".myqcloud.com/") + 14);

        // 1. 删除 COS 文件
        cosClient.deleteObject(bucket, key);

        // 2. 删除数据库记录
        fileRecordMapper.delete(new LambdaQueryWrapper<FileRecord>()
                .eq(FileRecord::getFileUrl, url));

        return ApiResponse.ok(true);
    }


    private String detectContentType(String ext) {
        switch (ext) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "mp4":
                return "video/mp4";
            case "mov":
                return "video/quicktime";
            case "avi":
                return "video/x-msvideo";
            case "mkv":
                return "video/x-matroska";
            case "pdf":
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }
}
