package buzz.freelearner.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * public response Bean object
 * @author eva
 * @create 2021/4/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {
    //status code like: 200
    private long statusCode;
    private String message;
    private Object object;

    /**
     * successful response with null object
     * @param message
     * @return
     */
    public static ResponseBean success(String message){
        return new ResponseBean(200, message, null);
    }

    /**
     * successful response with object
     * @param message
     * @param object
     * @return
     */
    public static ResponseBean success(String message, Object object){
        return new ResponseBean(200, message, object);
    }

    /**
     * error response with null object
     * @param message
     * @return
     */
    public static ResponseBean error(String message){
        return new ResponseBean(500, message, null);
    }

    /**
     * error response with object
     * @param message
     * @param object
     * @return
     */
    public static ResponseBean error(String message, Object object){
        return new ResponseBean(500, message, object);
    }
}
