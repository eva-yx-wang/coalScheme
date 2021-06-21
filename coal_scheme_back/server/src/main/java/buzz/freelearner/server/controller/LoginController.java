package buzz.freelearner.server.controller;

import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.pojo.User;
import buzz.freelearner.server.pojo.UserLogin;
import buzz.freelearner.server.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Login
 * @author eva review
 * @create 2021/4/24
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {
    //注入类
    @Autowired
    private IUserService userService;

    //返回公共类 ResponseBean, @CrossOrigin允许跨域访问
    @ApiOperation(value = "登录后返回token")
    @CrossOrigin
    @PostMapping("/login")
    public ResponseBean login(@RequestBody UserLogin userLogin, HttpServletRequest request){
        return userService.login(userLogin.getUsername(), userLogin.getPassword(), request);
    }

    //获取数据库封装的User类
    @ApiOperation(value = "获取当前登录信息")
    @GetMapping("/user/info")
    public User getUserInfo(Principal principal){
        //单独退化
        if(principal == null){
            return null;
        }
        String username = principal.getName();
        User user =  userService.getUserByUserName(username);
        //屏蔽密码, 保护用户数据
        user.setUserPassword(null);
        return user;
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ResponseBean logout(){
        //前端删除token信息，后端仅仅返回200状态码
        return ResponseBean.success("退出成功！");
    }
}
