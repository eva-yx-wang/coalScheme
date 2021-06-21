package buzz.freelearner.server.controller;


import buzz.freelearner.server.pojo.CoalInplo;
import buzz.freelearner.server.pojo.ResponseBean;
import buzz.freelearner.server.service.ICoalInploService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 煤种查询 前端控制器
 * </p>
 *
 * @author eva
 * @since 2021-04-24
 */
@RestController
@CrossOrigin
@RequestMapping("/coal/list")
public class CoalInploController {
    //注入类
    @Autowired
    private ICoalInploService coalInploService;

    //api
    //api说明
    @ApiOperation(value = "获取所有煤种")
    //api 请求方式及地址url
    @GetMapping("/get")
    public List<CoalInplo> getCoalList(){
        return coalInploService.list();
    }

    @ApiOperation(value ="添加煤种")
    @PostMapping("/add")
    public ResponseBean addCoalInplo(@RequestBody CoalInplo coalInplo){
        //save 成功则返回success
        if(coalInploService.save(coalInplo)){
            return ResponseBean.success("添加成功！");
        }
        return ResponseBean.error("操作失败!");
    }

    @ApiOperation(value ="更新煤种")
    @PutMapping("/update")
    public ResponseBean updateCoalInplo(@RequestBody CoalInplo coalInplo){
        //save 成功则返回success
        if(coalInploService.updateById(coalInplo)){
            return ResponseBean.success("更新成功！");
        }
        return ResponseBean.error("操作失败!");
    }

    @ApiOperation(value ="删除单个煤种")
    //deleteRequest匹配的url, 需要预访问
    //更新接口必须要重启服务器
    @DeleteMapping("/deleteSingle/{id}")
    public ResponseBean deleteCoalInploById(@PathVariable Integer id){
        //save 成功则返回success
        System.out.println(id);
        if(coalInploService.removeById(id)){
            return ResponseBean.success("删除成功！");
        }
        return ResponseBean.error("操作失败!");
    }

    @ApiOperation(value ="删除批量煤种")
    //deleteRequest匹配的url
    @DeleteMapping("/deleteBatch/")
    public ResponseBean deleteCoalInploByIds(Integer[] ids){
        //save 成功则返回success
        if(coalInploService.removeByIds(Arrays.asList(ids))){
            return ResponseBean.success("批量删除成功！");
        }
        return ResponseBean.error("操作失败!");
    }
}
