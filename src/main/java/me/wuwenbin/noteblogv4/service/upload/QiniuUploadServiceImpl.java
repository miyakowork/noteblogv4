package me.wuwenbin.noteblogv4.service.upload;

import me.wuwenbin.noteblogv4.model.constant.LayUploader;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Function;

/**
 * 七牛文件上传Service
 * created by Wuwenbin on 2018/7/17 at 14:46
 *
 * @author wuwenbin
 */
public class QiniuUploadServiceImpl<T,R> implements UploadService<T,R> {

    @Override
    public Upload.Method getUploadMethod() {
        return Upload.Method.QINIU;
    }

    @Override
    public LayUploader upload(MultipartFile fileObj, Function<T, R> extra) {
        return null;
    }
}
