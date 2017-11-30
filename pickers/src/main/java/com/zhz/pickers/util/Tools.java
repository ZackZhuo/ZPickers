package com.zhz.pickers.util;

/**
 * Created by Zack_zhuo on 2017/11/20.
 */

public class Tools {

    public static int string2int(String str){
        try{
            return Integer.parseInt(str);
        }catch (Exception e){
            return 0;
        }
    }
}
