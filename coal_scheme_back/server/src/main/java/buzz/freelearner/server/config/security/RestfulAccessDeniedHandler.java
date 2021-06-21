package buzz.freelearner.server.config.security;

import buzz.freelearner.server.pojo.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 无权限时访问接口， 返回的自定义结果commence()
 * @author eva eigenvectors
 * @create 2021/4/26
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        ResponseBean responseBean = ResponseBean.error("权限不足, 请联系管理员");
        responseBean.setStatusCode(403);
        printWriter.write(new ObjectMapper().writeValueAsString(responseBean));
        printWriter.flush();
        printWriter.close();
    }
}
