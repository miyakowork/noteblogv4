package me.wuwenbin.noteblogv4.web.management.message;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.NBTag;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/15 at 16:28
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/comment")
public class AdminCommentController extends BaseController {


    @RequestMapping
    @NBAuth(value = "management:comment:page", remark = "评论管理页面", group = ROUTER, type = NAV_LINK)
    public String tagIndex() {
        return "management/comment";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:comment:list", remark = "评论管理页面分页数据接口", group = AJAX)
    public LayuiTable<NBTag> cateList(Pagination<NBTag> tagPagination, String tagName) {
        Sort sort = getJpaSort(tagPagination);
        Pageable pageable = PageRequest.of(tagPagination.getPage() - 1, tagPagination.getLimit(), sort);
        if (StringUtils.isEmpty(tagName)) {
            Page<NBTag> tagPage = tagRepository.findAll(pageable);
            return layuiTable(tagPage, pageable);
        } else {
            Example<NBTag> tagExample = Example.of(
                    NBTag.builder().name(tagName).build(),
                    ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains).withIgnoreCase()
            );
            Page<NBTag> tagPage = tagRepository.findAll(tagExample, pageable);
            return layuiTable(tagPage, pageable);
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    @NBAuth(value = "management:tag:update", remark = "修改标签数据操作接口", group = AJAX)
    public NBR update(@Valid NBTag tag, BindingResult result) {
        if (result.getErrorCount() == 0) {
            return ajaxDone(() -> tagRepository.save(tag) != null, () -> "修改标签");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    @NBAuth(value = "management:tag:delete", remark = "删除标签数据操作接口", group = AJAX)
    public NBR delete(@RequestParam("id") Long cateId) {
        return ajaxDone(
                () -> tagReferRepository.countByTagId(cateId) == 0,
                () -> ajaxDone(cateId, tagRepository::deleteById, () -> "删除标签"),
                () -> "请删除相关使用此标签的内容！"
        );
    }
}
