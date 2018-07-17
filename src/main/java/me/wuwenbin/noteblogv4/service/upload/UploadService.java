package me.wuwenbin.noteblogv4.service.upload;

import me.wuwenbin.noteblogv4.model.constant.LayUploader;
import me.wuwenbin.noteblogv4.model.constant.Upload.Method;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Function;

/**
 * 上传方法
 * created by Wuwenbin on 2018/7/17 at 10:29
 *
 * @author wuwenbin
 */
public interface UploadService<T, R> {

    /**
     * 默认是本地服务器上传
     * 如果需要其他方式，请在实现类中重写该方法
     *
     * @return 返回默认的上传方式
     */
    default Method getUploadMethod() {
        return Method.LOCAL;
    }

    /**
     * 文件上传接口方法
     *
     * @param fileObj 文件对象
     * @param extra   上传之外的额外操作
     * @return layui upload 的上传json
     */
    LayUploader upload(MultipartFile fileObj, Function<T, R> extra);

}
