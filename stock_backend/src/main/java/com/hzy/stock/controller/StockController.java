package com.hzy.stock.controller;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.pojo.domain.StockUpdownDomain;
import com.hzy.stock.service.StockService;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:47
 * @description : 定义股票相关接口控制器
 */
@Api(value = "/api/quot/", tags = "股票相关接口")
@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 获取国内最新大盘的数据
     * @return
     */
    @ApiOperation(value = "获取国内最新大盘的数据",notes = "获取国内最新大盘的数据",httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }


    /**
     * 获取股票板块数据,获取上海深圳板块最新数据，以交易总金额降序查询，取前十条数据
     * @return
     */
    @ApiOperation(value = "获取股票板块数据",notes = "获取股票板块数据",httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getStockBlockInfoLimit(){
        return stockService.getStockBlockInfoLimit();
    }


    /**
     * 分页查询股票最新数据，并按照涨幅排序查询,展示出股票涨幅最大的数据
     * @param page
     * @param pageSize
     * @RequestParam注解的参数
     *          name用于指定请求头绑定的名称
     *          required是否必须，默认为true
     *          defaultValue默认值
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "page",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    @ApiOperation(value = "分页查询股票最新数据",notes = "分页查询股票最新数据",httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
                                                             @RequestParam(name = "pageSize", required = false,defaultValue = "20") Integer pageSize){
        return stockService.getStockPageInfo(page,pageSize);
    }

    /**
     * 设计股票涨幅榜模块，需求是查询涨幅榜最大的前4条数据展示在前端
     * @return
     */
    @ApiOperation(value = "设计股票涨幅榜模块",notes = "设计股票涨幅榜模块",httpMethod = "GET")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockIncreaseMax(){
        return stockService.getStockIncreaseMax();
    }


    /**
     * 统计股票最新交易日内每分钟的跌停的股票数量
     * @return
     */
    @ApiOperation(value = "统计股票最新交易日内每分钟的跌停的股票数量",notes = "统计股票最新交易日内每分钟的跌停的股票数量",httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }


    /**
     * 导出指定页码最新股票数据到excel
     * @param response  响应对象
     * @param page  当前页
     * @param pageSize 每页大小
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "page",value = "当前页"),//@RequestParam注解的参数paramType参数指定请求头绑定的名称
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "pageSize",value = "每页大小")
    })
    @ApiOperation(value = "导出指定页码最新股票数据到excel",notes = "导出指定页码最新股票数据到excel",httpMethod = "GET")
    @GetMapping("/stock/export")
    public void exportStockInfoByPage(HttpServletResponse response, @RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
                                                                   @RequestParam(name = "pageSize", required = false,defaultValue = "20") Integer pageSize){
        stockService.exportStockInFoByPage(response,page,pageSize);
    }


    /**
     * 统计A股大盘T日和T-1日成交量对比功能（成交量为沪深两市成交量之和），实现成交量功能
     * @return
     */
    @ApiOperation(value = "统计A股大盘T日和T-1日成交量对比功能",notes = "统计A股大盘T日和T-1日成交量对比功能",httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getStockTradeAmt(){
        return stockService.getStockTradeAmt();
    }


    /**
     * 统计当前时间下各个股票的涨幅区间的数量，完善各股涨跌图，涨幅区间分别为<-7% -7%~-5% -5%~-3% -3%~0% 0%~3% 3%~5% 5%~7% 7%>
     * @return
     */
    @ApiOperation(value = "统计当前时间下各个股票的涨幅区间的数量",notes = "统计当前时间下各个股票的涨幅区间的数量",httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map> getStockIncreaseRangInfo(){
        return stockService.getStockIncreaseRangeInfo();
    }



}
