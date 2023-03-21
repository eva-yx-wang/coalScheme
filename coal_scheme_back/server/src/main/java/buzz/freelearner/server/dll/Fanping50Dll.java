package buzz.freelearner.server.dll;

import com.sun.jna.Library;
import com.sun.jna.Native;
/**
 * 引入锅炉效率计算dll
 * @author eva
 * @create 2021/5/13
 */
public interface Fanping50Dll extends Library{
    Fanping50Dll fanping50dll = (Fanping50Dll) Native.load("fanping50.dll", Fanping50Dll.class);

    //计算锅炉效率
    public int fanping50(double mixedCAr, double mixedHAr, double mixedOAr, double mixedNAr, double mixedSAr,
                              double mixedMAr, double mixedAAr, double mixedVDaf, double qnetAr, double tEnv, double[] result);
}
