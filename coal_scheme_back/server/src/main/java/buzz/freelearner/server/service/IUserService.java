package buzz.freelearner.server.service;

import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户管理 服务类
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
public interface IUserService extends IService<User> {
    /**
     * return token after login
     * @param username
     * @param password
     * @param request
     * @return
     */
    ResponseBean login(String username, String password, HttpServletRequest request);

    /**
     * 由UserLogin 获取数据库封装的User 类接口
     * @param username
     * @return
     */
    User getUserByUserName(String username);
}
