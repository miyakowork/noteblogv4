package me.wuwenbin.noteblogv4.web.management.dictionary;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
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
@RequestMapping("/management/dictionary/cate")
public class AdminCateController extends BaseController {

    private final CateRepository cateRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public AdminCateController(CateRepository cateRepository, ArticleRepository articleRepository) {
        this.cateRepository = cateRepository;
        this.articleRepository = articleRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:cate:page", remark = "分类管理页面", group = ROUTER, type = NAV_LINK)
    public String cate() {
        return "management/dictionary/cate";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:cate:list", remark = "分类管理分页数据", group = AJAX)
    public LayuiTable<NBCate> cateList(Pagination<NBCate> catePage) {
        //jpa分页是从0开始
        Pageable pageable = PageRequest.of(catePage.getPage() - 1, catePage.getLimit());
        Page<NBCate> page = cateRepository.findAll(pageable);
        return layuiTable(page, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:cate:create", remark = "添加分类操作", group = AJAX)
    @ResponseBody
    public NBR cateCreate(NBCate cate) {
        if (cate != null && StrUtil.isNotEmpty(cate.getName())) {
            return ajaxDone(
                    () -> cateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cateRepository.save(cate) != null, () -> "添加分类信息"),
                    () -> "已存在此分类信息"
            );
        }
        return NBR.error("添加分类值有误！");
    }

    @RequestMapping("/delete")
    @NBAuth(value = "management:cate:delete", remark = "删除分类操作", group = AJAX)
    @ResponseBody
    public NBR delete(Long cateId) {
        return ajaxDone(
                () -> articleRepository.countByCateId(cateId) == 0,
                () -> ajaxDone(cateId, cateRepository::deleteById, () -> "删除分类"),
                () -> "此分类下还有文章！"
        );
    }

    @RequestMapping("/update")
    @NBAuth(value = "management:cate:update", remark = "修改分类操作", group = AJAX)
    @ResponseBody
    public NBR update(@Valid NBCate cate, BindingResult result) {
        if (result.getErrorCount() == 0) {
            return ajaxDone(
                    () -> cateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cateRepository.save(cate) != null, () -> "修改分类信息"),
                    () -> "已存在此分类信息（分类名/中文名重复）"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }
}
