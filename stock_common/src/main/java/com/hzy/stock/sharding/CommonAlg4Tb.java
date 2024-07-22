package com.hzy.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/20 18:15
 * @description : 定义个股流水表的分表算法类：个股流水表
 */
public class CommonAlg4Tb implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    /**
     * 分库策略：按月分表
     * 精准查询时候走该方法，cur_time条件必须是等号或者in
     * eg: select * from stock_market_info where cur_time = ..... amd code=.....
     * @param tbNames eg:2023->202301..202312
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> preciseShardingValue) {
        //获取逻辑表
        String logicTableName = preciseShardingValue.getLogicTableName();
        //获取分片键 cut_time
        String columnName = preciseShardingValue.getColumnName();
        //获取等值查询的条件值
        Date curTime = preciseShardingValue.getValue();
        //获取条件值对应的年月份，然后从collection集合中过滤出以该年份结尾的数据表即可
        String yearMonth = new DateTime(curTime).toString("yyyyMM");
        Optional<String> result = tbNames.stream().filter(dsName -> dsName.endsWith(yearMonth)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    /**
     * 范围查询：eg: select * from stock_market_info where cur_time begeween ..... and..... and code=.....
     * @param tbNames eg:2023->202301..202312
     * @param rangeShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> rangeShardingValue) {
        //获取逻辑表
        String logicTableName = rangeShardingValue.getLogicTableName();
        //获取分片键 cut_time
        String columnName = rangeShardingValue.getColumnName();
        //获取范围数据的封装
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        //判断下限
        if (valueRange.hasLowerBound()) {
            //获取起始值
            Date startTime = valueRange.lowerEndpoint();
            //获取条件所属年份
            int startYearMonth = Integer.parseInt(new DateTime(startTime).toString("yyyyMM"));
            //过滤出数据源中年份大于等于startYear的数据源
            //ds-2021,ds-2022,ds-2023,ds-2024
            tbNames = tbNames.stream().filter(tbname ->Integer.parseInt(tbname.substring(tbname.lastIndexOf("_")+1))>=startYearMonth).collect(Collectors.toList());
        }
        //判断上限
        if (valueRange.hasUpperBound()) {
            //获取起始值
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endYearMonth = Integer.parseInt(new DateTime(endTime).toString("yyyyMM"));
            //过滤出数据源中年份大于等于startYear的数据源
            //ds-2021,ds-2022,ds-2023,ds-2024
            tbNames = tbNames.stream().filter(tbname ->Integer.parseInt(tbname.substring(tbname.lastIndexOf("_")+1))<=endYearMonth).collect(Collectors.toList());
        }
        return tbNames;
    }
}
