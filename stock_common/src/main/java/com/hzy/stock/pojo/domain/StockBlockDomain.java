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
 * @date 2024/7/15 20:25
 * @description :股票板块区域类
 */
@ApiModel("股票板块区域类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockBlockDomain {
    /**
     * 公司数量
     */
    @ApiModelProperty("公司数量")
    private Integer companyNum;
    /**
     * 交易量
     */
    @ApiModelProperty("交易量")
    private Long tradeAmt;
    /**
     * 板块编码
     */
    @ApiModelProperty("板块编码")
    private String code;
    /**
     * 平均价
     */
    @ApiModelProperty("平均价")
    private BigDecimal avgPrice;
    /**
     * 板块名称
     */
    @ApiModelProperty("板块名称")
    private String name;
    /**
     * 当前日期
     */
    @ApiModelProperty("当前日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
    /**
     *交易金额
     */
    @ApiModelProperty("交易金额")
    private BigDecimal tradeVol;
    /**
     * 涨跌率
     */
    @ApiModelProperty("涨跌率")
    private BigDecimal updownRate;
}
