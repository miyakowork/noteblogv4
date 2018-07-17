package me.wuwenbin.noteblogv4.service.upload;

import me.wuwenbin.noteblogv4.model.constant.LayUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.function.Function;

/**
 * created by Wuwenbin on 2018/7/17 at 10:33
 * 本地上传的实现类
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class LocalUploadServiceImpl<T, R> implements UploadService<T, R> {


    @Override
    public LayUploader upload(MultipartFile fileObj, Function<T, R> extra) {
        return null;
    }
}
