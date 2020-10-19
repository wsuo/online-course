package com.lsu.server.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.apache.commons.codec.binary.Base64;

import java.io.File;

/**
 * @author wsuo
 */
public class VodUtil {

    /**
     * 使用账号 AccessKey 初始化 VOD 客户端
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     * @throws ClientException
     */
    private static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        // 点播服务接入区域，国内请填cn-shanghai，其他区域请参考文档[点播中心](~~98194~~)
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    /**
     * 获取视频上传地址和凭证
     *
     * @param vodClient VOD 客户端
     * @return 返回ACS响应对象
     * @throws ClientException 客户端异常信息
     */
    private static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient vodClient) throws ClientException {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setFileName("vod_test.mp4");
        request.setTitle("岁月神偷");
        // 设置分类 ID
        request.setCateId(1000204510L);
        // 设置转码模板 ID
        request.setTemplateGroupId("c9e358506fd4238e040a713dbab14b4c");
        // 设置请求超时时间
        request.setSysReadTimeout(1000);
        request.setSysConnectTimeout(1000);
        return vodClient.getAcsResponse(request);
    }

    /**
     * 使用上传凭证和地址初始化OSS客户端（注意需要先Base64解码并Json Decode再传入）
     *
     * @param uploadAuth
     * @param uploadAddress
     * @return
     */
    private static OSSClient initOssClient(JSONObject uploadAuth, JSONObject uploadAddress) {
        String endpoint = uploadAddress.getString("Endpoint");
        String accessKeyId = uploadAuth.getString("AccessKeyId");
        String accessKeySecret = uploadAuth.getString("AccessKeySecret");
        String securityToken = uploadAuth.getString("SecurityToken");
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, securityToken);
    }

    /**
     * 上传本地文件
     *
     * @param ossClient
     * @param uploadAddress
     * @param localFile
     */
    private static void uploadLocalFile(OSSClient ossClient, JSONObject uploadAddress, String localFile) {
        String bucketName = uploadAddress.getString("Bucket");
        String objectName = uploadAddress.getString("FileName");
        File file = new File(localFile);
        ossClient.putObject(bucketName, objectName, file);
    }

    /**
     * 刷新上传凭证
     *
     * @param vodClient
     * @return
     * @throws ClientException
     */
    public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient vodClient) throws ClientException {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setAcceptFormat(FormatType.JSON);
        request.setVideoId("VideoId");
        //设置请求超时时间
        request.setSysReadTimeout(1000);
        request.setSysConnectTimeout(1000);
        return vodClient.getAcsResponse(request);
    }

    public static void main(String[] argv) {
        //您的AccessKeyId
        String accessKeyId = "LTAI4G9mJMU4jkeMGVPTbCsA";
        //您的AccessKeySecret
        String accessKeySecret = "2xK9ogegMnQGqvcCObsJwpmT67JzOQ";
        //需要上传到VOD的本地视频文件的完整路径，需要包含文件扩展名
        String localFile = "D:\\fileUpload\\suiyue.mp4";
        try {
            // 初始化VOD客户端并获取上传地址和凭证
            DefaultAcsClient vodClient = initVodClient(accessKeyId, accessKeySecret);
            CreateUploadVideoResponse createUploadVideoResponse = createUploadVideo(vodClient);
            // 执行成功会返回VideoId、UploadAddress和UploadAuth
            String videoId = createUploadVideoResponse.getVideoId();
            JSONObject uploadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()), JSONObject.class);
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()), JSONObject.class);
            // 使用UploadAuth和UploadAddress初始化OSS客户端
            OSSClient ossClient = initOssClient(uploadAuth, uploadAddress);
            // 上传文件，注意是同步上传会阻塞等待，耗时与文件大小和网络上行带宽有关
            uploadLocalFile(ossClient, uploadAddress, localFile);
            System.out.println("上传视频成功, VideoId : " + videoId);
        } catch (Exception e) {
            System.out.println("上传视频失败, ErrorMessage : " + e.getLocalizedMessage());
        }
    }
}
