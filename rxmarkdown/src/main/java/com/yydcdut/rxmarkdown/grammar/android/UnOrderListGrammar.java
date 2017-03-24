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
package com.yydcdut.rxmarkdown.grammar.android;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.span.MDUnOrderListSpan;

import java.util.ArrayList;

/**
 * The implementation of grammar for unorder list.
 * Grammar:
 * "* "
 * <p>
 * "+ "
 * <p>
 * "- "
 * <p>
 * Created by yuyidong on 16/5/21.
 */
class UnOrderListGrammar extends GrammarAdapter {

    protected static final String KEY_0_UNORDER_LIST = "* ";
    protected static final String KEY_0_UNORDER_LIST_CHAR = "*";
    protected static final String KEY_1_UNORDER_LIST = "+ ";
    protected static final String KEY_1_UNORDER_LIST_CHAR = "+";
    protected static final String KEY_2_UNORDER_LIST = "- ";
    protected static final String KEY_2_UNORDER_LIST_CHAR = "-";

    private static final String KEY_IGNORE_0 = TodoGrammar.KEY_0_TODO;
    private static final String KEY_IGNORE_1 = TodoGrammar.KEY_1_TODO;
    private static final String KEY_IGNORE_2 = TodoDoneGrammar.KEY_0_TODO_DONE;
    private static final String KEY_IGNORE_3 = TodoDoneGrammar.KEY_1_TODO_DONE;
    private static final String KEY_IGNORE_4 = TodoDoneGrammar.KEY_2_TODO_DONE;
    private static final String KEY_IGNORE_5 = TodoDoneGrammar.KEY_3_TODO_DONE;

    private static final int START_POSITION = 2;

    private int mColor;

    public UnOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        mColor = rxMDConfiguration.getUnOrderListColor();
    }

    @Override
    public boolean isMatch(@NonNull CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        String text = charSequence.toString();
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            boolean b = lines[i].startsWith(KEY_0_UNORDER_LIST) ||
                    lines[i].startsWith(KEY_1_UNORDER_LIST) ||
                    (lines[i].startsWith(KEY_2_UNORDER_LIST));
            if (b) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public CharSequence format(@NonNull CharSequence charSequence) {
        if (!(charSequence instanceof SpannableStringBuilder)) {
            return charSequence;
        }
        SpannableStringBuilder ssb = (SpannableStringBuilder) charSequence;
        String text = charSequence.toString();
        int currentLineIndex = 0;
        String[] lines = text.split("\n");
        ArrayList<NestedUnOrderListBean> list = new ArrayList<>(lines.length);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(KEY_IGNORE_0) || lines[i].startsWith(KEY_IGNORE_1) || lines[i].startsWith(KEY_IGNORE_2) ||
                    lines[i].startsWith(KEY_IGNORE_3) || lines[i].startsWith(KEY_IGNORE_4) || lines[i].startsWith(KEY_IGNORE_5)) {
                list.add(new NestedUnOrderListBean(currentLineIndex, false, lines[i], -1));
                currentLineIndex += (lines[i] + "\n").length();
                continue;
            }
            if (existCodeSpan(ssb, currentLineIndex, currentLineIndex + (lines[i]).length())) {
                list.add(new NestedUnOrderListBean(currentLineIndex, false, lines[i], -1));
                currentLineIndex += (lines[i] + "\n").length();
                continue;
            }
            if (lines[i].startsWith(KEY_0_UNORDER_LIST) || lines[i].startsWith(KEY_1_UNORDER_LIST) || lines[i].startsWith(KEY_2_UNORDER_LIST)) {
                list.add(new NestedUnOrderListBean(currentLineIndex, true, lines[i], 0));
                currentLineIndex += (lines[i] + "\n").length();
                continue;
            }
            if (!lines[i].startsWith(OrderListGrammar.KEY_HEADER)) {
                list.add(new NestedUnOrderListBean(currentLineIndex, false, lines[i], -1));
                currentLineIndex += (lines[i] + "\n").length();
                continue;
            }
            int nested = calculateNested(lines[i]);
            if (nested > 0) {
                //判断上文
                if (i - 1 < 0 || i - 1 >= list.size()) {
                    currentLineIndex += (lines[i] + "\n").length();
                    continue;
                }
                NestedUnOrderListBean previousBean = list.get(i - 1);
                if (previousBean != null && previousBean.isRegular &&
                        (nested <= previousBean.nested + 1)) {
                    list.add(new NestedUnOrderListBean(currentLineIndex, true, lines[i], nested));
                } else {
                    list.add(new NestedUnOrderListBean(currentLineIndex, false, lines[i], -1));
                }
            }
            currentLineIndex += (lines[i] + "\n").length();
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            NestedUnOrderListBean bean = list.get(i);
            if (bean != null && bean.isRegular) {
                setSSB(bean.nested, bean.start, bean.line, ssb);
            }
        }
        return ssb;
    }

    /**
     * calculate nested
     *
     * @param text the content
     * @return nested number of content
     */
    private int calculateNested(@NonNull String text) {//有 "  " 才算嵌套的开始
        int nested = 0;
        while (true) {
            if ((nested + 1) * OrderListGrammar.KEY_HEADER.length() > text.length()) {
                break;
            }
            String sub = text.substring(nested * OrderListGrammar.KEY_HEADER.length(), (nested + 1) * OrderListGrammar.KEY_HEADER.length());
            if (OrderListGrammar.KEY_HEADER.equals(sub)) {//还是"  "
                nested++;
            } else if (KEY_0_UNORDER_LIST_CHAR.equals(sub) ||
                    KEY_1_UNORDER_LIST_CHAR.equals(sub) ||
                    KEY_2_UNORDER_LIST_CHAR.equals(sub)) {
                return nested;
            } else {
                return -1;
            }
        }
        return nested;
    }

    /**
     * set bullet
     *
     * @param nested the nested number
     * @param start  start position
     * @param line   the content
     * @param ssb    the content
     */
    private void setSSB(int nested, int start, @NonNull String line, @NonNull SpannableStringBuilder ssb) {
        ssb.delete(start, start + nested * OrderListGrammar.KEY_HEADER.length() + START_POSITION);
        ssb.setSpan(new MDUnOrderListSpan(10, mColor, nested),
                start,
                start + line.length() - (nested * OrderListGrammar.KEY_HEADER.length() + START_POSITION),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static class NestedUnOrderListBean {
        final int start;
        final boolean isRegular;
        final String line;//without "\n"
        final int nested;

        public NestedUnOrderListBean(int start, boolean isRegular, @NonNull String line, int nested) {
            this.start = start;
            this.isRegular = isRegular;
            this.line = line;
            this.nested = nested;
        }
    }
}
