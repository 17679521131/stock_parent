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
 * @date 2024/7/17 10:00
 * @description : 各股分时数据封装
 */
@ApiModel("各股分时数据封装绘制分K线图")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4MinuteDomain {
    /**
     * 日期，eg:202201280809
     */
    @ApiModelProperty("日期表示当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date date;
    /**
     * 交易量
     */
    @ApiModelProperty("交易量")
    private Long tradeAmt;
    /**
     * 股票编码
     */
    @ApiModelProperty("股票编码")
    private String code;
    /**
     * 最低价
     */
    @ApiModelProperty("最低价")
    private BigDecimal lowPrice;
    /**
     * 前收盘价
     */
    @ApiModelProperty("前收盘价")
    private BigDecimal preClosePrice;
    /**
     * 股票名称
     */
    @ApiModelProperty("股票名称")
    private String name;
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
     * 当前交易总金额
     */
    @ApiModelProperty("当前交易总金额")
    private BigDecimal tradeVol;
    /**
     * 当前价格
     */
    @ApiModelProperty("当前价格")
    private BigDecimal tradePrice;
}
