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
 * 配煤方案管理
 * </p>
 *
 * @author eva
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CoalScheme对象", description="配煤方案管理")
public class CoalScheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "方案编号")
    @TableId(value = "scheme_id", type = IdType.AUTO)
    private Integer schemeId;

    @ApiModelProperty(value = "混煤编号")
    private Integer mixedId;

    @ApiModelProperty(value = "最小可燃基挥发分(%)")
    private BigDecimal minVDaf;

    @ApiModelProperty(value = "最大收到基水分(%)")
    private BigDecimal maxMAr;

    @ApiModelProperty(value = "最大收到基灰分(%)")
    private BigDecimal maxAAr;

    @ApiModelProperty(value = "最大空干基硫分(%)")
    private BigDecimal maxSAd;

    @ApiModelProperty(value = "最小收到基低位发热量(kcal)")
    private BigDecimal minQnetAr;

    @ApiModelProperty(value = "最小软化温度(°C)")
    private BigDecimal minSoftT;

    @ApiModelProperty(value = "SOx估计(mg/Nm3)")
    private BigDecimal sox;

    @ApiModelProperty(value = "NOx估计(mg/Nm3)")
    private BigDecimal nox;

    @ApiModelProperty(value = "环境温度(°C)")
    private BigDecimal tEnv;

    @ApiModelProperty(value = "锅炉效率(%)")
    private BigDecimal boilerEff;

    @ApiModelProperty(value = "混煤煤耗(g/kwh)")
    private BigDecimal mixedConsumpKwh;

    @ApiModelProperty(value = "标煤煤耗(g/kwh)")
    private BigDecimal standConsumpKwh;

    @ApiModelProperty(value = "机组负荷(MW)")
    private BigDecimal boilderLoad;

    @ApiModelProperty(value = "主汽流量(t/h)")
    private BigDecimal steamFlow;

    @ApiModelProperty(value = "混煤单价(元/t)")
    private BigDecimal mixedPrice;

    @ApiModelProperty(value = "石灰单价(元/t)")
    private BigDecimal limePrice;

    @ApiModelProperty(value = "液氨单价(元/t)")
    private BigDecimal nh3Price;

    @ApiModelProperty(value = "上网电价(元/kwh)")
    private BigDecimal powerPrice;

    @ApiModelProperty(value = "石灰耗量(t/天)")
    private BigDecimal limeConsump;

    @ApiModelProperty(value = "石灰成本(元/天)")
    private BigDecimal limeCost;

    @ApiModelProperty(value = "液氨耗量(t/天)")
    private BigDecimal nh3Consump;

    @ApiModelProperty(value = "脱硝成本(元/天)")
    private BigDecimal nh3Cost;

    @ApiModelProperty(value = "日发电量(万度/天)")
    private BigDecimal powerGenerate;

    @ApiModelProperty(value = "厂用电率(%)")
    private BigDecimal ratioOwn;

    @ApiModelProperty(value = "日供电量(万度/天)")
    private BigDecimal powerSupply;

    @ApiModelProperty(value = "供电成本(元/kwh)")
    private BigDecimal powerCostKwh;

    @ApiModelProperty(value = "生产成本(元/天)")
    private BigDecimal powerCostDay;


}
