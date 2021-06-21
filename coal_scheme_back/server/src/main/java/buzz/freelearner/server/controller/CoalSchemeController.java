package buzz.freelearner.server.controller;


import buzz.freelearner.server.pojo.CoalScheme;
import buzz.freelearner.server.pojo.Price;
import buzz.freelearner.server.service.ICoalSchemeService;
import buzz.freelearner.server.service.impl.CoalSchemeServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 配煤方案管理 前端控制器
 * </p>
 *
 * @author eva
 * @since 2021-05-13
 */
@RestController
@CrossOrigin
@RequestMapping("/coal-scheme")
public class CoalSchemeController {

    @Autowired
    private ICoalSchemeService coalSchemeService;

    @ApiOperation(value ="计算成本占比随配比变化")
    @PostMapping("/get-data")
    public CoalScheme getSchemeData(@RequestBody Price price){

        CoalScheme scheme = coalSchemeService.getById(price.getSchemeId());
        if(scheme != null){
            //计算脱硫、脱硝成本、总成本
            double limePrice = price.getLimePrice();
            double nh3Price = price.getNh3Price();
            double powerPrice = price.getPowerPrice();
            //脱硫成本
            double limeCost = limePrice * scheme.getLimeConsump().doubleValue();
            //脱硝成本
            double nh3Cost = nh3Price * scheme.getNh3Consump().doubleValue();
            //总成本 = 燃料成本 + 脱硫成本 + 脱硝成本 + 厂用电成本
            //日供电成本(元/day)
            double powerCostDay = scheme.getMixedConsumpKwh().doubleValue() * scheme.getPowerSupply().doubleValue() *
                    0.01 * scheme.getMixedPrice().doubleValue() + limeCost + nh3Cost +
                    100 * scheme.getPowerGenerate().doubleValue() * scheme.getRatioOwn().doubleValue() * powerPrice;
            //日供电成本(元/kwh)
            double powerCostKwh = powerCostDay / (scheme.getPowerSupply().doubleValue() * 10000);

            //存储数据
            scheme.setLimePrice(new BigDecimal(limePrice).setScale(1,BigDecimal.ROUND_HALF_UP));
            scheme.setNh3Price(new BigDecimal(price.getNh3Price()).setScale(2,BigDecimal.ROUND_HALF_UP));
            scheme.setPowerPrice(new BigDecimal(price.getPowerPrice()).setScale(4,BigDecimal.ROUND_HALF_UP));
            scheme.setLimeCost(new BigDecimal(limeCost).setScale(1,BigDecimal.ROUND_HALF_UP));
            scheme.setNh3Cost(new BigDecimal(nh3Cost).setScale(1,BigDecimal.ROUND_HALF_UP));
            scheme.setPowerCostKwh(new BigDecimal(powerCostKwh).setScale(4,BigDecimal.ROUND_HALF_UP));
            scheme.setPowerCostDay(new BigDecimal(powerCostDay).setScale(1,BigDecimal.ROUND_HALF_UP));

            coalSchemeService.updateById(scheme);
            return scheme;
        }

        return null;
    }

}
