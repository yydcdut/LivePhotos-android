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

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.edit.EditToken;
import com.yydcdut.rxmarkdown.span.MDUnOrderListSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yydcdut.rxmarkdown.grammar.edit.OrderListGrammar.KEY_HEADER;

/**
 * The implementation of grammar for unorder list.
 * Grammar:
 * "* "
 * <p>
 * "+ "
 * <p>
 * "- "
 * <p>
 * Created by yuyidong on 16/7/8.
 */
class UnOrderListGrammar extends EditGrammarAdapter {
    private static final String IGNORE_0 = "- [x]";
    private static final String IGNORE_1 = "- [X]";
    private static final String IGNORE_2 = "- [ ]";
    private static final String IGNORE_3 = "* [x]";
    private static final String IGNORE_4 = "* [x]";
    private static final String IGNORE_5 = "* [ ]";
    private static final String PLACE_HOLDER_IGNORE = "     ";

    private int mColor;

    UnOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
        mColor = rxMDConfiguration.getUnOrderListColor();
    }

    @NonNull
    @Override
    @SuppressLint("WrongConstant")
    public List<EditToken> format(@NonNull Editable editable) {
        List<EditToken> editTokenList = new ArrayList<>();
        List<String> matchList0 = new ArrayList<>();//找到的
        List<String> matchList1 = new ArrayList<>();//找到的
        List<String> matchList2 = new ArrayList<>();//找到的
        StringBuilder content = new StringBuilder(editable);
        replaceTodo(content);
        //+ sss
        Pattern p0 = Pattern.compile("^( *)(\\+ )(.*?)$", Pattern.MULTILINE);
        Matcher m0 = p0.matcher(content);
        while (m0.find()) {
            matchList0.add(m0.group());
        }
        //- sss
        Pattern p1 = Pattern.compile("^( *)(\\- )(.*?)$", Pattern.MULTILINE);
        Matcher m1 = p1.matcher(content);
        while (m1.find()) {
            matchList1.add(m1.group());
        }
        //* sss
        Pattern p2 = Pattern.compile("^( *)(\\* )(.*?)$", Pattern.MULTILINE);
        Matcher m2 = p2.matcher(content);
        while (m2.find()) {
            matchList2.add(m2.group());
        }
        replace(matchList0, MDUnOrderListSpan.TYPE_KEY_2, content, editTokenList);
        replace(matchList1, MDUnOrderListSpan.TYPE_KEY_1, content, editTokenList);
        replace(matchList2, MDUnOrderListSpan.TYPE_KEY_0, content, editTokenList);
        return editTokenList;
    }

    private void replace(List<String> matchList, int type, StringBuilder content, List<EditToken> editTokenList) {
        for (String match : matchList) {
            int index = content.indexOf(match);
            int length = match.length();
            int nested = calculateNested(match);
            editTokenList.add(new EditToken(new MDUnOrderListSpan(10, mColor, nested, type), index, index + length, Spannable.SPAN_INCLUSIVE_INCLUSIVE));
            content.replace(index, index + length, getPlaceHolder(match));
        }
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

    private void replaceTodo(StringBuilder stringBuilder) {
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_0);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_0.length(), PLACE_HOLDER_IGNORE);
        }
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_1);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_1.length(), PLACE_HOLDER_IGNORE);
        }
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_2);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_2.length(), PLACE_HOLDER_IGNORE);
        }
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_3);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_3.length(), PLACE_HOLDER_IGNORE);
        }
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_4);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_4.length(), PLACE_HOLDER_IGNORE);
        }
        while (true) {
            int index0 = stringBuilder.indexOf(IGNORE_5);
            if (index0 == -1) {
                break;
            }
            stringBuilder.replace(index0, index0 + IGNORE_5.length(), PLACE_HOLDER_IGNORE);
        }
    }
}
