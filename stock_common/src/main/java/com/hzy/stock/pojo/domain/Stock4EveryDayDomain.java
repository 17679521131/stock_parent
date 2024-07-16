package com.hzy.stock.pojo.domain;

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
 * @date 2024/7/17 10:28
 * @description : 个股日K数据封装/绘制每日K线图
 */
@ApiModel(value = "个股日K数据封装/绘制每日K线图")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4EveryDayDomain {
    /**
     * 日期，eg:202201280809
     */
    @ApiModelProperty(value = "日期当前日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date date;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量")
    private Long tradeAmt;
    /**
     * 股票编码
     */
    @ApiModelProperty(value = "股票编码")
    private String code;
    /**
     * 最低价
     */
    @ApiModelProperty(value = "最低价")
    private BigDecimal lowPrice;
    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称")
    private String name;
    /**
     * 最高价
     */
    @ApiModelProperty(value = "最高价")
    private BigDecimal highPrice;
    /**
     * 开盘价
     */
    @ApiModelProperty(value = "开盘价")
    private BigDecimal openPrice;
    /**
     * 当前交易总金额
     */
    @ApiModelProperty(value = "当前交易总金额")
    private BigDecimal tradeVol;
    /**
     * 当前收盘价格指收盘时的价格，如果当天未收盘，则显示最新cur_price）
     */
    @ApiModelProperty(value = "当前收盘价格指收盘时的价格，如果当天未收盘，则显示最新cur_price）")
    private BigDecimal closePrice;
    /**
     * 前收盘价
     */
    @ApiModelProperty(value = "前收盘价")
    private BigDecimal preClosePrice;
}
