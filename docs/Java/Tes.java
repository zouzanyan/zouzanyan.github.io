package docs.Java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                case '"': {
                    String str = "";
                    while (hasMore()) {
                        char y = next();
                        if (y == '"') {
                            tokensList.add(str);
                            break;
                        }
                        str = str + String.valueOf(y);

                    }
                }
                case '-':
                break;
                default:
                break;

            }
        }
        return tokensList;

    }

    public static void main(String[] args) throws Exception {
        Tes tes = new Tes("{\"name\":true,\"sex:\"男\"}");
        List<String> list = tes.parse();
        System.out.println(list);
    }

}

/**
 * InnerTes
 */
class InnerTes {

    public static void main(String[] args) {
        int a = 6;
        switch (a) {
            case 5:
                System.out.println(5325);
                break;
        
            default:
                
        }
    }
}