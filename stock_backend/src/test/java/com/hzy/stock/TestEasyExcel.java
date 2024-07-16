package com.hzy.stock;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hzy.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/16 16:00
 * @description :
 */
public class TestEasyExcel {

    public List<User> init(){
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海"+i);
            user.setUserName("张三"+i);
            user.setBirthday(new Date());
            user.setAge(10+i);
            users.add(user);
        }
        return users;
    }

    /**
     * 测试excel导出功能
     */
    @Test
    public void testExcelIn(){
        List<User> users = this.init();
        //导出数据
        EasyExcel.write("/Users/daocaoaren/data/test.xls",User.class).sheet("用户信息").doWrite(users);
    }

    /**
     * excel数据格式必须与实体类定义一致，否则数据读取不到
     */
    @Test
    public void readExcel(){
        ArrayList<User> users = new ArrayList<>();
        //读取数据
        EasyExcel.read("/Users/daocaoaren/data/test.xls", User.class, new AnalysisEventListener<User>() {
            /**
             * 读取每一行excel内容，并封装（读取一行，就回调一次该方法）
             * @param o
             * @param analysisContext
             */
            @Override
            public void invoke(User o, AnalysisContext analysisContext) {
                System.out.println(o);
                users.add(o);
            }

            /**
             * 所有行读取完执行这个方法，也就是可以执行一个通知
             * @param analysisContext
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("完成。。。。");
            }
        }).sheet("用户信息").doRead();
        System.out.println(users);
    }
}
