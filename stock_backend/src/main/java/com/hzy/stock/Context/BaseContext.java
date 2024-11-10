package com.hzy.stock.Context;

/**
 * @author daocaoaren
 * @date 2024/11/10 01:09
 * @description :
 */
public class BaseContext {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(String id) {
        threadLocal.set(id);
    }

    public static String getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }
}
