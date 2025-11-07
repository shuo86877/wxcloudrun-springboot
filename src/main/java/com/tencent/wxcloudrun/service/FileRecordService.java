package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.FileRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author shuo
* @description 针对表【file_record】的数据库操作Service
* @createDate 2025-11-03 09:51:36
*/
public interface FileRecordService extends IService<FileRecord> {

    String uploadToCos(MultipartFile file) throws IOException;

    boolean removeById(Long id);
}
