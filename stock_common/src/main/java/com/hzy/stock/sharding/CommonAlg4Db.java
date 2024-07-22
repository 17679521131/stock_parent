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
 * @description : 定义公共的分库算法类：覆盖个股，大盘，板块相关表
 */
public class CommonAlg4Db implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    /**
     * 分库策略：按年分库
     * 精准查询时候走该方法，cur_time条件必须是等号或者in
     * eg: select * from stock_market_info where cur_time = ..... amd code=.....
     * @param dsNames ds-2021,ds-2022,ds-2023,ds-2024.....
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> preciseShardingValue) {
        //获取逻辑表
        String logicTableName = preciseShardingValue.getLogicTableName();
        //获取分片键 cut_time
        String columnName = preciseShardingValue.getColumnName();
        //获取等值查询的条件值
        Date curTime = preciseShardingValue.getValue();
        //获取条件值对应的年份，然后从collection集合中过滤出以该年份结尾的数据源即可
        String year = new DateTime(curTime).getYear()+"";
        Optional<String> result = dsNames.stream().filter(dsName -> dsName.endsWith(year)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    /**
     * 范围查询：eg: select * from stock_market_info where cur_time begeween ..... and..... and code=.....
     * @param dsNames
     * @param rangeShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> rangeShardingValue) {
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
            int startYear = new DateTime(startTime).getYear();
            //过滤出数据源中年份大于等于startYear的数据源
            //ds-2021,ds-2022,ds-2023,ds-2024
            dsNames = dsNames.stream().filter(dsname ->Integer.parseInt(dsname.substring(dsname.lastIndexOf("-")+1))>=startYear).collect(Collectors.toList());
        }
        //判断上限
        if (valueRange.hasUpperBound()) {
            //获取起始值
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endYear = new DateTime(endTime).getYear();
            //过滤出数据源中年份大于等于startYear的数据源
            //ds-2021,ds-2022,ds-2023,ds-2024
            dsNames = dsNames.stream().filter(dsname ->Integer.parseInt(dsname.substring(dsname.lastIndexOf("-")+1))<=endYear).collect(Collectors.toList());
        }
        return dsNames;
    }
}
