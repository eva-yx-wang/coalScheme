package buzz.freelearner.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查每次请求头中的Token并判断是否过滤
 * @author eva
 * @create 2021/4/24
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    //注入类
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value(("${jwt.tokenHead}"))
    private String tokenHead;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //从request中获取要验证的header
        String requestHeader = request.getHeader(tokenHeader);
        //has token
        if(requestHeader != null && requestHeader.startsWith(tokenHead)){
            String requestToken = requestHeader.substring(tokenHead.length());
            String username = jwtTokenUtils.getUserNameFromToken(requestToken);
            //token存在但用户未登录
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //用户登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证requstHeader中的token是否valid, 有效则重新设置token到用户对象中
                if(jwtTokenUtils.isValidToken(requestToken, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
