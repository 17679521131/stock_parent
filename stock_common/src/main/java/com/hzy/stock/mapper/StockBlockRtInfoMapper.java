package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.pojo.entity.StockBlockRtInfo;

import java.util.Date;
import java.util.List;

/**
* @author daocaoaren
* @description 针对表【stock_block_rt_info(股票板块详情信息表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockBlockRtInfo
*/
public interface StockBlockRtInfoMapper {




    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

    /**
     * 获取股票板块实时信息,沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @param lastDate
     * @return
     */
    List<StockBlockDomain> getStockBlockInfoLimit(Date lastDate);
}
