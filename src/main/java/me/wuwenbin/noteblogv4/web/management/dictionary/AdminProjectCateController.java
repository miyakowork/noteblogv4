package me.wuwenbin.noteblogv4.web.management.dictionary;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ProjectCateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ProjectRepository;
import me.wuwenbin.noteblogv4.model.entity.NBProjectCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/15 at 16:29
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/dictionary/projectCate")
public class AdminProjectCateController extends BaseController {

    private final ProjectCateRepository cateRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public AdminProjectCateController(ProjectCateRepository cateRepository, ProjectRepository projectRepository) {
        this.cateRepository = cateRepository;
        this.projectRepository = projectRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:projectCate:page", remark = "项目分类管理页面", group = ROUTER, type = NAV_LINK)
    public String cate() {
        return "management/dictionary/projectCate";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:projectCate:list", remark = "项目分类管理分页数据", group = AJAX)
    public LayuiTable<NBProjectCate> cateList(Pagination<NBProjectCate> catePage) {
        //jpa分页是从0开始
        Pageable pageable = PageRequest.of(catePage.getPage() - 1, catePage.getLimit());
        Page<NBProjectCate> page = cateRepository.findAll(pageable);
        return layuiTable(page, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:projectCate:create", remark = "添加项目分类操作", group = AJAX)
    @ResponseBody
    public NBR cateCreate(NBProjectCate cate) {
        if (cate != null && StrUtil.isNotEmpty(cate.getName())) {
            return ajaxDone(
                    () -> cateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cateRepository.save(cate) != null, () -> "添加项目分类信息"),
                    () -> "已存在此项目分类信息"
            );
        }
        return NBR.error("添加分类值有误！");
    }

    @RequestMapping("/delete")
    @NBAuth(value = "management:projectCate:delete", remark = "删除项目分类操作", group = AJAX)
    @ResponseBody
    public NBR delete(Long cateId) {
        return ajaxDone(
                () -> projectRepository.countByCateId(cateId) == 0,
                () -> ajaxDone(cateId, cateRepository::deleteById, () -> "删除项目分类"),
                () -> "此项目分类下还有项目！"
        );
    }

    @RequestMapping("/update")
    @NBAuth(value = "management:projectCate:update", remark = "修改项目分类操作", group = AJAX)
    @ResponseBody
    public NBR update(@Valid NBProjectCate cate, BindingResult result) {
        if (result.getErrorCount() == 0) {
            return ajaxDone(
                    () -> cateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cateRepository.save(cate) != null, () -> "修改项目分类信息"),
                    () -> "已存在此项目分类信息（项目分类名/中文名重复）"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }
}
