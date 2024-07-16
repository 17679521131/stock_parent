package com.hzy.stock.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author daocaoaren
 * @date 2024/7/16 16:01
 * @description :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadRowHeight(value = 35) // 表头行高
@ContentRowHeight(value = 25) // 内容行高
@ColumnWidth(value = 50) // 列宽
public class User implements Serializable {
    @ExcelProperty(value = {"用户基本信息","用户名"},index = 0)
    private String userName;
    @ExcelProperty(value = {"用户基本信息","年龄"},index = 1)
    private Integer age;
    @ExcelProperty(value = {"用户基本信息","地址"},index = 3)
    private String address;
    @ExcelProperty(value = {"用户基本信息","生日"},index = 2)
    @DateTimeFormat("yyyy-MM-dd")//这个是easyExcel里面的注解，不用SpringMVC里面的这个注解
    @ExcelIgnore//忽律指定表头信息，就是开启了之后不会显示在表格中显示这一行数据
    private Date birthday;
}
