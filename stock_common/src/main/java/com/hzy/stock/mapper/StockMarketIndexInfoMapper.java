package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author daocaoaren
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /**
     * 根据当前时间点查询指定大盘编码对应的数据
     * @param newDate 当前时间点
     * @param innerCode 大盘编码集合
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("newDate") Date newDate,@Param("innerCode") List<String> innerCode);

}
