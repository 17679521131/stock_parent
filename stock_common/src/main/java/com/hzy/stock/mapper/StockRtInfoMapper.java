package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.StockUpdownDomain;
import com.hzy.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author daocaoaren
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询,展示出股票涨幅最大的数据
     * @param newDate 当前最新时间对象
     * @return
     */
    List<StockUpdownDomain> getNewDateStockPageInfo(@Param("newDate") Date newDate);

    /**
     * 设计股票涨幅榜模块，需求是查询涨幅榜最大的前4条数据展示在前端
     * @return
     */
    List<StockUpdownDomain> getStockIncreaseMax(@Param("newDate") Date newDate);

    /**
     * 统计指定时间范围内，股票涨跌停的数量流水
     * @param newStartDate 最新的股票开盘时间
     * @param newEndDate 最新截止时间
     * @param flag 1表示统计涨停，0表示跌停
     * @return
     */
    List<Map> getStockUpDownCount(@Param("newStartDate") Date newStartDate,@Param("newEndDate") Date newEndDate,@Param("flag") int flag);

    /**
     * 获取指定时间内，各个涨幅区间的各个股票数量
     * @param newDate 当前时间
     * @return
     */
    List<Map> getStockIncreaseRangeInfoByDate(@Param("newDate") Date newDate);
}
