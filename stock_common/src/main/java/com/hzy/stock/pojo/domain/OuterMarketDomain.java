package com.hzy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author daocaoaren
 * @date 2024/7/19 17:20
 * @description : 外盘指数领域对象
 */
@Data
@ApiModel("外盘指数")
public class OuterMarketDomain {
    /**
     * 大盘名称
     */
    @ApiModelProperty("大盘名称")
     private String name;

    /**
     * 当前大盘点
     */
    @ApiModelProperty("当前大盘点")
    private BigDecimal curPoint;
    /**
     * 涨跌值
     */
    @ApiModelProperty("涨跌值")
    private BigDecimal upDown;

    /**
     * 涨幅
     */
    @ApiModelProperty("涨幅")
    private BigDecimal rose;
    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}
