package buzz.freelearner.server.service.impl;

import buzz.freelearner.server.config.security.JwtTokenUtils;
import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.pojo.User;
import buzz.freelearner.server.mapper.UserMapper;
import buzz.freelearner.server.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户管理 服务实现类
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
//不要漏@Service
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //注入类
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * return token after login
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public ResponseBean login(String username, String password, HttpServletRequest request) {
        //获取登录名并用解码器解码登录密码
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //登录失败：user为空 且解码器解码后密码匹配
//        if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())){
//            return ResponseBean.error("用户名或密码错误！");
//        }else if(!userDetails.isEnabled()){
//            return ResponseBean.error("账户被禁用, 请联系管理员");
//        }

        //登录失败：不存在这个用户名或密码不匹配 (不用上面的密码解码, 因为数据库内暂存的是真实密码
        if(userDetails == null || !password.equals(userDetails.getPassword())){
            return ResponseBean.error("用户名或密码错误！");
        }else if(!userDetails.isEnabled()){
            return ResponseBean.error("账户被禁用, 请联系管理员");
        }

        //update security UserLogin
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //登录成功, 动态生成token
        String token = jwtTokenUtils.generateToken(userDetails);
        //返回tokenMap到前端
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ResponseBean.success("登录成功", tokenMap);
    }

    /**
     * 由UserLogin的username 获取数据库查询结果并封装为User类返回
     * @param username
     * @return
     */
    @Override
    public User getUserByUserName(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("user_name",
                username).eq("enabled", true));
    }

}
