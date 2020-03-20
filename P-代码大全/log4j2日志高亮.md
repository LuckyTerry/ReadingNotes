# log4j2日志高亮

## 项目中使用的

```xml
%magenta{[%d{yyyy-MM-dd HH:mm:ss:SSS}{GMT+8}]}[%-5p][%t][%C.%M:%L][%X{traceId}][%highlight{%m}{WARN=bright yellow}] %n
```

## 正则替换高亮转义

```java
/**
 * 目前抓到的带颜色的格式有：
 * ^[[35m内容^[[m
 * ^[[1;31m内容^[[m
 *
 * 可以使用如下正则去替换。
 *
 * @param args
 */
public static void main(String[] args) {
    String regex = "(\\^\\[\\[.*m)(.*)(\\^\\[\\[m)";
    boolean match = isMatch(regex, "^[[35m内容^[[m");
    System.out.println(match);
    boolean match1 = isMatch(regex, "^[[1;31m内容^[[m");
    System.out.println(match1);
    String replaceAll = getReplaceAll("^[[35m内容^[[m", regex, "$2");
    System.out.println(replaceAll);
    String replaceAll1 = getReplaceAll("^[[1;31m内容^[[m", regex, "$2");
    System.out.println(replaceAll1);
}

public static boolean isMatch(String regex, CharSequence input) {
    return input != null && input.length() > 0 && Pattern.matches(regex, input);
}

public static String getReplaceAll(String input, String regex, String replacement) {
    return input == null ? "" : Pattern.compile(regex).matcher(input).replaceAll(replacement);
}
```