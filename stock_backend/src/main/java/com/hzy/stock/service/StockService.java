package com.hzy.stock.service;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.pojo.domain.StockUpdownDomain;
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
}
