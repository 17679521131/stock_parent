package com.hzy.stock;

import com.hzy.stock.mapper.StockBlockRtInfoMapper;
import com.hzy.stock.mapper.StockBusinessMapper;
import com.hzy.stock.mapper.StockRtInfoMapper;
import com.hzy.stock.mapper.SysUserMapper;

import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.pojo.entity.StockBusiness;
import com.hzy.stock.pojo.entity.SysUser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author by itheima
 * @Date 
 * @Description
 */
@SpringBootTest
public class TestShardingJdbcDemo {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * @Description 测试默认数据源的配置
     */
    @Test
    public void testDefault(){
        SysUser user = sysUserMapper.selectByPrimaryKey(1237361915165020161l);
        System.out.println(user);
    }

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * @Description 测试广播表
     */
    @Test
    public void testBroadCast(){
//        StockBusiness pojo = StockBusiness.builder().stockCode("90000")
//                .stockName("900000")
//                .blockLabel("900000")
//                .blockName("900000")
//                .business("900000")
//                .updateTime(new Date())
//                .build();
//        stockBusinessMapper.insert(pojo);

        stockBusinessMapper.deleteByPrimaryKey("90000");
    }

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    /**
     * @Description 测试公共分库算法类
     */
    @Test
    public void testCommon4Db(){
        Date curDate= DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockBlockDomain> info = stockBlockRtInfoMapper.getStockBlockInfoLimit(curDate);
        //System.out.println(info);
    }

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    /**
     * @Description 测试范围匹配查询
     */
    @Test
    public void testStockRtInfo(){
        Date start= DateTime.parse("2022-07-13 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date end= DateTime.parse("2022-07-13 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> info = stockRtInfoMapper.getStockUpDownCount(start,end,0);
        System.out.println(info);
    }
//
//    /**
//     * @Description 测试精准查询数据
//     */
//    @Test
//    public void testPreci(){
//        Date start= DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        List<Map> info = stockRtInfoMapper.getStockUpdownSectionInfo(start);
//        System.out.println(info);
//    }


}
