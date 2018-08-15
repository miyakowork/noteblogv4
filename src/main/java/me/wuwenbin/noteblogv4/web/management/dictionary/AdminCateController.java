package me.wuwenbin.noteblogv4.web.management.dictionary;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.dictionary.CateService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private final CateService cateService;
    private final CateRepository cateRepository;

    @Autowired
    public AdminCateController(CateService cateService, CateRepository cateRepository) {
        this.cateService = cateService;
        this.cateRepository = cateRepository;
    }


    @RequestMapping
    @NBAuth(value = "management:cate:page", remark = "分类管理页面", group = ROUTER, type = NAV_LINK)
    public String cate() {
        return "management/dictionary/cate";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:cate:list", remark = "分类管理分页数据", group = AJAX)
    public LayuiTable<NBCate> cateList(Pagination<NBCate> catePage, NBCate cateQueryBo) {
        Page<NBCate> catePageInfo = cateService.findPageInfo(catePage, cateQueryBo);
        return layuiTable(catePageInfo);
    }

    @RequestMapping("/add")
    @NBAuth(value = "management:cate:create", remark = "添加分类操作", group = AJAX)
    @ResponseBody
    public NBR cateCreate(NBCate cate) {
        if (cate != null && StrUtil.isNotEmpty(cate.getName())) {
            return ajaxDone(
                    () -> cateService.findIfExist(cate),
                    () -> ajaxDone(() -> cateRepository.save(cate) != null, () -> "分类信息"),
                    () -> "已存在此分类"
            );
        }
        return NBR.error("添加分类值有误！");
    }
}
