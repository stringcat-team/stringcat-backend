package com.sp.common.utils;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public class RedisKeyUtils {

    @Getter
    public enum KeyPrefix {
        QUESTION_LIKE("q_like"),
        QUESTION_DIS_LIKE("q_dis_like"),
        QUESTION_LIKE_COUNT("q_like_count"),
        QUESTION_DIS_LIKE_COUNT("q_dis_like_count"),

        ANSWER_LIKE("a_like"),
        ANSWER_DIS_LIKE("a_dis_like"),
        ANSWER_LIKE_COUNT("a_like_count"),
        ANSWER_DIS_LIKE_COUNT("a_dis_like_count");

        private final String prefix;

        KeyPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    public static String generateKey(KeyPrefix keyPrefix, Long id) {
        return String.join(":", keyPrefix.getPrefix(), String.valueOf(id));
    }

    public static List<String> generateKeys(KeyPrefix keyPrefix, List<Long> ids) {
        return ids.stream()
            .map(id -> generateKey(keyPrefix, id))
            .collect(Collectors.toList());
    }
}
