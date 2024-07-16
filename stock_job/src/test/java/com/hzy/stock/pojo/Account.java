package com.hzy.stock.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daocaoaren
 * @date 2024/7/17 20:38
 * @description :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private Integer id;

    private String userName;

    private String address;

}

