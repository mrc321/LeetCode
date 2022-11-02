package leetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从给定的API接口中请股票求数据
 */
public class Test {
    static Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    public static void main(String[] args) {
        Matcher matcher = DATE_PATTERN.matcher("            \"5. volume\": \"13100\"false");
        System.out.println(matcher.find());
        System.out.println(matcher.group(0));
    }
}

