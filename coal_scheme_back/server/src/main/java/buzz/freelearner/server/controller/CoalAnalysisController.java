package buzz.freelearner.server.controller;


import buzz.freelearner.server.mapper.CoalAnalysisMapper;
import buzz.freelearner.server.mapper.CoalInploMapper;
import buzz.freelearner.server.pojo.CoalAnalysis;
import buzz.freelearner.server.pojo.CoalInplo;
import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.service.ICoalAnalysisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 煤质管理 前端控制器
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
@RestController
@CrossOrigin
@RequestMapping("/coal/analysis")
public class CoalAnalysisController {
    //注入类
    @Autowired
    private ICoalAnalysisService coalAnalysisService;
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;

    @ApiOperation(value = "获取所选两个煤种的分析数据")
    @GetMapping("/getTwo/")
    public List<CoalAnalysis> getTwoAnalysis(Integer[] ids){
        //查询两条数据
        return coalAnalysisService.listByIds(Arrays.asList(ids));
    }
}
