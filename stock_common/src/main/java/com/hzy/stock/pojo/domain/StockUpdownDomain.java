package com.hzy.stock.pojo.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author daocaoaren
 * @date 2024/7/15 21:05
 * @description : 股票涨停信息/股票涨幅信息表/对涨幅榜
 */
@ApiModel("股票涨跌信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdownDomain {
    /**
     * 股票编码
     */
    @ApiModelProperty("股票编码")
    @ExcelProperty(value = {"股票涨停信息统计表","股票编码"},index = 0)
    private String code;

    /**
     * 股票名称
     */
    @ApiModelProperty("股票名称")
    @ExcelProperty(value = {"股票涨停信息统计表","股票名称"},index = 1)
    private String name;

    /**
     * 前收盘价
     */
    @ApiModelProperty("前收盘价")
    @ExcelProperty(value = {"股票涨停信息统计表","前收盘价"},index = 2)
    private BigDecimal preClosePrice;

    /**
     * 当前价格
     */
    @ApiModelProperty("当前价格")
    @ExcelProperty(value = {"股票涨停信息统计表","当前价格"},index = 3)
    private BigDecimal tradePrice;

    /**
     * 涨跌
     */
    @ApiModelProperty("涨跌")
    @ExcelProperty(value = {"股票涨停信息统计表","涨跌"},index = 4)
    private BigDecimal increase;

    /**
     * 涨幅
     */
    @ApiModelProperty("涨幅")
    @ExcelProperty(value = {"股票涨停信息统计表","涨幅"},index = 5)
    private BigDecimal upDown;

    /**
     * 振幅
     */
    @ApiModelProperty("振幅")
    @ExcelProperty(value = {"股票涨停信息统计表","振幅"},index = 6)
    private BigDecimal amplitude;

    /**
     * 成交量
     */
    @ApiModelProperty("成交量")
    @ExcelProperty(value = {"股票涨停信息统计表","成交量"},index = 7)
    private Long tradeAmt;

    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    @ExcelProperty(value = {"股票涨停信息统计表","交易金额"},index = 8)
    private BigDecimal tradeVol;

    /**
     * 当前日期
     */
    @ApiModelProperty("当前日期")
    @ExcelProperty(value = {"股票涨幅信息统计表","日期"},index = 9)
    @DateTimeFormat("yyy-MM-dd HH:mm")//easyExcel的注解-》excel
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")//springmvc支持的注解-》json格式数据
    private Date curDate;
}
