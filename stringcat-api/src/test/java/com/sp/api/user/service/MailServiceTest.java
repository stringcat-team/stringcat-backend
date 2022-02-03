package com.sp.api.user.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MailServiceTest {

    private static final List<String> codeFactors = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    @Test
    void issueCodes() {
        StringBuilder sb = new StringBuilder();
        Collections.shuffle(codeFactors);

        for(int i=0; i<6; i++) {
            sb.append(codeFactors.get(i));
        }

        System.out.println(sb.toString());
    }

}