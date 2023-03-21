package buzz.freelearner.server.config.security;

import buzz.freelearner.server.pojo.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录或token expired 时访问接口， 返回的自定义结果commence()
 * @author eva
 * @create 2021/4/25
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        ResponseBean responseBean = ResponseBean.error("未登录或tokens过期！");
        responseBean.setStatusCode(401);
        printWriter.write(new ObjectMapper().writeValueAsString(responseBean));
        printWriter.flush();
        printWriter.close();
    }
}
