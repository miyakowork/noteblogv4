package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.mapper.TagMapper;
import me.wuwenbin.noteblogv4.model.entity.NBTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * created by Wuwenbin on 2018/8/4 at 11:25
 * @author wuwenbin
 */
@Controller
@RequestMapping("/tag")
public class TagController {

    private final TagMapper tagMapper;

    @Autowired
    public TagController(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<NBTag> tagsList() {
        return tagMapper.findAll();
    }
}
