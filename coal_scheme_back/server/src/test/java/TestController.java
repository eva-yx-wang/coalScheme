import buzz.freelearner.server.dll.Fanping50Dll;
import buzz.freelearner.server.dll.SoftTDll;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eva
 * @create 2021/4/26
 */
@RestController
public class TestController {

    //测试SoftTDll的软化温度计算函数hrdyc
    @Test
    public void TestDll(){
        double[] result = new double[10];
        Fanping50Dll.fanping50dll.fanping50(55.22,3.80,13.14,0.70,
                0.34,7.11,19.70,42.09, 4915.84*4.187, 20, result);
        double boilerEff = result[0] - 1;
        System.out.println(boilerEff);
    }
}
