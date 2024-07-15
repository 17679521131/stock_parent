package com.hzy.stock.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:23
 * @description :定义股票相关的值对象
 */
@ApiModel(description = "定义股票相关值对象封装")
@ConfigurationProperties(prefix = "stock")
//@Component
@Data
public class StockInfoConfig {
    //A股大盘ID集合
    @ApiModelProperty("封装国内A股大盘编码集合")
    private List<String> inner;


    //外盘ID集合
    @ApiModelProperty("封装国外大盘编码集合")
    private List<String> outer;
}
