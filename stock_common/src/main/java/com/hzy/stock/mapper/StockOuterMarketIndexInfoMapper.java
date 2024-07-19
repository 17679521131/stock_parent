package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.OuterMarketDomain;
import com.hzy.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author daocaoaren
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    /**
     * 由于国外大盘开盘时间不稳定，所以我们是通过时间和大盘点数降序获取
     * @return
     */
    List<OuterMarketDomain> getOutMarketInfoByDate();

    /**
     * 插入最新获取的国外大盘数据
     * @param list 国外大盘数据集合
     * @return
     */
    int insertAllData(@Param("list") List<StockOuterMarketIndexInfo> list);
}
