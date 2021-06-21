package buzz.freelearner.server.config.security;

import buzz.freelearner.server.pojo.User;
import buzz.freelearner.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Security configuration
 * @author eva
 * @create 2021/4/24 download
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //注入类
    @Autowired
    private IUserService userService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    //1.重写userDetailsService
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        //1.重写username
        return username ->{
            //由IUserService内定义的接口获取LoginUser的username
            User user = userService.getUserByUserName(username);
            if(user != null){ return user;}
            return null;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){ return new JwtAuthenticationTokenFilter();}


    //2.放入重写的userDetailsService
    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder){
        try {
            //获取用户info时, 隐藏password
            authBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            System.out.println("无法执行重写的userDetailsService");
        }
    }

    //3.放行一些特殊路径, 不走拦截器
    @Override
    public void configure(WebSecurity web){
        //勿漏根路径"/",暂时全放行
        web.ignoring().antMatchers(
                "/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http){
        //使用token, 关闭csrf
        try {
            http.csrf()
                    .disable()
                    //使用token, 关闭session
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    //除了请求特殊路径, 其他请求均需要授权
                    .anyRequest()
                    .authenticated()
                    .and()
                    //禁用缓存
                    .headers()
                    .cacheControl();
            //添加jwt filter
            http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            //添加自定义未授权和未登录的自定义返回结果
            //详见RestfulAccessDeniedHandler, RestAuthorizationEntryPoint definition
            http.exceptionHandling()
                    .accessDeniedHandler(restfulAccessDeniedHandler)
                    .authenticationEntryPoint(restAuthorizationEntryPoint);
        } catch (Exception e) {
            System.out.println();
        }
    }
}
