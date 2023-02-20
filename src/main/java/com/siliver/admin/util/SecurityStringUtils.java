package com.siliver.admin.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 字符串安全验证
 */
public class SecurityStringUtils {

    private static final String key = "and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";

    private static final Set<String> notAllowedKeyWords = new HashSet<>(0);

    private static final String replacedString = "INVALID";

    static {
        String[] keyStr = key.split("\\|");
        notAllowedKeyWords.addAll(Arrays.asList(keyStr));
    }

    /**
     * 防xss攻击
     *
     * @param valueBefore 需要格式化的数据
     * @return 格式化后的数据
     */
    public static String cleanXSS(String valueBefore) {
        String valueAfter = valueBefore.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        valueAfter = valueAfter.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        valueAfter = valueAfter.replaceAll("'", "&#39;");
        valueAfter = valueAfter.replaceAll("eval\\((.*)\\)", "");
        valueAfter = valueAfter.replaceAll("[\"'] *javascript:(.*)[\"']", "\"\"");
        valueAfter = valueAfter.replaceAll("script", "");
        return valueAfter;
    }

    /**
     * 防sql注入
     * todo 目前会影响原数据
     *
     * @param valueBefore 需要格式化的数据
     * @return 格式化之后的数据
     */
    public static String cleanSqlKeyWords(String valueBefore) {
        String valueAfter = valueBefore;
        for (String keyword : notAllowedKeyWords) {
            if (valueAfter.length() > keyword.length() + 4
                    && (valueAfter.contains(" " + keyword) || valueAfter.contains(keyword + " ") || valueAfter.contains(" " + keyword + " "))) {
                valueAfter = StringUtils.replace(valueAfter, keyword, replacedString);
            }
        }
        return valueAfter;
    }

    /**
     * script异常数据过滤
     *
     * @param valueBefore 过滤前
     * @return 过滤后
     */
    public static String stripXSS(String valueBefore) {
        String valueAfter = null;
        if (StringUtils.hasText(valueBefore)) {
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            valueAfter = scriptPattern.matcher(valueBefore).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("<script(.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("expression\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("onload(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("alert\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("window.location(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("unescape\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("execscript\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("msgbox\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("confirm\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");

            scriptPattern = Pattern.compile("prompt\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            valueAfter = scriptPattern.matcher(valueAfter).replaceAll("");
        }
        return valueAfter;
    }

}
