package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.tencent.wxcloudrun.dto.FileRecord;
import com.tencent.wxcloudrun.service.FileRecordService;
import com.tencent.wxcloudrun.dao.FileRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
* @author shuo
* @description 针对表【file_record】的数据库操作Service实现
* @createDate 2025-11-03 09:51:36
*/
@Service
@RequiredArgsConstructor
public class FileRecordServiceImpl extends ServiceImpl<FileRecordMapper, FileRecord>
    implements FileRecordService{
    private final COSClient cosClient;

    @Value("${tencent.cos.bucket}")
    private String bucketName;

    @Value("${tencent.cos.region}")
    private String region;

    /**
     * 上传文件到COS
     */
    @Override
    public String uploadToCos(MultipartFile file) throws IOException {
        // 临时保存文件到本地
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffix;

        File localFile = File.createTempFile("upload-", suffix);
        file.transferTo(localFile);

        // 上传路径：upload/yyyy/MM/dd/filename
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String key = "upload/" + datePath + "/" + newFileName;

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            cosClient.putObject(putObjectRequest);
        } finally {
            localFile.delete(); // 删除本地临时文件
        }

        String url = String.format("https://%s.cos.%s.myqcloud.com/%s", bucketName, region, key);
        return url;
    }

    /**
     * 删除COS文件 + 数据库记录
     */
    @Override
    public boolean removeById(Long id) {
        FileRecord record = this.getById(id);
        if (record == null) return false;

        String fileUrl = record.getFileUrl();
        String key = extractObjectKey(fileUrl);

        try {
            cosClient.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (CosClientException e) {
            e.printStackTrace();
        }

        return super.removeById(id);
    }

    private String extractObjectKey(String fileUrl) {
        if (fileUrl == null) return "";
        int index = fileUrl.indexOf(".com/");
        if (index != -1) {
            return fileUrl.substring(index + 5);
        }
        return fileUrl;
    }
}




