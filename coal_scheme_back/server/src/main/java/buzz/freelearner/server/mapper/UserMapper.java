package buzz.freelearner.server.mapper;

import buzz.freelearner.server.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户管理 Mapper 接口
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
