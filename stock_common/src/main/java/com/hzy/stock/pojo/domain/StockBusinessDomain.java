package com.hzy.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/19 20:24
 * @description : 个人股票描述信息领域
 */
@ApiModel(description = "个人股票描述信息领域")
@Data
public class StockBusinessDomain {

    /**
     * 股票编码
     */
    @ApiModelProperty(value = "股票编码")
    private String code;

    /**
     * 股票所属行业
     */
    @ApiModelProperty(value = "股票所属行业")
    private String trade;

    /**
     * 股票主营业务描述
     */
    @ApiModelProperty(value = "股票主营业务描述")
    private String business;

    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称")
    private String name;
}
