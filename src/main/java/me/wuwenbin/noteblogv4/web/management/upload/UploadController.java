package me.wuwenbin.noteblogv4.web.management.upload;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.NBUpload;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;

/**
 * created by Wuwenbin on 2018/8/3 at 22:06
 *
 * @author wuwenbin
 */
@Slf4j
@RestController
@RequestMapping("/management")
public class UploadController {

    private final String EDITOR_MD_FILE_NAME = "editormd-image-file";
    private final String LAY_UPLOADER_FILE_NAME = "file";


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @NBAuth(value = "management:common:upload", remark = "通用上传接口", group = AJAX)
    public Object upload(HttpServletRequest request, @RequestParam(value = LAY_UPLOADER_FILE_NAME, required = false) MultipartFile file, @RequestParam("reqType") String reqType) {
        String base64 = request.getParameter("base64");
        String GRAFFITI = "1";
        if (StrUtil.isNotEmpty(base64) && GRAFFITI.equals(base64)) {
            String base64Str = request.getParameter("img_base64_data");
            file = NBUtils.base64ToMultipartFile(base64Str);
        }
        return Objects.requireNonNull(NBUtils.getUploadServiceByConfig()).upload(file, reqType, (v) -> {
        }, null);
    }


    @RequestMapping(value = "/upload/editorMD", method = RequestMethod.POST)
    @NBAuth(value = "management:editormd:upload", remark = "editormd编辑器上传接口", group = AJAX)
    public Object uploadEditorMD(@RequestParam(value = EDITOR_MD_FILE_NAME) MultipartFile file) {
        try {
            NBUpload upload = Objects.requireNonNull(NBUtils.getUploadServiceByConfig()).uploadIt(file, (v) -> {
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
