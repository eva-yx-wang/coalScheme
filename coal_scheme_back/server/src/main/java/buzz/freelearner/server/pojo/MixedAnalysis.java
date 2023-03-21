package buzz.freelearner.server.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 混煤煤质管理
 * </p>
 *
 * @author eva
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MixedAnalysis对象", description="混煤煤质管理")
public class MixedAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "方案编号")
    @TableId(value = "mixed_id", type = IdType.AUTO)
    private Integer mixedId;

    @ApiModelProperty(value = "方案名称")
    private String mixedName;

    @ApiModelProperty(value = "煤种1编号")
    private Integer coal1Id;

    @ApiModelProperty(value = "煤种2编号")
    private Integer coal2Id;

    @ApiModelProperty(value = "煤种1比率(%)")
    private BigDecimal coal1Ratio;

    @ApiModelProperty(value = "收到基碳(%)")
    private BigDecimal mixedCAr;

    @ApiModelProperty(value = "收到基氢(%)")
    private BigDecimal mixedHAr;

    @ApiModelProperty(value = "收到基氧(%)")
    private BigDecimal mixedOAr;

    @ApiModelProperty(value = "收到基氮(%)")
    private BigDecimal mixedNAr;

    @ApiModelProperty(value = "收到基全硫(%)")
    private BigDecimal mixedSAr;

    @ApiModelProperty(value = "空干基硫(%)")
    private BigDecimal mixedSAd;

    @ApiModelProperty(value = "收到基灰分(%)")
    private BigDecimal mixedAAr;

    @ApiModelProperty(value = "收到基全水分(%)")
    private BigDecimal mixedMAr;

    @ApiModelProperty(value = "空干基水分(%)")
    private BigDecimal mixedMAd;

    @ApiModelProperty(value = "可燃基挥发分(%)")
    private BigDecimal mixedVDaf;

    @ApiModelProperty(value = "收到基低位发热量(kcal)")
    private BigDecimal mixedQnetAr;

    @ApiModelProperty(value = "二氧化硅(%)")
    private BigDecimal ashSio2Ar;

    @ApiModelProperty(value = "三氧化二铝(%)")
    private BigDecimal ashAl2o3Ar;

    @ApiModelProperty(value = "三氧化二铁(%)")
    private BigDecimal ashFe2o3Ar;

    @ApiModelProperty(value = "二氧化钛(%)")
    private BigDecimal ashTio2;

    @ApiModelProperty(value = "氧化钙(%)")
    private BigDecimal ashCao;

    @ApiModelProperty(value = "氧化镁(%)")
    private BigDecimal ashMgo;

    @ApiModelProperty(value = "三氧化硫(%)")
    private BigDecimal ashSo3;

    @ApiModelProperty(value = "氧化钾(%)")
    private BigDecimal ashK2o;

    @ApiModelProperty(value = "软化温度(°C)")
    private BigDecimal softT;


}
