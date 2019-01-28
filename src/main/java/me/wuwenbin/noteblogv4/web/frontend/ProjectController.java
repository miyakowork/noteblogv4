package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.repository.ProjectRepository;
import me.wuwenbin.noteblogv4.model.entity.NBProject;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/12/15 at 11:30 AM
 *
 * @author wuwenbin
 */
@Controller("frontProjectController")
@RequestMapping("/project")
public class ProjectController extends BaseController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @RequestMapping
    public String project(Model model, Pagination<NBProject> pagination) {
        Pageable pageable = getPageable(pagination);
        Page<NBProject> projectPage = projectRepository.findAll(pageable);
        model.addAttribute("projects", projectPage);
        return "frontend/project";
    }
}
