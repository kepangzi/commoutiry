package file.majiang.commounity.controller;

import file.majiang.commounity.mapper.UserMapper;
import file.majiang.commounity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;

    @GetMapping(value = "/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if(StringUtils.pathEquals(cookie.getName(),"token")){
                //获取跳转带过来的token
                String token = cookie.getValue();
                //获取数据库查询出来的token
                User user = userMapper.findByToken(token);
                if (user!=null){
                    request.getSession().setAttribute("user",user);
                    break;
                }
            }
        }
        return "index";
    }
}
