package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.StockBusinessDomain;
import com.hzy.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author daocaoaren
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(String id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    /**
     * 查询所有A股各个股票的编码
     * @return
     */
    List<String>  getAllStockCode();

    /**
     * 根据模糊字符串查询股票名字和编码
     * @param searchStr
     * @return
     */
    List<Map> getStockNameAndCodeByStr(@Param("searchStr") String searchStr);

    /**
     * 根据股票编码查询股票的行业信息
     * @param code
     * @return
     */
    StockBusinessDomain getStockBusinessInfoByCode(@Param("code") String code);
}
