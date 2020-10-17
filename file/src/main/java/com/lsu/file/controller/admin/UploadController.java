package com.lsu.file.controller.admin;

import com.lsu.server.dto.FileDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.enums.FileUseEnum;
import com.lsu.server.service.FileService;
import com.lsu.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseDto<FileDto> upload(@RequestParam MultipartFile file, String use) {
        LOG.info("文件上传开始");
        LOG.info("文件名称: {}", file.getOriginalFilename());
        LOG.info("文件大小: {}", file.getSize());

        // 保存文件到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String key = UuidUtil.getShortUuid();
        String filename = file.getOriginalFilename();
        String suffix = filename != null ? filename.substring(filename.lastIndexOf(".") + 1).toLowerCase() : null;

        // 如果文件夹不存在就创建
        String dir = useEnum != null ? useEnum.name().toLowerCase() : null;
        File fullDir = new File(path + dir);
        if (!fullDir.exists()) {
            boolean b = fullDir.mkdir();
        }
        String newPath = dir + File.separator + key + "." + suffix;
        String fullPath = path + newPath;
        File dest = new File(fullPath);
        try {
            file.transferTo(dest);
            LOG.info("文件保存路径: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("保存文件记录开始");
        FileDto fileDto = new FileDto();
        fileDto.setPath(newPath);
        fileDto.setName(filename);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse(use);
        fileService.save(fileDto);

        ResponseDto<FileDto> responseDto = new ResponseDto<>();
        fileDto.setPath(domain + newPath);
        responseDto.setContent(fileDto);
        return responseDto;
    }
}
