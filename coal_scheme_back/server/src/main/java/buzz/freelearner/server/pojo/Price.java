package buzz.freelearner.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author eva
 * @create 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Price", description = "")
public class Price {
    @ApiModelProperty(value="方案ID", required = true)
    private int schemeId;

    @ApiModelProperty(value = "石灰单价", required = true)
    private double limePrice;

    @ApiModelProperty(value = "液氨单价", required = true)
    private double nh3Price;

    @ApiModelProperty(value = "上网电价", required = true)
    private double powerPrice;
}
