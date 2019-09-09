package file.majiang.commounity.controller;

import file.majiang.commounity.dto.AccessTokenDTO;
import file.majiang.commounity.dto.GithubUser;
import file.majiang.commounity.mapper.UserMapper;
import file.majiang.commounity.model.User;
import file.majiang.commounity.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
        @Autowired
        private GithubProvider githubProvider;

        @Value("${github.client.id}")
        private String clientId;
        @Value("${github.client.secret}")
        private String clientSecret;
        @Value("${github.redirect.uri}")
        private String redirectUri;

        @Autowired
        private UserMapper userMapper;

        @GetMapping(value = "/callback")
        public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse response){
            AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
            accessTokenDTO.setClient_id(clientId);
            accessTokenDTO.setClient_secret(clientSecret);
            accessTokenDTO.setRedirect_uri(redirectUri);
            accessTokenDTO.setCode(code);
            accessTokenDTO.setState(state);
            //获取token
            String accessToken = githubProvider.getAccessToken(accessTokenDTO);
            //使用token调用，获取user对象数据
            GithubUser githubUser = githubProvider.getUser(accessToken);
            if (githubUser!=null){
                User user = new User();
                user.setName(githubUser.getName());
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setCreateTime(System.currentTimeMillis());
                user.setGmtModified(user.getCreateTime());
                userMapper.insert(user);
                //登录成功，写cookie和session
                response.addCookie(new Cookie("token",token));
//                request.getSession().setAttribute("user",githubUser);
                return "redirect:/";
            }else{
                //登录失败
                return "redirect:/";
            }
    }
}
