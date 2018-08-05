package me.wuwenbin.noteblogv4.service.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.model.constant.Upload.Method;
import me.wuwenbin.noteblogv4.model.entity.NBUpload;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 上传方法
 * created by Wuwenbin on 2018/7/17 at 10:29
 *
 * @author wuwenbin
 */
public interface UploadService<T> {

    /**
     * layui上传组件
     */
    String LAYUI_UPLOADER = "lay";

    /**
     * nkeditor 上传组件
     */
    String NKEDITOR_UPLOADER = "nk";

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
     * 默认上传方法
     *
     * @param fileObj
     * @param extra
     * @param t
     * @return
     * @throws IOException
     */
    default NBUpload uploadIt(MultipartFile fileObj, Consumer<T> extra, T t) throws IOException {
        String uploadPathPre = Objects.requireNonNull(fileObj.getContentType()).contains("image/") ? Upload.FileType.IMAGE : Upload.FileType.FILE;
        String fileName = fileObj.getOriginalFilename();
        //扩展名，包括点符号
        String ext = fileName.substring(Objects.requireNonNull(fileName).lastIndexOf("."));
        String newFileName = RandomUtil.randomUUID().concat(ext);
        String prefix = NBUtils.getBean(Environment.class).getProperty("noteblog.upload.path");
        String uploadFilePath = FileUtil.getAbsolutePath(prefix + uploadPathPre + "/" + newFileName);
        FileOutputStream out = new FileOutputStream(uploadFilePath);
        out.write(fileObj.getBytes());
        out.flush();
        out.close();
        String virtualPath = Upload.FileType.VISIT_PATH.concat(uploadPathPre).concat("/").concat(newFileName);
        extra.accept(t);
        return NBUpload.builder()
                .diskPath(uploadFilePath)
                .virtualPath(virtualPath)
                .upload(LocalDateTime.now())
                .type(fileObj.getContentType())
                .build();
    }

    /**
     * 文件上传接口方法
     *
     * @param fileObj 文件对象
     * @param reqType 上传组件（layuiUploader还是NKuploader）
     * @param extra   上传之外的额外操作
     * @param t       上传之外的额外操作的参数
     * @return upload 的上传json
     */
    Object upload(MultipartFile fileObj, String reqType, Consumer<T> extra, T t);

}
