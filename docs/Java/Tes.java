package docs.Java;

import java.util.*;

//词法分析，语法分析

/**
 * Tes
 */
public class Tes {
    private char[] charArray;
    private int cur = 0;
    private int size;
    // 留坑，String类型来存储token会导致无法区分到底是json特殊字符还是字符串字符，如true和"true"，需要增加属性来进一步判断
    private List<String> tokensList = new ArrayList<>();

    Tes(String str) {
        charArray = str.toCharArray();
        size = charArray.length;
    }

    public boolean hasMore() {
        if (cur == size) {
            return false;
        } else {
            return true;
        }
    }

    public char next() {
        if (!hasMore()) {
            return (char) -1;
        }
        return charArray[cur++];
    }

    public List<String> parse() throws Exception {
        while (hasMore()) {
            char c = next();
            switch (c) {
                case '{':
                    tokensList.add(String.valueOf(c));
                    break;
                case '}':
                    tokensList.add(String.valueOf(c));
                    break;
                case '[':
                    tokensList.add(String.valueOf(c));
                    break;
                case ']':
                    tokensList.add(String.valueOf(c));
                    break;
                case ',':
                    tokensList.add(String.valueOf(c));
                    break;
                case ':':
                    tokensList.add(String.valueOf(c));
                    break;
                case 't': {
                    if (next() == 'r' && next() == 'u' && next() == 'e') {
                        tokensList.add("true");
                        break;
                    } else {
                        throw new Exception("json格式错误");
                    }
                }
                case 'f': {
                    if (next() == 'a' && next() == 'l' && next() == 's' && next() == 'e') {
                        tokensList.add("false");
                        break;
                    } else {
                        throw new Exception("json格式错误");
                    }
                }
                case 'n': {
                    if (next() == 'u' && next() == 'l' && next() == 'l') {
                        tokensList.add("null");
                        break;
                    } else {
                        throw new Exception("json格式错误");
                    }
                }
                case '"':
                    StringBuilder strBuilder = new StringBuilder();
                    char y;
                    while (hasMore()) {
                        y = next();
                        if (y == '\\') {  // 处理转义字符
                            if (hasMore()) {
                                strBuilder.append(y);
                                strBuilder.append(next());
                            }
                        } else if (y == '"') {
                            tokensList.add(strBuilder.toString());
                            break;
                        } else {
                            strBuilder.append(y);
                        }
                    }
                    break;
                default:
                    if (Character.isDigit(c) || c == '-') {
                        StringBuilder numBuilder = new StringBuilder();
                        numBuilder.append(c);
                        while (hasMore()) {
                            char nextChar = charArray[cur];
                            if (Character.isDigit(nextChar) || nextChar == '.' || nextChar == 'e' || nextChar == 'E' || (nextChar == '-' && (charArray[cur - 1] == 'e' || charArray[cur - 1] == 'E'))) {
                                numBuilder.append(nextChar);
                                cur++;
                            } else {
                                break;
                            }
                        }
                        tokensList.add(numBuilder.toString());
                    } else if (!Character.isWhitespace(c)) {
                        throw new Exception("不支持的字符: " + c);
                    }
                    break;

            }
        }
        return tokensList;

    }

    public static void main(String[] args) throws Exception {
        Tes tes = new Tes("{\"name\":true,\"sex\":\"男\"}");
        List<String> list = tes.parse();
        System.out.println(list);
    }

}

class JSONParser {
    private String input;
    private int index;

    public JSONParser(String input) {
        this.input = input;
        this.index = 0;
    }

    public void parse() throws JSONException {
        Object result = parseValue();
        System.out.println(result);
    }

    private Object parseValue() throws JSONException {
        char currentChar = nextNonWhitespace();
        if (currentChar == '{') {
            return parseObject();
        } else if (currentChar == '[') {
            return parseArray();
        } else if (currentChar == '"') {
            return parseString();
        } else if (Character.isDigit(currentChar) || currentChar == '-') {
            return parseNumber();
        } else if (match("true")) {
            return true;
        } else if (match("false")) {
            return false;
        } else if (match("null")) {
            return null;
        }
        throw new JSONException("Invalid JSON"); // 处理错误或抛出异常，表示无效的JSON
    }

    private Map<String, Object> parseObject() throws JSONException {
        Map<String, Object> obj = new HashMap<>();
        while (true) {
            char nextChar = nextNonWhitespace();
            if (nextChar == '"') {
                String key = parseString(); // 解析键
                expect(':'); // 期望一个冒号
                Object value = parseValue(); // 解析值
                obj.put(key, value);
                char next = nextNonWhitespace();
                if (next == ',') {
                    // 继续解析下一个键值对
                    continue;
                } else if (next == '}') {
                    // 已经解析完所有的键值对，退出循环
                    break;
                } else {
                    throw new JSONException("Invalid JSON"); // 处理错误或抛出异常，表示无效的JSON
                }
            } else if (nextChar == '}') {
                break;  // JSON对象为空，直接退出循环
            } else {
                throw new JSONException("Invalid JSON: Unexpected character while parsing object");
            }
        }
        return obj;
    }

    private List<Object> parseArray() throws JSONException {
        List<Object> arr = new ArrayList<>();
        while (true) {
            arr.add(parseValue());
            char next = nextNonWhitespace();
            if (next == ',') {
                continue;
            } else if (next == ']') {
                break;
            } else {
                throw new JSONException("Invalid JSON"); // 处理错误或抛出异常，表示无效的JSON
            }
        }
        return arr;
    }

    private String parseString() throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (c == '"') {
                break;
            } else if (c == '\\') {
                // 处理转义字符（如果需要）
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Number parseNumber() throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (index < input.length() && (Character.isDigit(input.charAt(index)) || input.charAt(index) == '.')) {
            sb.append(input.charAt(index++));
        }
        try {
            return Double.parseDouble(sb.toString()); // 为简单起见，解析为double类型
        } catch (NumberFormatException e) {
            throw new JSONException("Invalid number format");
        }
    }

    private char next() {
        if (index < input.length()) {
            return input.charAt(index++);
        }
        return 0; // 输入结束
    }

    private char nextNonWhitespace() {
        while (index < input.length()) {
            char c = input.charAt(index++);
            if (!Character.isWhitespace(c)) {
                return c;
            }
        }
        return 0; // 输入结束
    }

    private void expect(char expected) throws JSONException {
        char actual = nextNonWhitespace();
        if (actual != expected) {
            throw new JSONException("Unexpected character: " + actual);
        }
    }

    private boolean match(String expected) {
        int start = index;
        for (int i = 0; i < expected.length(); i++) {
            if (index + i >= input.length() || input.charAt(index + i) != expected.charAt(i)) {
                index = start; // 重置索引
                return false;
            }
        }
        index += expected.length();
        return true;
    }

    public static void main(String[] args) {
        String jsonString = "{\"name\":\"John\",\"age\":30,\"car\":\"John\"}";
        JSONParser parser = new JSONParser(jsonString);
        try {
            parser.parse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

class JSONException extends Exception {
    public JSONException(String message) {
        super(message);
    }
}

