package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.repository.CloudFileCateRepository;
import me.wuwenbin.noteblogv4.dao.repository.CloudFileRepository;
import me.wuwenbin.noteblogv4.model.entity.NBCloudFile;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by Wuwenbin on 2019/2/14 at 16:02
 *
 * @author wuwenbin
 */
@Controller("frontCloudFileController")
@RequestMapping("/file")
public class CloudFileController extends BaseController {

    private final CloudFileRepository cloudFileRepository;
    private final CloudFileCateRepository cloudFileCateRepository;

    @Autowired
    public CloudFileController(CloudFileRepository cloudFileRepository, CloudFileCateRepository cloudFileCateRepository) {
        this.cloudFileRepository = cloudFileRepository;
        this.cloudFileCateRepository = cloudFileCateRepository;
    }

    @RequestMapping
    public String file(Model model, Long cateId) {
        model.addAttribute("cates", cloudFileCateRepository.findAll());
        model.addAttribute("currentId", cateId);
        return "frontend/cloud_file";
    }

    @RequestMapping("/list")
    @ResponseBody
    public LayuiTable<NBCloudFile> cateList(Pagination<NBCloudFile> cloudFilePagination, Long cateId, String fileName) {
        Pageable pageable = getPageable(cloudFilePagination);
        if (StringUtils.isEmpty(cateId) && StringUtils.isEmpty(fileName)) {
            Page<NBCloudFile> page = cloudFileRepository.findAll(pageable);
            return layuiTable(page, pageable);
        } else if (StringUtils.isEmpty(cateId)) {
            NBCloudFile file = NBCloudFile.builder().name(fileName).build();
            Example<NBCloudFile> example = Example.of(file,
                    ExampleMatcher.matching().withMatcher("name",
                            ExampleMatcher.GenericPropertyMatcher::contains).withIgnoreCase());
            Page<NBCloudFile> page = cloudFileRepository.findAll(example, pageable);
            return layuiTable(page, pageable);
        } else if (StringUtils.isEmpty(fileName)) {
            NBCloudFile file = NBCloudFile.builder().cateId(cateId).build();
            Example<NBCloudFile> example = Example.of(file);
            Page<NBCloudFile> page = cloudFileRepository.findAll(example, pageable);
            return layuiTable(page, pageable);
        } else {
            NBCloudFile file = NBCloudFile.builder().name(fileName).cateId(cateId).build();
            Example<NBCloudFile> example = Example.of(file,
                    ExampleMatcher.matching().withMatcher("name",
                            ExampleMatcher.GenericPropertyMatchers.contains()).withIgnoreCase());
            Page<NBCloudFile> page = cloudFileRepository.findAll(example, pageable);
            return layuiTable(page, pageable);
        }
    }
}
