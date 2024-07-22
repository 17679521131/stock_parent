package com.hzy.stock.mapper;

import com.hzy.stock.pojo.domain.*;
import com.hzy.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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

    /**
     * 根据当前时间，以及最新开盘时间，股票编号获取该股票的流水信息
     * @param stockCode
     * @param startDate
     * @param endDate
     * @return
     */
    List<Stock4MinuteDomain> getStockInfoByDateAndCode(@Param("stockCode") String stockCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询指定日期范围内指定股票每天的交易数据
     * @param stockCode 股票编号
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<Stock4EveryDayDomain> getStockInfoByEveryDay(@Param("stockCode") String stockCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询指定的时间范围内，每天的收盘时间，作为getStockInfoByEveryDay方法的拆分中的条件
     * @param stockCode 股票编号
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<Date> getStockInfoEveryDay(@Param("stockCode") String stockCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据getStockInfoEveryDay查询到的每日交易收盘的时间进行查询每日股票的数据返回封装到Stock4EveryDayDomain绘制日k线
     * @param stockCode 股票编号
     * @param dateList 每日交易收盘时间
     * @return
     */
    List<Stock4EveryDayDomain> getStockInfoBySelectEverDay(@Param("stockCode") String stockCode, @Param("dateList") List<Date> dateList);

    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertAllData(@Param("list") List<StockRtInfo> list);


    /**
     * 根据最新时间和股票编码，查询股票最新的分时数据
     * @param code
     * @param endDate
     * @return
     */
    StockNewPriceDomain getStockNewPriceByCode(@Param("code") String code, @Param("endDate") Date endDate);

    /**
     * 根据股票编码，倒序显示最新的前10条交易数据
     * @param code
     * @return
     */
    List<StockNewTransactionDomain> getStockNewTransactionByCode(@Param("code") String code);

    /**
     * 查询股票每周的基础数据，包含每周的开盘时间点和收盘时间点
     * 但是不包含开盘价格和收盘价格
     * @param code 股票编码
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<Stock4EveryWeekDomain> getStockWeekKLineByCode(@Param("code") String code, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询指定股票在指定时间下的数据
     * @param code
     * @param times
     * @return
     *  map接口：
     *      openPrice:xxx
     *      closePrice:xxx
     */
    List<BigDecimal> getStockInfoByCodeAndTimes(@Param("code") String code, @Param("times") List<Date> times);
}
