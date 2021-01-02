package com.lsu.file.controller.web;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.lsu.file.controller.admin.FileController;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.util.VodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsuo
 */
@RestController("webVodController")
@RequestMapping("/web")
public class VodController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    public static final String BUSINESS_NAME = "VOD视频服务";

    @RequestMapping(value = "/get-auth/{vod}", method = RequestMethod.GET)
    public ResponseDto getAuth(@PathVariable String vod) throws ClientException {
        LOG.info("获取播放授权开始");
        ResponseDto<String> responseDto = new ResponseDto<>();
        DefaultAcsClient client = VodUtil.initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response;
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
