package com.tencent.wxcloudrun.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName file_record
 */
@TableName(value ="file_record")
@Data
public class FileRecord {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 
     */
    private String fileName;

    /**
     * 
     */
    private String fileUrl;

    /**
     * 
     */
    private String fileType;

    /**
     * 
     */
    private Long fileSize;

    /**
     * 
     */
    private String uploadId;

    /**
     * 
     */
    private Date createTime;
}