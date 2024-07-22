package com.hzy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author daocaoaren
 * @date 2024/7/19 20:54
 * @description :  股票最新分时行情数据
 */
@ApiModel("股票最新分时行情数据")
@Data
public class StockNewPriceDomain {
    /**
     * 最新交易量
     */
    @ApiModelProperty("最新交易量")
    private Long tradeAmt;

    /**
     * 前收盘价格
     */
    @ApiModelProperty("前收盘价")
     private BigDecimal preClosePrice;

    /**
     * 最低价
     */
    @ApiModelProperty("最低价")
    private BigDecimal lowPrice;

    /**
     * 最高价
     */
    @ApiModelProperty("最高价")
    private BigDecimal highPrice;
    /**
     * 开盘价
     */
    @ApiModelProperty("开盘价")
    private BigDecimal openPrice;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    private BigDecimal tradeVol;
    /**
     * 当前价格
     */
    @ApiModelProperty("当前价格")
    private BigDecimal tradePrice;
    /**
     * 当前日期
     */
    @ApiModelProperty("当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
}
