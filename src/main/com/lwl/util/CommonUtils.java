package main.com.lwl.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Stack;

/**
 * @author lwl
 * @date 2018/7/6 16:07
 * @description
 */
public class CommonUtils {
    private CommonUtils(){}
    public static Date getDateSpecifyDaysFromToday(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryLocalDateTime = now.plus(days, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = expiryLocalDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将金额转换为前端可以直接展示的字符串，超过10000元保留两位有效数字，小于10000元保留一位有效数字，每隔三位用英文逗号隔开
     * @author lwl
     * @param amount 交易额，单位为分
     * @return 交易额字符串，单位为元
     */
    public static String toDisplayString(Long amount){
        if (amount == null){
            return "";
        }
        Long amountYuan = amount / 100;
        char[] amountCharArray = amountYuan.toString().toCharArray();
        int length = amountCharArray.length;
        Stack<String> stack = new Stack<>();

        //从个位开始循环
        for (int i = length - 1; i >= 0; i--){
            //获取相对于个位的索引
            int indexRevert = length - 1 - i;
            if (indexRevert != 0 && indexRevert % 3 == 0){
                stack.push(",");
            }
            //小于5保留一位
            if (length <= 5){
                if (i > 0){
                    stack.push("0");
                    continue;
                }
                stack.push(String.valueOf(amountCharArray[i]));
                continue;
            }
            //大于5保留两位
            if (i > 1){
                stack.push("0");
                continue;
            }
            stack.push(String.valueOf(amountCharArray[i]));
        }

        StringBuilder stringBuilder = new StringBuilder();
        while (!stack.isEmpty()){
            stringBuilder.append(stack.pop());
        }
        return stringBuilder.toString();
    }
}
