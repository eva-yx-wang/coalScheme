package buzz.freelearner.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
//注意用的是springfox下的SecurityContext
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eva
 * @create 2021/4/26
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    //1.生成swagger documentation
    @Bean
    public Docket createRestApi(){
        //1.1 扫描哪些包去生成swagger doc
        return new Docket(DocumentationType.SWAGGER_2)
                //1.1.1传入api information
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("buzz.freelearner.server.controller"))
                .paths(PathSelectors.any())
                .build()
                //1.1.2 添加全局authorization
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    //1.1.1 定义apiInfo()
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("配煤管理系统接口文档")
                .description("接口使用说明")
                .contact(new Contact("eva", "http:localhost:8081/doc.html", "eva_yx_wang.163.com"))
                .version("0.0.1")
                .build();

    }

    //1.1.2 全局authorization
    //1.1.2.1 设置请求头info
    private List<ApiKey> securitySchemes(){
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("ApiAuthorization", "Authorization", "Header");
        result.add(apiKey);
        return result;
    }

    //1.1.2.2 设置授权路径
    private List<SecurityContext> securityContexts(){
        List<SecurityContext> result = new ArrayList<>();
        //使用Regular Expression
        //匹配所有以"/hello/"开头的url_path
        result.add(getContextByPath("/hello/.*"));
        return result;
    }
    //1.1.2.2.1 授权路径搜索
    private SecurityContext getContextByPath(String pathRegEx){
        return SecurityContext.builder()
                .securityReferences(defaultAuthorization())
                .forPaths(PathSelectors.regex(pathRegEx))
                .build();
    }

    //1.1.2.2.2 set default-authorization
    private List<SecurityReference> defaultAuthorization() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global",
                "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }
}
