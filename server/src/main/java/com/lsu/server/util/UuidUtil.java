package com.lsu.server.util;

import java.util.UUID;

/**
 * @author wsuo
 */
public class UuidUtil {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private static final Integer LENGTH = 8;


    /**
     * 获取短UUID
     *
     * @return
     */
    public static String getShortUuid() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UuidUtil.getUuid();
        for (int i = 0; i < LENGTH; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            // 对62取余
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    /**
     * 获得32位UUID
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getShortUuid());
    }
}
