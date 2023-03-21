package buzz.freelearner.server.exception;

import buzz.freelearner.server.pojo.ResponseBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * @author eva
 * @create 2021/4/28
 * @RestControllerAdvice 自定义异常拦截类
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(SQLException.class)
    public ResponseBean mysqlException(SQLException exception){
        //对数据库的操作违反了数据库的唯一约束条件
        //如删除主表记录或插入Unique字段重复的数据
        if(exception instanceof SQLIntegrityConstraintViolationException){
            return ResponseBean.error("数据已存在且存在关联数据!");
        }
        return ResponseBean.error("数据库异常，操作失败!");
    }

}
