/*
 * Copyright (C) 2016 yydcdut (yuyidong2015@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yydcdut.rxmarkdown.grammar.edit;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.edit.EditToken;
import com.yydcdut.rxmarkdown.span.MDOrderListSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The implementation of grammar for order list.
 * Grammar:
 * "1. "
 * <p>
 * Created by yuyidong on 16/7/8.
 */
class OrderListGrammar extends EditGrammarAdapter {

    /**
     * see com.yydcdut.rxmarkdown.grammar.android.OrderListGrammar
     * used UnOrderListGrammar
     */
    public static final String KEY_HEADER = " ";

    OrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
    }

    @NonNull
    @Override
    @SuppressLint("WrongConstant")
    public List<EditToken> format(@NonNull Editable editable) {
        List<EditToken> editTokenList = new ArrayList<>();
        Pattern p = Pattern.compile("^( *)(\\d+)\\. (.*?)$", Pattern.MULTILINE);
        StringBuilder content = new StringBuilder(editable);
        Matcher m = p.matcher(content);
        List<String> matchList = new ArrayList<>();//找到的
        while (m.find()) {
            matchList.add(m.group());
        }
        for (String match : matchList) {
            int index = findTrueIndex(match, content);
            int length = match.length();
            int nested = calculateNested(match);
            int number = calculateNumber(match, nested);
            editTokenList.add(new EditToken(new MDOrderListSpan(10, nested, number), index, index + length, Spannable.SPAN_INCLUSIVE_INCLUSIVE));
            content.replace(index, index + length, getPlaceHolder(match));
        }
        return editTokenList;
    }

    private int calculateNested(String text) {
        if (text.length() < 2) {
            return -1;
        }
        int nested = 0;
        while (true) {
            if ((nested + 1) * KEY_HEADER.length() > text.length()) {
                break;
            }
            String sub = text.substring(nested * KEY_HEADER.length(), (nested + 1) * KEY_HEADER.length());
            if (KEY_HEADER.equals(sub)) {//还是" "
                nested++;
            } else {
                return nested;
            }
        }
        return nested;
    }

    /**
     * calculate the key number
     *
     * @param text   the content
     * @param nested the nested number
     * @return the key number
     */
    private int calculateNumber(@NonNull String text, int nested) {
        if (text.length() < 3) {
            return -1;
        }
        int number = -1;
        String s = text.substring(nested * KEY_HEADER.length(), text.length());
        if (TextUtils.isDigitsOnly(s.substring(0, 1))) {
            number = Integer.parseInt(s.substring(0, 1));
            for (int i = 1; i < s.length(); i++) {
                if (TextUtils.isDigitsOnly(s.substring(i, i + 1))) {
                    number = number * 10 + Integer.parseInt(s.substring(i, i + 1));
                    continue;
                } else {
                    return number;
                }
            }
        } else {
            return number;
        }
        return number;
    }

    /**
     * find true index position
     *
     * @param match   String,match text
     * @param content StringBuilder, the total text
     * @return the index position
     */
    private static int findTrueIndex(String match, StringBuilder content) {
        int index = content.indexOf(match);
        int length = match.length();
        if (index + length >= content.length()) {
            return index;
        }
        char c = content.charAt(index + length);
        if (c == '\n') {
            return index;
        }
        content.replace(index, index + length, getPlaceHolder(match));
        return findTrueIndex(match, content);
    }
}
