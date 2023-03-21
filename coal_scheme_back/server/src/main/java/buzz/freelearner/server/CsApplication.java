package buzz.freelearner.server;
//启动类

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** coal-Scheme 应用启动器
 * @author eva
 * @create 2021/4/23
 */
@SpringBootApplication
@MapperScan("buzz.freelearner.server.mapper")
public class CsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsApplication.class, args);
    }


}
