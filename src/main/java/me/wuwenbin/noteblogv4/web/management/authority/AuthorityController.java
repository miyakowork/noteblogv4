package me.wuwenbin.noteblogv4.web.management.authority;

import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by Wuwenbin on 2018/7/23 at 10:50
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class AuthorityController {

    private final RoleRepository roleRepository;

    @Autowired
    public AuthorityController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping("/role")
    public String roleIndex(Model model) {
        model.addAttribute("roleList",roleRepository.findAll());
        return "management/authority/role";
    }

    @RequestMapping("roleJson")
    @ResponseBody
    public Object roleJson(){
        return roleRepository.findAll();
    }

}
