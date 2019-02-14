package me.wuwenbin.noteblogv4.web.management;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.CloudFileCateRepository;
import me.wuwenbin.noteblogv4.dao.repository.CloudFileRepository;
import me.wuwenbin.noteblogv4.exception.NoteFetchFailedException;
import me.wuwenbin.noteblogv4.model.entity.NBCloudFile;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.util.NBUtils;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.OTHER;

/**
 * created by Wuwenbin on 2018/8/18 at 10:14
 *
 * @author wuwenbin
 */
@RequestMapping("/management/cloudFile")
@Controller
public class CloudFileController extends BaseController {

    private final CloudFileRepository cloudFileRepository;
    private final CloudFileCateRepository cloudFileCateRepository;

    @Autowired
    public CloudFileController(CloudFileRepository cloudFileRepository, CloudFileCateRepository cloudFileCateRepository) {
        this.cloudFileRepository = cloudFileRepository;
        this.cloudFileCateRepository = cloudFileCateRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @NBAuth(value = "management:cloudFile:list_page", remark = "云文件管理页面", type = NAV_LINK, group = ROUTER)
    public String list() {
        return "management/cloud/cloud_file_list";
    }

    @RequestMapping("/add")
    @NBAuth(value = "management:cloudFile:add_page", remark = "云文件发布页面", type = NAV_LINK, group = ROUTER)
    public String add(Model model) {
        model.addAttribute("cateList", cloudFileCateRepository.findAll());
        return "management/cloud/cloud_file_add";
    }

    @RequestMapping("/edit")
    @NBAuth(value = "management:cloudFile:edit_page", remark = "云文件管编辑页面", type = OTHER, group = ROUTER)
    public String edit(Model model, Long id) {
        Optional<NBCloudFile> cloudFile = cloudFileRepository.findById(id);
        model.addAttribute("cateList", cloudFileCateRepository.findAll());
        model.addAttribute("editCloudFile", cloudFile.orElseThrow(NoteFetchFailedException::new));
        return "management/cloud/cloud_file_edit";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @NBAuth(value = "management:cloudFile:list_data", remark = "云文件管理页面中的数据接口", group = AJAX)
    @ResponseBody
    public LayuiTable<NBCloudFile> cloudFileList(Pagination<NBCloudFile> nbCloudFilePagination) {
        Pageable pageable = getPageable(nbCloudFilePagination);
        Page<NBCloudFile> jpaPage = cloudFileRepository.findAll(pageable);
        return layuiTable(jpaPage, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:cloudFile:create", remark = "发布一个新的项目", group = AJAX)
    @ResponseBody
    public NBR projectCreate(@Valid NBCloudFile nbCloudFile, BindingResult result) {
        if (result.getErrorCount() == 0) {
            nbCloudFile.setPost(LocalDateTime.now());
            nbCloudFile.setModify(LocalDateTime.now());
            nbCloudFile.setDescription(NBUtils.stripSqlXSS(nbCloudFile.getDescription()));
            return ajaxDone(
                    () -> cloudFileRepository.save(nbCloudFile) != null
                    , () -> "分享云文件"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @RequestMapping("/update")
    @NBAuth(value = "management:cloudFile:update", remark = "修改一个云文件", group = AJAX)
    @ResponseBody
    public NBR projectUpdate(@Valid NBCloudFile nbCloudFile, BindingResult result) {
        if (result.getErrorCount() == 0) {
            nbCloudFile.setModify(LocalDateTime.now());
            return ajaxDone(() -> cloudFileRepository.saveAndFlush(nbCloudFile) != null, () -> "更新云文件信息");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    @NBAuth(value = "management:cloudFile:delete", remark = "删除云文件操作", group = AJAX)
    public NBR delete(@PathVariable("id") Long id) {
        return ajaxDone(id, cloudFileRepository::deleteById, () -> "删除项目");
    }

}
