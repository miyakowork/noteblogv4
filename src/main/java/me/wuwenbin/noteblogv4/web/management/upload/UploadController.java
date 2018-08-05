package me.wuwenbin.noteblogv4.web.management.upload;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.NBUpload;
import me.wuwenbin.noteblogv4.service.upload.UploadService;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;

/**
 * created by Wuwenbin on 2018/8/3 at 22:06
 */
@Slf4j
@RestController
@RequestMapping("/management")
public class UploadController {

    private final String EDITOR_MD_FILE_NAME = "editormd-image-file";
    private final String LAY_UPLOADER_FILE_NAME = "file";

    private UploadService<Void> uploadService = NBUtils.getUploadServiceByConfig();

    private static void accept(Void v) {
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @NBAuth(value = "management:common:upload", remark = "通用上传接口", group = AJAX)
    public Object upload(@RequestParam(value = LAY_UPLOADER_FILE_NAME) MultipartFile file, @RequestParam("reqType") String reqType) {
        return uploadService.upload(file, reqType, UploadController::accept, null);
    }


    @RequestMapping(value = "/upload/editorMD", method = RequestMethod.POST)
    @NBAuth(value = "management:editormd:upload", remark = "editormd编辑器上传接口", group = AJAX)
    public Object uploadEditorMD(@RequestParam(value = EDITOR_MD_FILE_NAME) MultipartFile file) {
        try {
            NBUpload upload = uploadService.uploadIt(file, v -> {
            }, null);
            String visitUrl = upload.getVirtualPath();
            return MapUtil.of(new Object[][]{
                    {"success", 1}, {"message", "上传成功！"}, {"url", visitUrl}
            });
        } catch (IOException e) {
            log.error("editormd编辑器上传图片失败，错误信息：{}", e.getMessage());
            return MapUtil.of("success", 0);
        }
    }
}
