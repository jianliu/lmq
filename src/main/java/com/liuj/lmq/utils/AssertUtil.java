package com.liuj.lmq.utils;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public class AssertUtil {

    public static void notNull(Object obj){
        if(obj == null){
            throw new IllegalArgumentException("object can not be null!");
        }
    }

    public static void notNull(Object obj, String message){
        if(obj == null){
            throw new IllegalArgumentException(message);
        }
    }

}
