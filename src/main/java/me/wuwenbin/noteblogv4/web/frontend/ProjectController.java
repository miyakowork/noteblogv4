package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/12/15 at 11:30 AM
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @RequestMapping
    public String project() {
        return "frontend/project";
    }
}
