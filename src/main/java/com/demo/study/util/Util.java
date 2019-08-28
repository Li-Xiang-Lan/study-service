package com.demo.study.util;

/**
 * create cheng 2019/8/14
 */
public class Util {

    public static boolean isEmpty(String str){
        return str==null||str.equals("");
    }

    public static int calculateRowIndex(int pageNum,int pageSize){
        return (pageNum>0)?(pageNum-1)*pageSize:0;
    }
}
