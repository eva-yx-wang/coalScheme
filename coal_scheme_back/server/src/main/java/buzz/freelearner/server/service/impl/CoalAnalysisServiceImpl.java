package buzz.freelearner.server.service.impl;

import buzz.freelearner.server.mapper.CoalAnalysisMapper;
import buzz.freelearner.server.pojo.CoalAnalysis;
import buzz.freelearner.server.service.ICoalAnalysisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author eva
 * @create 2021/4/30
 */
@Service
public class CoalAnalysisServiceImpl extends ServiceImpl<CoalAnalysisMapper, CoalAnalysis>
        implements ICoalAnalysisService {
}
