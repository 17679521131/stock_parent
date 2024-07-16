package com.hzy.stock.mapper;

import com.hzy.stock.pojo.entity.StockBusiness;

import java.util.List;

/**
* @author daocaoaren
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    /**
     * 查询所有A股各个股票的编码
     * @return
     */
    List<String>  getAllStockCode();

}
