package com.lsu.file.controller.admin;

import com.lsu.server.dto.ResponseDto;
import com.lsu.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping("/upload")
    public ResponseDto<String> upload(@RequestParam MultipartFile file) {
        LOG.info("文件上传开始: {}", file);
        LOG.info("文件名称: {}", file.getOriginalFilename());
        LOG.info("文件大小: {}", file.getSize());

        // 保存文件到本地
        String filename = file.getOriginalFilename();
        String key = UuidUtil.getShortUuid();
        String fullPath = path + "teacher/" + key + "-" + filename;
        File dest = new File(fullPath);
        try {
            file.transferTo(dest);
            LOG.info("文件保存路径: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setContent(domain + "f/teacher/" + key + "-" + filename);
        return responseDto;
    }
}
