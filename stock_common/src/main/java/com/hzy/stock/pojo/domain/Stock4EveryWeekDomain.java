package com.hzy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @date 2024/7/20 11:11
 * @description : 绘制周k线
 */
@ApiModel(value = "个股日K数据封装/绘制每周K线图")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4EveryWeekDomain {
    /**
     * 股票编码
     */
    private String stockCode;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 平均价
     */
    private BigDecimal avgPrice;
    /**
     * 最低价
     */
    private BigDecimal minPrice;
    /**
     * 最高价
     */
    private BigDecimal maxPrice;
    /**
     * 收盘价
     */
    private BigDecimal closePrice;

    /**
     * 一周的最大时间，一般指周五
     */
    @JsonFormat(pattern = "yyy-MM-dd")
    private Date mxTime;

    /**
     * 每周的收盘时间点
     * 临时字段，前端无需展示
     */
    @JsonIgnore
    private Date miTime;

}
