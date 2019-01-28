package me.wuwenbin.noteblogv4.service.upload;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.LayUploader;
import me.wuwenbin.noteblogv4.model.constant.NkUploader;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.model.entity.NBUpload;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * 七牛文件上传Service
 * created by Wuwenbin on 2018/7/17 at 14:46
 *
 * @author wuwenbin
 */
@Slf4j
@Service("qiniuUpload")
@Transactional(rollbackOn = Exception.class)
public class QiniuUploadServiceImpl implements UploadService<Object> {

    private final ParamRepository paramRepository;

    @Autowired
    public QiniuUploadServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public Upload.Method getUploadMethod() {
        return Upload.Method.QINIU;
    }

    @Override
    public <S> Object upload(MultipartFile fileObj, String reqType, Consumer<S> extra, S s) {
        log.info("上传[" + fileObj.getContentType() + "]类型文件");
        Response res = doUpload(fileObj, getUpToken());
        String message;
        try {
            if (res != null && res.isOK()) {
                JSONObject respObj = JSONUtil.parseObj(res.bodyString());
                String generateFileName = respObj.getStr("key");
                String qiniuDomain = paramRepository.findByName(NoteBlogV4.Param.QINIU_DOMAIN).getValue();
                log.info("上传至七牛云服务器成功！，文件：[{}]", generateFileName);
                extra.accept(s);
                String src = qiniuDomain + "/" + generateFileName;
                if (LAYUI_UPLOADER.equalsIgnoreCase(reqType)) {
                    return new LayUploader().ok("上传至七牛云服务器成功！", src);
                } else if (NKEDITOR_UPLOADER.equalsIgnoreCase(reqType)) {
                    return new NkUploader().ok(src);
                } else {
                    return NBR.ok("上传成功！", src);
                }
            } else {
                message = res != null ? res.error : "上传至七牛云服务失败，返回信息为空！";
            }
        } catch (QiniuException e) {
            message = StrUtil.format("上传至七牛云服务器失败，错误信息：{}", e.getMessage());
            e.printStackTrace();
        }
        log.error("上传至七牛服务器失败，错误信息：[{}]", message);
        if (LAYUI_UPLOADER.equalsIgnoreCase(reqType)) {
            return new LayUploader().err(message);
        } else if (NKEDITOR_UPLOADER.equalsIgnoreCase(reqType)) {
            return new NkUploader().err("上传至七牛服务器失败，错误信息：" + message);
        } else {
            return NBR.error("上传至七牛服务器失败，错误信息：[{}]" + message);
        }
    }

    @Override
    public <S> NBUpload uploadIt(MultipartFile fileObj, Consumer<S> extra, S t) {
        log.info("上传[" + fileObj.getContentType() + "]类型文件");
        Response res = doUpload(fileObj, getUpToken());
        try {
            if (res != null && res.isOK()) {
                JSONObject respObj = JSONUtil.parseObj(res.bodyString());
                String generateFileName = respObj.getStr("key");
                String qiniuDomain = paramRepository.findByName(NoteBlogV4.Param.QINIU_DOMAIN).getValue();
                extra.accept(t);
                String src = qiniuDomain + "/" + generateFileName;
                log.info("上传至七牛云服务器成功！，文件：[{}]", generateFileName);
                return NBUpload.builder()
                        .diskPath("")
                        .virtualPath(src)
                        .upload(LocalDateTime.now())
                        .type(fileObj.getContentType())
                        .build();
            } else {
                throw new RuntimeException("上传文件至七牛云失败" + (res != null ? "，" + res.error : ""));
            }
        } catch (QiniuException e) {
            throw new RuntimeException("上传文件至七牛云失败", e);
        }

    }

    /**
     * 获取uptoken
     *
     * @return
     */
    private String getUpToken() {
        String accessKey = paramRepository.findByName("qiniu_accessKey").getValue();
        String secretKey = paramRepository.findByName("qiniu_secretKey").getValue();
        String bucketName = paramRepository.findByName("qiniu_bucket").getValue();
        //密钥配置
        Auth auth = Auth.create(accessKey, secretKey);
        //简单上传，使用默认策略，只需要设置上传的空间名就可以了
        return auth.uploadToken(bucketName);
    }

    /**
     * 七牛上传
     *
     * @param file    文件
     * @param uptoken 上传密钥
     * @return Response
     */
    private Response doUpload(MultipartFile file, String uptoken) {
        try {
            String resId = IdUtil.randomUUID();
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.autoZone());
            String extend = fileName.substring(fileName.lastIndexOf("."));
            //调用put方法上传
            Response res = new UploadManager(cfg).put(file.getBytes(), resId.concat(extend), uptoken);
            log.info("[七牛上传文件] - [{}] - 返回信息：", res.isOK(), res.bodyString());
            return res;
        } catch (QiniuException e) {
            Response re = e.response;
            log.error("[七牛上传文件] - [{}] - 异常信息：", re.isOK(), re.toString());
            try {
                log.error(" 响应异常文本信息：{}", re.bodyString());
            } catch (QiniuException ignored) {
            }
            return re;
        } catch (Exception ex) {
            log.error(" 文件IO读取异常，异常信息：{}", ex.getMessage());
            return null;
        }
    }
}
