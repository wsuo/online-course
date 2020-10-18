package com.lsu.file.controller.admin;

import com.lsu.server.dto.FileDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.enums.FileUseEnum;
import com.lsu.server.service.FileService;
import com.lsu.server.util.Base64ToMultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

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
        String newPath = dir + File.separator + key + "." + suffix;
        String localPath = newPath + "." + fileDto.getShardIndex();
        String fullPath = path + localPath;
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

        if (fileDto.getShardIndex().equals(fileDto.getShardTotal())) {
            this.merge(fileDto);
        }
        return responseDto;
    }

    /**
     * 合并上传的文件分片
     */
    private void merge(FileDto fileDto) {
        LOG.info("合并分片开始");
        String relativePath = fileDto.getPath().replace(domain, "");
        // 总分片数: 即循环的次数
        Integer shardTotal = fileDto.getShardTotal();
        File newFile = new File(path + relativePath);
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            // 以追加的方式写入
            FileOutputStream outputStream = new FileOutputStream(newFile, true);
            FileInputStream inputStream = null;
            for (int i = 0; i < shardTotal; i++) {
                inputStream = new FileInputStream(new File(path + relativePath + "." + (i + 1)));
                while ((len = inputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("合并分片结束");

        // 文件被程序占用: 删除失败: 现在通知虚拟机回收垃圾即可
        System.gc();

        // 删除残留的分片
        LOG.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = path + relativePath + "." + (i + 1);
            File file = new File(filePath);
            boolean res = file.delete();
            LOG.info("删除{}，{}", filePath, res ? "成功" : "失败");
        }
        LOG.info("删除分片结束");
    }

    /**
     * 检查分片
     *
     * @param key KEY 值
     * @return 返回对象
     */
    @GetMapping("/check/{key}")
    public ResponseDto<FileDto> check(@PathVariable String key) {
        LOG.info("检查上传文件的分片: {}", key);
        ResponseDto<FileDto> responseDto = new ResponseDto<>();
        FileDto fileDto = fileService.findByKey(key);
        responseDto.setContent(fileDto);
        return responseDto;
    }
}
