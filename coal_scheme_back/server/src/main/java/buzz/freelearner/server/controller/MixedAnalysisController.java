package buzz.freelearner.server.controller;

import buzz.freelearner.server.dll.Fanping50Dll;
import buzz.freelearner.server.dll.SoftTDll;
import buzz.freelearner.server.mapper.CoalSchemeMapper;
import buzz.freelearner.server.pojo.CoalScheme;
import buzz.freelearner.server.pojo.MixedAnalysis;
import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.service.ICoalInploService;
import buzz.freelearner.server.service.ICoalSchemeService;
import buzz.freelearner.server.service.IMixedAnalysisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 * 混煤煤质管理 前端控制器
 * </p>
 *
 * @author eva
 * @since 2021-05-08
 */
@RestController
@CrossOrigin
@RequestMapping("/mixed-analysis")
public class MixedAnalysisController {

    //注入类
    //注意每个注入类都要加@Autowired
    @Autowired
    private IMixedAnalysisService mixedAnalysisService;
    @Autowired
    private ICoalInploService coalInploService;
    @Autowired
    private ICoalSchemeService coalSchemeService;
    @Autowired
    private CoalSchemeMapper coalSchemeMapper;


    @ApiOperation(value ="计算混煤数据并存储")
    @PostMapping("/add")
    public ResponseBean addMixedCoal(@RequestBody MixedAnalysis mixedAnalysis){
        //计算软化温度softT并设置
        double softT = SoftTDll.softTDll.hrdyc(mixedAnalysis.getAshAl2o3Ar().doubleValue(),
                mixedAnalysis.getAshSio2Ar().doubleValue(), mixedAnalysis.getAshCao().doubleValue(),
                mixedAnalysis.getAshFe2o3Ar().doubleValue(), mixedAnalysis.getAshMgo().doubleValue(),
                mixedAnalysis.getAshTio2().doubleValue(),mixedAnalysis.getAshSo3().doubleValue(),
                mixedAnalysis.getAshK2o().doubleValue());
        mixedAnalysis.setSoftT(new BigDecimal(softT).setScale(2, BigDecimal.ROUND_HALF_UP));
        //如果混煤数据未存在则保存
        //否则抛出SQLIntegrityConstraintViolationException
        if(mixedAnalysisService.save(mixedAnalysis)){
            return ResponseBean.success("保存混煤数据成功！");
        }
        return ResponseBean.error("操作失败！");
    }

    @ApiOperation(value ="根据混煤名称获取数据")
    @GetMapping("/get")
    public MixedAnalysis getMixedCoal(String mixedName){
        QueryWrapper<MixedAnalysis> queryWrapper = new QueryWrapper<>();
        return mixedAnalysisService.getOne(queryWrapper.eq("mixed_name", mixedName));
    }

    @ApiOperation(value = "初步存储配煤方案")
    @PostMapping("/add-scheme")
    public int addSimpleScheme540(@RequestBody CoalScheme coalScheme){

        MixedAnalysis mixedAnalysis =  mixedAnalysisService.getById(coalScheme.getMixedId());
        //煤价
        double coal1Price = coalInploService.getById(mixedAnalysis.getCoal1Id()).getCoalPrice().doubleValue();
        double coal2Price = coalInploService.getById(mixedAnalysis.getCoal2Id()).getCoalPrice().doubleValue();
        double ratio = mixedAnalysis.getCoal1Ratio().doubleValue() * 0.01;
        double mixedPrice = coal1Price * ratio + coal2Price * (1-ratio);
        coalScheme.setMixedPrice(new BigDecimal(mixedPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
        //锅炉效率、环境温度
        double mixedQnet = mixedAnalysis.getMixedQnetAr().doubleValue() * 4.187;
        double[] result = new double[10];
        double tEnv = 20.0;
        Fanping50Dll.fanping50dll.fanping50(mixedAnalysis.getMixedCAr().doubleValue(),
                mixedAnalysis.getMixedHAr().doubleValue(), mixedAnalysis.getMixedOAr().doubleValue(),
                mixedAnalysis.getMixedNAr().doubleValue(), mixedAnalysis.getMixedSAr().doubleValue(),
                mixedAnalysis.getMixedMAr().doubleValue(), mixedAnalysis.getMixedAAr().doubleValue(),
                mixedAnalysis.getMixedVDaf().doubleValue(), mixedQnet, tEnv, result);
        double boilerEff = result[0] - 1;
        coalScheme.setBoilerEff(new BigDecimal(boilerEff).setScale(2, BigDecimal.ROUND_HALF_UP));
        coalScheme.setTEnv(new BigDecimal(tEnv).setScale(2, BigDecimal.ROUND_HALF_UP));

        //厂用电率、机组负荷（540t/h）、日发电量(固定)
        double ratioOwn = 0.065;
        //机组负荷
        double boilerLoad = 178.00;
        double powerGenerate = boilerLoad * 24 / 10;
        double steamFlow = 540.00;
        coalScheme.setRatioOwn(new BigDecimal(ratioOwn * 100).setScale(2,BigDecimal.ROUND_HALF_UP));
        coalScheme.setBoilderLoad(new BigDecimal(boilerLoad).setScale(2,BigDecimal.ROUND_HALF_UP));
        coalScheme.setSteamFlow(new BigDecimal(steamFlow).setScale(2,BigDecimal.ROUND_HALF_UP));
        //小数位超出setScale范围则必须设置圆整
        coalScheme.setPowerGenerate(new BigDecimal(powerGenerate).setScale(2,BigDecimal.ROUND_HALF_UP));
        //日供电量
        double powerSupply = powerGenerate * (1 - ratioOwn);
        coalScheme.setPowerSupply(new BigDecimal(powerSupply).setScale(2,BigDecimal.ROUND_HALF_UP));
        //混煤煤耗、标煤煤耗
        double mixedConsumpKwh = (24 * (steamFlow * (3416 - 1016) + 431 * (3533-3051)) /
                (mixedQnet * boilerEff * 0.01)) * 100 / powerSupply;
        double standConsumpKwh = (mixedConsumpKwh * mixedQnet) / 29273;
        coalScheme.setMixedConsumpKwh(new BigDecimal(mixedConsumpKwh).setScale(2,BigDecimal.ROUND_HALF_UP));
        coalScheme.setStandConsumpKwh(new BigDecimal(standConsumpKwh).setScale(2,BigDecimal.ROUND_HALF_UP));

        //估计SOx、NOx(SOx有较大误差)
        double SOx = 1821.4 * mixedAnalysis.getMixedSAr().doubleValue() + 376.48;
        coalScheme.setSox(new BigDecimal(SOx).setScale(1,BigDecimal.ROUND_HALF_UP));
        double NOx = 212 + 155 * mixedAnalysis.getMixedNAr().doubleValue();
        coalScheme.setNox(new BigDecimal(NOx).setScale(1,BigDecimal.ROUND_HALF_UP));
        //理论空气量v0, 实际烟气量vg
        double v0 = 0.0889 * (mixedAnalysis.getMixedCAr().doubleValue() +
                0.375 * mixedAnalysis.getMixedSAr().doubleValue()) +
                0.265 * mixedAnalysis.getMixedHAr().doubleValue() - 0.0333 * mixedAnalysis.getMixedOAr().doubleValue();
        double vg = (1.886 * (mixedAnalysis.getMixedCAr().doubleValue() +
                0.375 * mixedAnalysis.getMixedSAr().doubleValue()) + 0.8 * mixedAnalysis.getMixedNAr().doubleValue() +
                11.1 * mixedAnalysis.getMixedHAr().doubleValue() + 1.24 * mixedAnalysis.getMixedMAr().doubleValue()) / 100
                + (0.79 + 0.0161 + 0.4 * 1.0161) * v0;
        //估计石灰耗量、液氨耗量
        double limeConsump = ((SOx - 20) * 1.02 * mixedConsumpKwh * powerSupply * vg)/(64 * Math.pow(10, 6));
        double nh3Consump = ((NOx - 35) * 72 * 17 * mixedConsumpKwh * powerSupply * vg)/(46 * Math.pow(10, 10));
        coalScheme.setLimeConsump(new BigDecimal(limeConsump).setScale(1,BigDecimal.ROUND_HALF_UP));
        coalScheme.setNh3Consump(new BigDecimal(nh3Consump).setScale(2,BigDecimal.ROUND_HALF_UP));

       if(coalSchemeService.save(coalScheme)){
           return coalScheme.getSchemeId();}
       return 0;
    }
}
