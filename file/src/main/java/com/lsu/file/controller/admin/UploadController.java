package com.lsu.file.controller.admin;

import com.lsu.server.dto.FileDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.enums.FileUseEnum;
import com.lsu.server.service.FileService;
import com.lsu.server.util.Base64ToMultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * 上传文件控制器
 *
 * @Author wang suo
 * @Date 2020/10/17 0017 13:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin")
public class UploadController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);
    public static final String BUSINESS_NAME = "文件上传";

    @Value("${file.domain}")
    private String domain;

    @Value("${file.path}")
    private String path;

    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
    public ResponseDto<FileDto> upload(@RequestBody FileDto fileDto) {
        LOG.info("文件上传开始");

        String use = fileDto.getUse();
        String shardBase64 = fileDto.getShard();
        String suffix = fileDto.getSuffix();
        String key = fileDto.getKey();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        LOG.info("文件名称: {}", shard != null ? shard.getOriginalFilename() : null);
        LOG.info("文件大小: {}", shard != null ? shard.getSize() : 0);

        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);

        // 如果文件夹不存在就创建
        String dir = useEnum != null ? useEnum.name().toLowerCase() : null;
        File fullDir = new File(path + dir);
        if (!fullDir.exists()) {
            boolean b = fullDir.mkdir();
        }
        StringBuffer sb = new StringBuffer(dir).append(File.separator).append(key).append(".").append(suffix).append(".").append(fileDto.getShardIndex());
        String newPath = sb.toString();
        String fullPath = path + newPath;
        File dest = new File(fullPath);
        try {
            if (shard != null) {
                shard.transferTo(dest);
            }
            LOG.info("文件保存路径: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("保存文件记录开始");
        fileDto.setPath(newPath);
        fileService.save(fileDto);

        ResponseDto<FileDto> responseDto = new ResponseDto<>();
        fileDto.setPath(domain + newPath);
        responseDto.setContent(fileDto);
        return responseDto;
    }
}
