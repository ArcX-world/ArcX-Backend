package avatar.util.system;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String join(Iterable<?> iterable, String delimiter) {
        if (iterable == null)
            return "";
        Iterator<?> it = iterable.iterator();
        if (it.hasNext()) {
            StringBuilder sb = new StringBuilder().append(it.next());
            while (it.hasNext())
                sb.append(delimiter).append(it.next());
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String join(AbstractCollection<String> s, String delimiter) {
        if (s.isEmpty())
            return "";
        Iterator<String> iter = s.iterator();
        StringBuffer buffer = new StringBuffer(iter.next());
        while (iter.hasNext())
            buffer.append(delimiter).append(iter.next());
        return buffer.toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0 || s.trim().equals("null");
    }

    public static String prepareOnlineUserInSql(Collection<String> checkUserList,
                                                String sql) {
        List<String> temp = new ArrayList<String>();
        for (String s : checkUserList) {
            if (s.indexOf("'") < 0) {
                temp.add(s);
            }
        }

        if (temp.size() == 0) {
            return null;
        } else {
            return String.format(sql, StringUtil.join(temp, "','"));
        }
    }

    public static String prepareInSqlClause(Collection<Integer> valueList,
                                            String sql) {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i : valueList) {
            temp.add(String.valueOf(i));
        }

        if (temp.size() == 0) {
            return null;
        } else {
            return String.format(sql, StringUtil.join(temp, ","));
        }
    }

    public static String intArrayToString(int[] array) {
        return intArrayToString(array, ",");
    }

    public static String intArrayToString(int[] array, String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int element : array) {
            sb.append(element).append(separator);
        }
        return sb.delete(sb.length() - separator.length(), sb.length()).toString();
    }

    public static int[] parseString2IntArr(String str, String separator) {
        if (isNullOrEmpty(str)) {
            return new int[0];
        }
        String[] strArr = str.split(separator);
        int[] result = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            result[i] = Integer.valueOf(strArr[i]);
        }
        return result;
    }
    
    public static List<Integer> parseString2IntList(String str, String separator) {
        if (isNullOrEmpty(str)) {
            return new ArrayList<Integer>(0);
        }
        String[] strArr = str.split(separator);
        List<Integer> result = new ArrayList<Integer>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            result.add(Integer.valueOf(strArr[i]));
        }
        return result;
    }

    public static List<String> parseString2StringList(String str, String separator) {
        if (isNullOrEmpty(str)) {
            return new ArrayList<String>(0);
        }
        String[] strArr = str.split(separator);
        List<String> result = new ArrayList<String>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            result.add(strArr[i]);
        }
        return result;
    }

    public static int parseBinaryString2Int(String binaryString) {
        if (!binaryString.matches("[01]+")) {
            throw new RuntimeException("wrong binaryString!!");
        }
        int i = binaryString.length() - 1, j = 0;
        int num = 0;
        while (i >= 0) {
            num += binaryString.charAt(i--) == '1' ? Math.pow(2, j) : 0;
            j++;
        }
        return num;
    }
    /**
     * 字符串是否符合匹配正则表达式
     * @param str   字符串
     * @param regex  正则表达式
     * @return true:符合  false:不符合
     */
    public static boolean matches(String str,String regex){
		Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(str); 
        return matcher.matches();
	}

    /**首字母大写*/
    public static String uppercaseStr(String str) {
    	//str = str.substring(0, 1).toUpperCase() + str.substring(1);
        char[] cs=str.toCharArray();
        if(cs[0] >= 97 ){ 
        	cs[0]-=32;
        	return String.valueOf(cs);
        }
        return str;
    }
    
    /**首字母转成小写*/
    public static String lowerCaseStr(String str){
    	char[] cs=str.toCharArray();
        if(cs[0] >= 65 &&cs[0] < 97 ){ 
        	cs[0]+=32;
        	return String.valueOf(cs);
        }
        return str;
    }

    /**
     * 获取数字
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        try {
            if (null == obj || "null".equals(obj) || "".equals(obj))
                return 0;
            else
                return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

}
