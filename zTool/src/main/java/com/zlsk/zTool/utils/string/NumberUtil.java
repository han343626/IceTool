package com.zlsk.zTool.utils.string;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by IceWang on 2018/7/2.
 */

public class NumberUtil {

    public static int getRandom(int max,int min){
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }

    /**
     * 数字保留指定小数位
     * @param value 目标数
     * @param count 小数位
     * @return 保留指定小数的数字字符串
     */
    public static String decimalFormat(Number value, int count){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(count);
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    /**
     * 字符串转double
     * @param object 目标字符串
     * @param count 保留小数位
     * @return double
     */
    public static double strToDouble(Object object,int count){
        if(object == null){
            return 0;
        }

        try{
            double result = Double.parseDouble(String.valueOf(object));
            return Double.parseDouble(decimalFormat(result,count));
        }catch (Exception exc){
            return 0;
        }
    }

    /**
     * 返回较大值
     */
    public static double compareDouble(double var1,double var2){
        BigDecimal data1 = new BigDecimal(var1);
        BigDecimal data2 = new BigDecimal(var2);

        int compare = data1.compareTo(data2);

        return compare == 1 ? var2 : var1;
    }
}
