package buzz.freelearner.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 煤种查询
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CoalInplo对象", description="煤种查询")
public class CoalInplo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "煤种编号")
    @TableId(value = "coal_id", type = IdType.AUTO)
    private Integer coalId;

    @ApiModelProperty(value = "煤种名称")
    private String coalName;

    @ApiModelProperty(value = "煤种价格")
    private BigDecimal coalPrice;

    @ApiModelProperty(value = "煤种储量")
    private BigDecimal coalLoad;

    @ApiModelProperty(value = "煤种产地")
    private String coalOrigin;


}
