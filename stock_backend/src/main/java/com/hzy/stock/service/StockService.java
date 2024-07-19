package com.hzy.stock.service;

import com.hzy.stock.pojo.domain.*;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:52
 * @description :
 */

public interface StockService {
    /**
     * 获取国内最新大盘的数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerMarketInfo();

    /**
     * 获取股票板块数据,获取上海深圳板块最新数据，以交易总金额降序查询，取前十条数据
     * @return
     */
    R<List<StockBlockDomain>> getStockBlockInfoLimit();

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询,展示出股票涨幅最大的数据
     * @param page
     * @param pageSize
     * @return
     */
    R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize);

    /**
     * 设计股票涨幅榜模块，需求是查询涨幅榜最大的前4条数据展示在前端
     * @return
     */
    R<List<StockUpdownDomain>> getStockIncreaseMax();

    /**
     * 统计股票最新交易日内每分钟的跌停的股票数量
     * @return
     */
    R<Map<String, List>> getStockUpdownCount();

    /**
     * 导出指定页码最新股票数据到excel
     * @param response  响应对象
     * @param page  当前页
     * @param pageSize 每页大小
     * @return
     */
    void exportStockInFoByPage(HttpServletResponse response, Integer page, Integer pageSize);

    /**
     * 统计A股大盘T日和T-1日成交量对比功能（成交量为沪深两市成交量之和），实现成交量功能
     * @return
     */
    R<Map<String, List>> getStockTradeAmt();

    /**
     * 统计当前时间下各个股票的涨幅区间的数量，完善各股涨跌图，涨幅区间分别为<-7% -7%~-5% -5%~-3% -3%~0% 0%~3% 3%~5% 5%~7% 7%>
     * @return
     */
    R<Map> getStockIncreaseRangeInfo();

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param code 股票编码
     * @return
     */
    R<List<Stock4EveryDayDomain>> stockScreenDKLine(String code);

    /**
     * 获取最新外盘数据
     * @return
     */
    R<List<OuterMarketDomain>> getOutMaketInfo();

    /**
     * 股票搜索功能，模糊查询返回股票编码名称和编码
     * @param searchStr 模糊输入的股票半编码
     * @return
     */
    R<List<Map>> getStockNameAndCode(String searchStr);

    /**
     * 获取股票业务信息
     * @param code 股票编码
     * @return
     */
    R<StockBusinessDomain> getStockBusinessInfo(String code);

    /**
     * 获取个股票最分时新行情数据
     * @param code 股票编码
     * @return
     */
    R<StockNewPriceDomain> getStockNewPriceInfo(String code);

    /**
     * 获取股票最新交易量数据
     * @param code 股票编码
     * @return
     */
    R<List<StockNewTransactionDomain>> getStockNewTransactionInfo(String code);
}
