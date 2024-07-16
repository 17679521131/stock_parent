package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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



    /**
     *统计A股大盘T日和T-1日成交量
     * @param lastDate
     * @param openDate
     * @param innerCode
     * @return
     */
    List<Map> getStockTradeAmtMin(@Param("innerCode") List<String> innerCode,@Param("openDate") Date openDate,@Param("lastDate") Date lastDate);


    /**
     * 批量插入最新挖掘的股票大盘数据
     * @param list 封装成list集合了
     */
    int insertSelectData(@Param("list") ArrayList<StockMarketIndexInfo> list);
}
