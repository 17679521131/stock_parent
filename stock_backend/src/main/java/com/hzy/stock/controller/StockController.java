package com.hzy.stock.controller;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.service.StockService;
import com.hzy.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
