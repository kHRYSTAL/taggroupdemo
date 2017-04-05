package me.khrystal.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */

public class StringUtil {

    public static final String LABEL_REGEX = "[^+\\u4E00-\\u9FA5\\w\\s%'·.-]+";

    /**
     * 检查标签格式是否正确
     * (中英文数字空格.+-%)
     * @param labelText
     * @return true 正确 false 不正确
     */
    public static boolean checkLabelFormat(String labelText) {
        Pattern pattern = Pattern.compile(LABEL_REGEX);
        Matcher m = pattern.matcher(labelText);
        return !m.find();
    }
}
