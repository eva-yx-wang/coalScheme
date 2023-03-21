package buzz.freelearner.server.dll;

import com.sun.jna.Library;
import com.sun.jna.Native;
/**
 * 计算软化温度ST的dll
 * @author eva
 * @create 2021/5/8
 */
public interface SoftTDll  extends Library {
    SoftTDll softTDll = (SoftTDll) Native.load("HRDYCgsjy.dll", SoftTDll.class);

    //计算软化温度softT
    public double hrdyc(double ashAl2o3Ar, double SiO2, double ashCao,
                        double ashFe2o3Ar,double ashMgo, double ashTio2, double ashSo3, double ashK2o);
}
