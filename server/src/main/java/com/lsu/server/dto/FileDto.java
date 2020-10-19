package com.lsu.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author wsuo
 */
@Data
@ToString
public class FileDto {

    /**
     * id
     */
    private String id;

    /**
     * 相对路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 大小|字节B
     */
    private Integer size;

    /**
     * 用途|枚举[FileUseEnum]：COURSE("C", "讲师"), TEACHER("T", "课程")
     */
    private String use;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedAt;

    /**
     * base64
     */
    private String shard;

    /**
     * 分片索引
     */
    private Integer shardIndex;

    /**
     * 分片大小
     */
    private Integer shardSize;

    /**
     * 总分片数
     */
    private Integer shardTotal;

    /**
     * 唯一标识: 组合分片用
     */
    private String key;

    /**
     * 阿里云vod
     */
    private String vod;
}