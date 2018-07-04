package com.twimi.documentmanager.Controller;

import com.twimi.documentmanager.Dao.UserDao;
import com.twimi.documentmanager.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.POST)
    public String loginAction(ModelMap modelMap,
                              HttpSession session,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/index";
            } else {
                modelMap.addAttribute("message", "密码错误");
                return "login";
            }
        } else {
            modelMap.addAttribute("message", "用户名不存在");
            return "login";
        }
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public String registerAction(ModelMap modelMap,
                                 HttpSession session,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "role", defaultValue = "0") int role,
                                 @RequestParam("name") String name,
                                 @RequestParam("sex") String sex,
                                 @RequestParam("birthday") String birthday,
                                 @RequestParam(value = "address" ,required = false) String address,
                                 @RequestParam("contact") String contact,
                                 @RequestParam(value = "referrer",required = false) String referrer,
                                 @RequestParam("industryid") int industryid,
                                 @RequestParam("committeeid") int committeeid) {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            modelMap.addAttribute("message", "用户名已存在");
            return "register";
        } else {
            int uid = userDao.insert(new User(username, password, role, name, sex, birthday, address, contact, referrer, industryid, committeeid));
            if (uid <= 0) {
                modelMap.addAttribute("message", "数据库错误");
                return "register";
            } else {
                session.setAttribute("user", user);
                return "redirect:/index";
            }
        }
    }
}
