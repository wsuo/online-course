package com.lsu.file.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.lsu.server.dto.FileDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.enums.FileUseEnum;
import com.lsu.server.service.FileService;
import com.lsu.server.util.Base64ToMultipartFile;
import com.lsu.server.util.VodUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author wsuo
 */
@RestController
@RequestMapping("/admin")
public class VodController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    public static final String BUSINESS_NAME = "VOD视频上传";

    @Resource
    private FileService fileService;

    @PostMapping("/vod")
    public ResponseDto<FileDto> fileUpload(@RequestBody FileDto fileDto) throws Exception {
        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        Integer shardIndex = fileDto.getShardIndex();
        Integer shardSize = fileDto.getShardSize();
        String shardBase64 = fileDto.getShard();
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String dir = useEnum.name().toLowerCase();

        String path = dir +
                "/" +
                key +
                "." +
                suffix;

        //需要上传到VOD的本地视频文件的完整路径，需要包含文件扩展名
        String vod = "";
        String fileUrl = "";
        try {
            // 初始化VOD客户端并获取上传地址和凭证
            DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);
            CreateUploadVideoResponse createUploadVideoResponse = VodUtil.createUploadVideo(vodClient, path);
            // 执行成功会返回VideoId、UploadAddress和UploadAuth
            vod = createUploadVideoResponse.getVideoId();
            JSONObject uploadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()), JSONObject.class);
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()), JSONObject.class);
            // 使用UploadAuth和UploadAddress初始化OSS客户端
            OSSClient ossClient = VodUtil.initOssClient(uploadAuth, uploadAddress);
            // 上传文件，注意是同步上传会阻塞等待，耗时与文件大小和网络上行带宽有关
            if (shard != null) {
                VodUtil.uploadLocalFile(ossClient, uploadAddress, shard.getInputStream());
            }
            System.out.println("上传视频成功, vod : " + vod);
            GetMezzanineInfoResponse response = VodUtil.getMezzanineInfoResponse(vodClient, vod);
            System.out.println("获取视频信息 response = " + JSON.toJSONString(response));
            fileUrl = response.getMezzanine().getFileURL();
            ossClient.shutdown();
        } catch (Exception e) {
            System.out.println("上传视频失败, ErrorMessage : " + e.getLocalizedMessage());
        }

        fileDto.setPath(path);
        fileDto.setVod(vod);
        fileService.save(fileDto);
        ResponseDto<FileDto> responseDto = new ResponseDto<>();
        fileDto.setPath(fileUrl);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @RequestMapping(value = "/get-auth/{vod}", method = RequestMethod.GET)
    public ResponseDto<String> getAuth(@PathVariable String vod) {
        LOG.info("获取播放授权开始");
        ResponseDto<String> responseDto = new ResponseDto<>();
        DefaultAcsClient client = VodUtil.initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            response = VodUtil.getVideoPlayAuthResponse(client, vod);
            String playAuth = response.getPlayAuth();
            //播放凭证
            LOG.info("授权码 = {}", playAuth);
            responseDto.setContent(playAuth);
            //VideoMeta信息
            LOG.info("VideoMeta信息 = {}", response.getVideoMeta().getTitle());
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        LOG.info("获取播放授权结束");
        return responseDto;
    }
}
