/**
 * 
 */
package com.qdong.communal.library.util;

/**
 * 
 * 字符串操作工具类
 * @author zd
 *
 */
public class StringUtil {

	public static final String EMPTY = "";
	
	public static boolean isEmpty(String str){
		return str.length() == 0;
	}

    /**
     * @method name: substring
     * @des:    截取价格类型字符串后面的.00, 例如11.00截取后返回11
     * @param : [str]
     * @return type: java.lang.String
     * @date 创建时间：2015/11/23 10:36
     * @author hujie
     */
    public static String substring(String str) {
        String s = "";
        if(!android.text.TextUtils.isEmpty(str)) {
            if(str.indexOf(".") != -1) {
                s = str.substring(0, str.indexOf("."));
            }
        }
        return s;
    }
}
