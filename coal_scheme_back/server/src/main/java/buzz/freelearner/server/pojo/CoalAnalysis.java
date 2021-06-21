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
 * 煤质管理
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CoalAnalysis对象", description="煤质管理")
public class CoalAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "煤种编号")
    @TableId(value = "coal_id", type = IdType.AUTO)
    private Integer coalId;

    @ApiModelProperty(value = "收到基全水分")
    private BigDecimal coalMAr;

    @ApiModelProperty(value = "空干基水分")
    private BigDecimal coalMAd;

    @ApiModelProperty(value = "收到基灰分")
    private BigDecimal coalAAr;

    @ApiModelProperty(value = "可燃基挥发分")
    private BigDecimal coalVDaf;

    @ApiModelProperty(value = "空干基挥发分")
    private BigDecimal coalVAd;

    @ApiModelProperty(value = "低位发热量")
    private BigDecimal coalQnet;

    @ApiModelProperty(value = "收到基碳")
    private BigDecimal coalCAr;

    @ApiModelProperty(value = "收到基氢")
    private BigDecimal coalHAr;

    @ApiModelProperty(value = "收到基氮")
    private BigDecimal coalNAr;

    @ApiModelProperty(value = "收到基全硫")
    private BigDecimal coalSAr;

    @ApiModelProperty(value = "收到基氧")
    private BigDecimal coalOAr;

    @ApiModelProperty(value = "灰变形温度")
    private BigDecimal ashDtAr;

    @ApiModelProperty(value = "灰软化温度")
    private BigDecimal ashStAr;

    @ApiModelProperty(value = "灰流动温度")
    private BigDecimal ashFtAr;

    @ApiModelProperty(value = "二氧化硅")
    private BigDecimal ashSio2Ar;

    @ApiModelProperty(value = "三氧化二铝")
    private BigDecimal ashAl2o3Ar;

    @ApiModelProperty(value = "三氧化二铁")
    private BigDecimal ashFe2o3Ar;

    @ApiModelProperty(value = "二氧化钛")
    private BigDecimal ashTio2;

    @ApiModelProperty(value = "氧化钙")
    private BigDecimal ashCao;

    @ApiModelProperty(value = "氧化镁")
    private BigDecimal ashMgo;

    @ApiModelProperty(value = "三氧化硫")
    private BigDecimal ashSo3;

    @ApiModelProperty(value = "氧化钾")
    private BigDecimal ashK2o;

    @ApiModelProperty(value = "氧化钠")
    private BigDecimal ashNa20;


}
