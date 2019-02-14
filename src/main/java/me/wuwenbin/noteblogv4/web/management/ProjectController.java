package me.wuwenbin.noteblogv4.web.management;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ProjectCateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ProjectRepository;
import me.wuwenbin.noteblogv4.exception.NoteFetchFailedException;
import me.wuwenbin.noteblogv4.model.entity.NBProject;
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
@RequestMapping("/management/project")
@Controller
public class ProjectController extends BaseController {

    private final ProjectRepository projectRepository;
    private final ProjectCateRepository projectCateRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectCateRepository projectCateRepository) {
        this.projectRepository = projectRepository;
        this.projectCateRepository = projectCateRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @NBAuth(value = "management:project:list_page", remark = "项目管理页面", type = NAV_LINK, group = ROUTER)
    public String list() {
        return "management/project/project_list";
    }

    @RequestMapping("/add")
    @NBAuth(value = "management:project:add_page", remark = "项目发布页面", type = NAV_LINK, group = ROUTER)
    public String add(Model model) {
        model.addAttribute("cateList", projectCateRepository.findAll());
        return "management/project/project_add";
    }

    @RequestMapping("/edit")
    @NBAuth(value = "management:project:edit_page", remark = "项目管编辑页面", type = OTHER, group = ROUTER)
    public String edit(Model model, Long id) {
        Optional<NBProject> project = projectRepository.findById(id);
        model.addAttribute("cateList", projectCateRepository.findAll());
        model.addAttribute("editProject", project.orElseThrow(NoteFetchFailedException::new));
        return "management/project/project_edit";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @NBAuth(value = "management:project:list_data", remark = "项目管理页面中的数据接口", group = AJAX)
    @ResponseBody
    public LayuiTable<NBProject> projectList(Pagination<NBProject> notePagination) {
        Pageable pageable = getPageable(notePagination);
        Page<NBProject> jpaPage = projectRepository.findAll(pageable);
        return layuiTable(jpaPage, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:project:create", remark = "发布一个新的项目", group = AJAX)
    @ResponseBody
    public NBR projectCreate(@Valid NBProject nbProject, BindingResult result) {
        if (result.getErrorCount() == 0) {
            nbProject.setPost(LocalDateTime.now());
            nbProject.setDescription(NBUtils.stripSqlXSS(nbProject.getDescription()));
            return ajaxDone(
                    () -> projectRepository.save(nbProject) != null
                    , () -> "分享项目"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @RequestMapping("/update")
    @NBAuth(value = "management:project:update", remark = "修改一篇项目", group = AJAX)
    @ResponseBody
    public NBR projectUpdate(@Valid NBProject nbProject, BindingResult result) {
        if (result.getErrorCount() == 0) {
            nbProject.setModify(LocalDateTime.now());
            return ajaxDone(() -> projectRepository.updateProjectById(nbProject) == 1, () -> "更新项目信息");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    @NBAuth(value = "management:project:delete", remark = "删除项目操作", group = AJAX)
    public NBR delete(@PathVariable("id") Long id) {
        return ajaxDone(id
                , projectRepository::deleteById
                , () -> "删除项目"
        );
    }

}
