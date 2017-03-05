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
package com.yydcdut.rxmarkdown.edit;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMDEditText;
import com.yydcdut.rxmarkdown.span.MDOrderListSpan;
import com.yydcdut.rxmarkdown.span.MDUnOrderListSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RxMDEditText, order and unorder list controller.
 * When input "enter(\n)", RxMDEditText will check the line up the new line whether is order nor unorder list.
 * If it is, RxMDEditText will add order or unorder list grammar key automatically.
 * <p>
 * Created by yuyidong on 16/7/8.
 */
public class ListController extends AbsEditController {

    private RxMDEditText mRxMDEditText;
    private RxMDEditText.EditTextWatcher mTextWatcher;
    private RxMDConfiguration mRxMDConfiguration;

    private boolean mNeedFormat;

    /**
     * Constructor
     *
     * @param rxMDEditText RxMDEditText
     * @param textWatcher  TextWatcher
     */
    public ListController(RxMDEditText rxMDEditText, RxMDEditText.EditTextWatcher textWatcher) {
        mRxMDEditText = rxMDEditText;
        mTextWatcher = textWatcher;
    }

    @Override
    public void setRxMDConfiguration(@Nullable RxMDConfiguration rxMDConfiguration) {
        mRxMDConfiguration = rxMDConfiguration;
    }

    /**
     * invoke when beforeTextChanged
     *
     * @param s      CharSequence
     * @param start  start position
     * @param before before changed number
     * @param after  after changed number
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        if (mRxMDConfiguration == null || !(s instanceof Editable)) {
            return;
        }
        Editable editable = (Editable) s;
        if (checkLineDelete(editable, start, before, after)) {
            int beforeLinePosition = EditUtils.findBeforeNewLineChar(editable, start - 1) + 1;
            MDOrderListSpan mdBeginOrderListSpan = getOrderListSpan(editable, beforeLinePosition, true);
            MDOrderListSpan mdEndOrderListSpan = getOrderListSpan(editable, start + 1, true);//(start + 1),+1就是为了略过\n
            MDUnOrderListSpan mdBeginUnOrderListSpan = getUnOrderListSpan(editable, beforeLinePosition, true);
            MDUnOrderListSpan mdEndUnOrderListSpan = getUnOrderListSpan(editable, start + 1, true);//(start + 1),+1就是为了略过\n
            if (mdBeginOrderListSpan != null) {
                int spanStart = editable.getSpanStart(mdBeginOrderListSpan);
                int position = EditUtils.findNextNewLineCharCompat(editable, start + 1);//(start + 1),+1就是为了略过\n
                if (mdEndOrderListSpan != null) {
                    editable.removeSpan(mdEndOrderListSpan);
                }
                editable.removeSpan(mdBeginOrderListSpan);
                editable.setSpan(new MDOrderListSpan(10, mdBeginOrderListSpan.getNested(), mdBeginOrderListSpan.getNumber()),
                        spanStart, position, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (mdBeginUnOrderListSpan != null) {
                int spanStart = editable.getSpanStart(mdBeginUnOrderListSpan);
                int position = EditUtils.findNextNewLineCharCompat(editable, start + 1);//(start + 1),+1就是为了略过\n
                if (mdEndUnOrderListSpan != null) {
                    editable.removeSpan(mdEndUnOrderListSpan);
                }
                editable.removeSpan(mdBeginUnOrderListSpan);
                editable.setSpan(new MDUnOrderListSpan(10, mdBeginUnOrderListSpan.getColor(), mdBeginUnOrderListSpan.getNested()),
                        spanStart, position, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        } else {
            mNeedFormat = checkDeleteOrderListSpan(editable, start, before, after);
            mNeedFormat |= checkDeleteUnOrderListSpan(editable, start, before, after);
        }
    }

    /**
     * invoke when onTextChanged
     *
     * @param s      CharSequence
     * @param start  start position
     * @param before before changed number
     * @param after  after changed number
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {
        if (mRxMDConfiguration == null || !(s instanceof Editable)) {
            return;
        }
        Editable editable = (Editable) s;
        if (checkNewLineInput(editable, start, before, after)) {
            MDOrderListSpan mdOrderListSpan = getOrderListSpan(editable, start, false);
            MDUnOrderListSpan mdUnOrderListSpan = getUnOrderListSpan(editable, start, false);
            if (mdOrderListSpan != null) {
                //// TODO: 2017/1/9 判断上一行是否有内容
                int startOfSpan = editable.getSpanStart(mdOrderListSpan);
                int endOfSpan = editable.getSpanEnd(mdOrderListSpan);
                int nested = mdOrderListSpan.getNested();
                if (endOfSpan - startOfSpan > ". ".length() + String.valueOf(mdOrderListSpan.getNumber()).length()) {
                    updateOrderListSpanBeforeNewLine(editable, start, mdOrderListSpan);
                    insertOrderList(editable, mdOrderListSpan, start);
                } else {

                }
            } else if (mdUnOrderListSpan != null) {
                int spanStart = editable.getSpanStart(mdUnOrderListSpan);
                int spanEnd = editable.getSpanEnd(mdUnOrderListSpan);
                int nested = mdUnOrderListSpan.getNested();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = spanStart; i < spanEnd; i++) {
                    stringBuilder.append(editable.charAt(i));
                }
                if (spanEnd - spanStart > 3 + nested) {
                    updateUnOrderListSpanBeforeNewLine(editable, start, mdUnOrderListSpan, false);
                    insertUnOrderList(editable, mdUnOrderListSpan, start);
                } else {
                    deleteUnOrderListByEnter(editable, mdUnOrderListSpan, start, before, after, nested, spanStart, spanEnd);
                }
            }
        } else if (checkLineHeaderPosition(editable, start, before, after)) {
            updateLineHeaderList(editable, start, before, after);
        } else if (isBeginningOfListSpan(editable, start, before, after) || mNeedFormat) {
            updateListSpanBeginning(editable, start, before, after);
        } else if (isSatisfiedOrderListFormat(editable, start)) {
            formatOrderList(editable, start);
        }
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {

    }

    /**
     * the text that automatic filling after enter "\n"
     *
     * @param nested      the nested number
     * @param isOrderList is order list or unorder list
     * @param number      if order list, the list number
     * @return the text
     */
    private static String getNestedString(int nested, boolean isOrderList, int number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nested; i++) {
            sb.append(" ");
        }
        if (isOrderList) {
            sb.append(String.valueOf(number + 1));
            sb.append(". ");
        } else {
            sb.append("* ");
        }
        return sb.toString();
    }

    /**
     * when entering '\n', if this line is empty unorder list, delete this line or decrease the nested number
     *
     * @param editable          the editable
     * @param mdUnOrderListSpan the unorder list span
     * @param start             the start position
     * @param before            delete text's number
     * @param after             add text's number
     * @param nested            the nested number
     * @param spanStart         the beginning of span
     * @param spanEnd           the end of span
     */
    private void deleteUnOrderListByEnter(Editable editable, MDUnOrderListSpan mdUnOrderListSpan,
                                          int start, int before, int after,
                                          int nested, int spanStart, int spanEnd) {
        if (nested == 0) {
            editable.removeSpan(mdUnOrderListSpan);
            mRxMDEditText.removeTextChangedListener(mTextWatcher);
            mTextWatcher.doBeforeTextChanged(editable, start, (spanEnd - spanStart), 0);
            editable.delete(spanStart, spanEnd);
            mTextWatcher.doOnTextChanged(editable, start, (spanEnd - spanStart), 0);
            mTextWatcher.doAfterTextChanged(editable);
            mRxMDEditText.addTextChangedListener(mTextWatcher);
        } else {
            updateUnOrderListSpanBeforeNewLine(editable, start, mdUnOrderListSpan, true);
            mRxMDEditText.removeTextChangedListener(mTextWatcher);
            mTextWatcher.doBeforeTextChanged(editable, start + after - 1, 1, 0);
            editable.delete(start + after - 1, start + after);//delete '\n'
            mTextWatcher.doOnTextChanged(editable, start + after - 1, 1, 0);
            mTextWatcher.doAfterTextChanged(editable);
            mTextWatcher.doBeforeTextChanged(editable, spanStart, 1, 0);
            editable.delete(spanStart, spanStart + 1);//decrease nested
            mTextWatcher.doOnTextChanged(editable, spanStart, 1, 0);
            mTextWatcher.doAfterTextChanged(editable);
            mRxMDEditText.addTextChangedListener(mTextWatcher);
        }
    }

    /**
     * 1. aaa --> 1. aaa\n2.
     *
     * @param editable        the editable
     * @param mdOrderListSpan the order list span
     * @param start           start position
     */
    private void insertOrderList(Editable editable, MDOrderListSpan mdOrderListSpan, int start) {
        mRxMDEditText.removeTextChangedListener(mTextWatcher);
        String appendString = getNestedString(mdOrderListSpan.getNested(), true, mdOrderListSpan.getNumber());
        mTextWatcher.doBeforeTextChanged(editable, start + 1, 0, appendString.length());
        editable.insert(start + 1, appendString);
        int position = EditUtils.findNextNewLineCharCompat(editable, start + appendString.length());
        editable.setSpan(new MDOrderListSpan(10, mdOrderListSpan.getNested(), mdOrderListSpan.getNumber() + 1),
                start + 1,
                position == -1 ? start + 1 + appendString.length() : position,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextWatcher.doOnTextChanged(editable, start + 1, 0, appendString.length());
        mTextWatcher.doAfterTextChanged(editable);
        mRxMDEditText.addTextChangedListener(mTextWatcher);
    }

    /**
     * - aaa --> - aaa\n-
     *
     * @param editable          the editable
     * @param mdUnOrderListSpan the unorder list span
     * @param start             start position
     */
    private void insertUnOrderList(Editable editable, MDUnOrderListSpan mdUnOrderListSpan, int start) {
        mRxMDEditText.removeTextChangedListener(mTextWatcher);
        String appendString = getNestedString(mdUnOrderListSpan.getNested(), false, -1);
        mTextWatcher.doBeforeTextChanged(editable, start + 1, 0, appendString.length());
        editable.insert(start + 1, appendString);
        int position = EditUtils.findNextNewLineCharCompat(editable, start + appendString.length());
        editable.setSpan(new MDUnOrderListSpan(10, mdUnOrderListSpan.getColor(), mdUnOrderListSpan.getNested()),
                start + 1,
                position == -1 ? start + 1 + appendString.length() : position,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTextWatcher.doOnTextChanged(editable, start + 1, 0, appendString.length());
        mTextWatcher.doAfterTextChanged(editable);
        mRxMDEditText.addTextChangedListener(mTextWatcher);
    }

    /**
     * checking the input character is '\n'
     *
     * @param editable the editable after finishing inputting
     * @param start    the start position
     * @param before   delete text's number
     * @param after    add text's number
     * @return whether input character is '\n'
     */
    private static boolean checkNewLineInput(Editable editable, int start, int before, int after) {
        if (after != 1 || before != 0 || start >= editable.length()) {
            return false;
        }
        if (editable.charAt(start) == '\n') {
            return true;
        }
        return false;
    }

    /**
     * checking the delete character is '\n'
     *
     * @param editable the editable after finishing deleting
     * @param start    the start position
     * @param before   delete text's number
     * @param after    add text's number
     * @return whether delete character is '\n'
     */
    private static boolean checkLineDelete(Editable editable, int start, int before, int after) {
        if (before != 1 || after != 0) {
            return false;
        }
        if (editable.charAt(start) == '\n') {
            return true;
        }
        return false;
    }

    /**
     * checking the start position is the head of line, and starts with order or unorder list key
     * aaa --> 1. aaa
     * aaa --> - aaa
     *
     * @param editable the editable after inputting
     * @param start    the start position
     * @param before   delete text's number
     * @param after    add text's number
     * @return whether it is the head of line and starts with order or unorder list key
     */
    private static boolean checkLineHeaderPosition(Editable editable, int start, int before, int after) {
        if (start == 0 || EditUtils.findBeforeNewLineChar(editable, start) + 1 == start) {
            int end = EditUtils.findNextNewLineCharCompat(editable, start);
            if (getOrderListSpan(editable, start, true) != null ||
                    getUnOrderListSpan(editable, start, true) != null ||
                    getOrderListSpan(editable, start + 1 > end ? start : end, true) != null ||
                    getUnOrderListSpan(editable, start + 1 > end ? start : end, true) != null) {
                //交给isBeginningOfListSpan()去处理
                return false;
            }
            boolean bool = isUnOrderList(editable, start, false);
            if (bool) {
                return bool;
            }
            return isOrderList(editable, start, false);
        }
        return false;
    }

    /**
     * add order and unorder list span
     * aaa --> 1. aaa
     * aaa --> - aaa
     *
     * @param editable the editable after inputting
     * @param start    the start position
     * @param before   delete text's number
     * @param after    add text's number
     */
    private void updateLineHeaderList(Editable editable, int start, int before, int after) {
        int position = EditUtils.findNextNewLineCharCompat(editable, start);
        int nested = calculateNested(editable, start, 0);
        if (nested == -1) {
            return;
        }
        if (isOrderList(editable, start, false)) {
            int number = calculateOrderListNumber(editable, start, 0);
            editable.setSpan(new MDOrderListSpan(10, nested, number),
                    start,
                    position,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else if (isUnOrderList(editable, start, false)) {
            editable.setSpan(new MDUnOrderListSpan(10, mRxMDConfiguration.getUnOrderListColor(), nested),
                    start,
                    position,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    /**
     * get the order list span, if there isn't contains order list span, return null
     *
     * @param editable  the text
     * @param start     the start position
     * @param beginning whether calculate from beginning
     * @return
     */
    @Nullable
    private static MDOrderListSpan getOrderListSpan(Editable editable, int start, boolean beginning) {
        MDOrderListSpan[] mdOrderListSpans;
        if (beginning) {
            mdOrderListSpans = editable.getSpans(start, start + 1, MDOrderListSpan.class);
        } else {
            mdOrderListSpans = editable.getSpans(start - 1, start, MDOrderListSpan.class);
        }
        if (mdOrderListSpans != null && mdOrderListSpans.length > 0) {
            return mdOrderListSpans[0];
        }
        return null;
    }

    /**
     * 1. aaa --> 1. a\n2. aa
     *
     * @param editable        the text
     * @param start           the start position
     * @param mdOrderListSpan the order list span
     */
    private static void updateOrderListSpanBeforeNewLine(Editable editable, int start, MDOrderListSpan mdOrderListSpan) {
        int position = EditUtils.findNextNewLineCharCompat(editable, start);
        int startSpan = editable.getSpanStart(mdOrderListSpan);
        int endSpan = editable.getSpanEnd(mdOrderListSpan);
        if (endSpan <= position) {
            return;
        }
        editable.removeSpan(mdOrderListSpan);
        editable.setSpan(new MDOrderListSpan(10, mdOrderListSpan.getNested(), mdOrderListSpan.getNumber()),
                startSpan, position, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * - aaa --> - a\n- aa
     *
     * @param editable          the text
     * @param start             the start position
     * @param mdUnOrderListSpan the order list span
     * @param nestedDecrease    need decrease nested
     */
    private static void updateUnOrderListSpanBeforeNewLine(Editable editable, int start, MDUnOrderListSpan mdUnOrderListSpan, boolean nestedDecrease) {
        int position = EditUtils.findNextNewLineCharCompat(editable, start);
        int startSpan = editable.getSpanStart(mdUnOrderListSpan);
        int endSpan = editable.getSpanEnd(mdUnOrderListSpan);
        if (endSpan <= position) {
            return;
        }
        editable.removeSpan(mdUnOrderListSpan);
        int nested = mdUnOrderListSpan.getNested();
        if (nestedDecrease && nested > 0) {
            nested--;
        }
        editable.setSpan(new MDUnOrderListSpan(10, mdUnOrderListSpan.getColor(), nested),
                startSpan, position, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * get the unorder list span, if there isn't contains unorder list span, return null
     *
     * @param editable  the text
     * @param start     the start position
     * @param beginning whether calculate from beginning
     * @return
     */
    @Nullable
    private static MDUnOrderListSpan getUnOrderListSpan(Editable editable, int start, boolean beginning) {
        MDUnOrderListSpan[] mdUnOrderListSpans;
        if (beginning) {
            mdUnOrderListSpans = editable.getSpans(start, start + 1, MDUnOrderListSpan.class);
        } else {
            mdUnOrderListSpans = editable.getSpans(start - 1, start, MDUnOrderListSpan.class);
        }
        if (mdUnOrderListSpans != null && mdUnOrderListSpans.length > 0) {
            return mdUnOrderListSpans[0];
        }
        return null;
    }

    /**
     * check the position is beginning of list span
     * "1. aaa" --> " 1. aaa"
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     * @return TRUE --> at the beginning
     */
    private static boolean isBeginningOfListSpan(Editable editable, int start, int before, int after) {
        if (before - after > 0) {
            return false;
        }
        MDOrderListSpan mdOrderListSpan = getOrderListBeginning(editable, start, before, after);
        MDUnOrderListSpan mdUnOrderListSpan = getUnOrderListBeginning(editable, start, before, after);
        if (mdOrderListSpan != null) {
            int spanStart = editable.getSpanStart(mdOrderListSpan);
            if (start <= spanStart ||
                    (start >= spanStart && start <= (spanStart + mdOrderListSpan.getNested() + String.valueOf(mdOrderListSpan.getNumber()).length() + 2))) {//2 --> ". "
                return true;
            }
        } else if (mdUnOrderListSpan != null) {
            int spanStart = editable.getSpanStart(mdUnOrderListSpan);
            if (start <= spanStart || (start >= spanStart && start <= (spanStart + mdUnOrderListSpan.getNested() + 2))) {//2 --> "(-/+/*) "
                return true;
            }
        }
        return false;
    }

    /**
     * get the order list span, if there doesn't contain, return null
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     * @return the order list span or null
     */
    @Nullable
    private static MDOrderListSpan getOrderListBeginning(Editable editable, int start, int before, int after) {
        if (before != 0) {
            MDOrderListSpan[] mdOrderListSpans = editable.getSpans(start, start, MDOrderListSpan.class);
            if (mdOrderListSpans != null && mdOrderListSpans.length > 0) {
                return mdOrderListSpans[0];
            }
            return null;
        } else {
            if (start + 1 > editable.length()) {
                return null;
            }
            int addNumber = Math.abs(after - before);//增加了多少
            MDOrderListSpan[] mdOrderListSpans = editable.getSpans(start + addNumber, start + addNumber + 1, MDOrderListSpan.class);
            if (mdOrderListSpans != null && mdOrderListSpans.length > 0) {
                return mdOrderListSpans[0];
            }
            return null;
        }
    }

    /**
     * get the unorder list span, if there doesn't contain, return null
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     * @return the unorder list span or null
     */
    @Nullable
    private static MDUnOrderListSpan getUnOrderListBeginning(Editable editable, int start, int before, int after) {
        if (before != 0) {
            MDUnOrderListSpan[] mdUnOrderListSpans = editable.getSpans(start, start, MDUnOrderListSpan.class);
            if (mdUnOrderListSpans != null && mdUnOrderListSpans.length > 0) {
                return mdUnOrderListSpans[0];
            }
            return null;
        } else {
            if (start + 1 > editable.length()) {
                return null;
            }
            MDUnOrderListSpan[] mdUnOrderListSpans = editable.getSpans(start, start + after + 1, MDUnOrderListSpan.class);
            if (mdUnOrderListSpans != null && mdUnOrderListSpans.length > 0) {
                return mdUnOrderListSpans[0];
            }
            return null;
        }
    }

    /**
     * "1. aaa" --> " 1. aaa" change nested number
     * "1. aaa" --> "12. aaa" change list number
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     */
    private static void updateListSpanBeginning(Editable editable, int start, int before, int after) {
        MDOrderListSpan mdOrderListSpan = getOrderListBeginning(editable, start, before, after);
        MDUnOrderListSpan mdUnOrderListSpan = getUnOrderListBeginning(editable, start, before, after);
        if (mdOrderListSpan != null) {
            int spanEnd = editable.getSpanEnd(mdOrderListSpan);
            int position = EditUtils.findBeforeNewLineChar(editable, start) + 1;
            if (!isOrderList(editable, position, false)) {
                editable.removeSpan(mdOrderListSpan);
                return;
            }
            int nested = calculateNested(editable, position, 0);
            if (nested == -1) {
                return;
            }
            editable.removeSpan(mdOrderListSpan);
            int number = calculateOrderListNumber(editable, position + nested, 0);
            editable.setSpan(new MDOrderListSpan(10, nested, number),
                    position, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else if (mdUnOrderListSpan != null) {
            int spanEnd = editable.getSpanEnd(mdUnOrderListSpan);
            int position = EditUtils.findBeforeNewLineChar(editable, start) + 1;
            if (!isUnOrderList(editable, position, false)) {
                editable.removeSpan(mdUnOrderListSpan);
                return;
            }
            int nested = calculateNested(editable, position, 0);
            if (nested == -1) {
                return;
            }
            editable.removeSpan(mdUnOrderListSpan);
            editable.setSpan(new MDUnOrderListSpan(10, mdUnOrderListSpan.getColor(), nested),
                    position, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    /**
     * calculate nested number
     *
     * @param s             the text
     * @param next          the next position
     * @param currentNested current nested number
     * @return the nested number
     */
    private static int calculateNested(CharSequence s, int next, int currentNested) {
        if (next + 1 > s.length()) {
            return -1;
        }
        if (s.charAt(next) == ' ') {
            return calculateNested(s, next + 1, currentNested + 1);
        } else {
            return currentNested;
        }
    }

    /**
     * calculate order list number
     *
     * @param s      the text
     * @param next   the start position
     * @param number current list number
     * @return the list number
     */
    private static int calculateOrderListNumber(CharSequence s, int next, int number) {
        if (next + 1 > s.length()) {
            return number;
        }
        CharSequence cs = s.subSequence(next, next + 1);
        if (TextUtils.isDigitsOnly(cs)) {
            number = number * 10 + Integer.parseInt(String.valueOf(cs));
            return calculateOrderListNumber(s, next + 1, number);
        } else if (" ".equals(cs)) {
            return calculateOrderListNumber(s, next + 1, number);
        } else {
            return number;
        }
    }

    /**
     * check the text is order list formatOrderList
     *
     * @param s        the text
     * @param next     the next position
     * @param isNumber is already judge number, so next is "." or number
     * @return TRUE --> is order list formatOrderList
     */
    private static boolean isOrderList(CharSequence s, int next, boolean isNumber) {
        if (next + 1 > s.length()) {
            return false;
        }
        char c = s.charAt(next);
        if (isNumber) {
            if (Character.isDigit(c)) {
                return isOrderList(s, next + 1, true);
            } else if (c == '.') {
                return true;
            } else {
                return false;
            }
        } else if (c == ' ') {
            return isOrderList(s, next + 1, false);
        } else if (Character.isDigit(c)) {
            return isOrderList(s, next + 1, true);
        } else {
            return false;
        }
    }

    /**
     * check the text is unorder list formatOrderList
     *
     * @param s      the text
     * @param next   the next position
     * @param hasKey is already judge the key(*\+\-)
     * @return TRUE --> is unorder list formatOrderList
     */
    private static boolean isUnOrderList(CharSequence s, int next, boolean hasKey) {
        if (next + 1 > s.length()) {
            return false;
        }
        if (hasKey) {
            char c = s.charAt(next);
            if (c == ' ') {
                return true;
            } else {
                return false;
            }
        } else {
            char c = s.charAt(next);
            if (c == '+' || c == '-' || c == '*') {
                return isUnOrderList(s, next + 1, true);
            } else if (c == ' ') {
                return isUnOrderList(s, next + 1, false);
            } else {
                return false;
            }
        }
    }

    /**
     * check whether satisfy the order list formatOrderList
     * additional premise, there doesn't contain order list span
     *
     * @param editable the text
     * @param start    the start position
     * @return TRUE --> satisfied
     */
    private static boolean isSatisfiedOrderListFormat(Editable editable, int start) {
        int startPosition = EditUtils.findBeforeNewLineChar(editable, start) + 1;//略过\n
        int endPosition = EditUtils.findNextNewLineCharCompat(editable, start);
        MDOrderListSpan[] mdOrderListSpans = editable.getSpans(startPosition, endPosition, MDOrderListSpan.class);
        if (mdOrderListSpans != null && mdOrderListSpans.length > 0) {
            return false;
        }
        CharSequence charSequence = editable.subSequence(startPosition, endPosition);
        Pattern p = Pattern.compile("^( *)(\\d+)\\. (.*?)$");
        Matcher m = p.matcher(charSequence);
        return m.matches();
    }

    /**
     * formatOrderList to the order list
     *
     * @param editable
     * @param start
     */
    private static void formatOrderList(Editable editable, int start) {
        int startPosition = EditUtils.findBeforeNewLineChar(editable, start) + 1;//略过\n
        int endPosition = EditUtils.findNextNewLineCharCompat(editable, start);
        int nested = calculateNested(editable, startPosition, 0);
        int number = calculateOrderListNumber(editable, startPosition + nested, 0);
        editable.setSpan(new MDOrderListSpan(10, nested, number),
                startPosition, endPosition, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * whether change nested number
     * 11. aa --> 1. aa
     * " 1. aa" --> "1. aa"
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     * @return TRUE --> should change nested number
     */
    private static boolean checkDeleteOrderListSpan(Editable editable, int start, int before, int after) {
        if (before == 0) {
            return false;
        }
        MDOrderListSpan mdOrderListSpan = getOrderListSpan(editable, start, true);
        if (mdOrderListSpan == null) {
            return false;
        }
        int position = EditUtils.findBeforeNewLineChar(editable, start) + 1;
        int totalPosition = position + mdOrderListSpan.getNested() + String.valueOf(mdOrderListSpan.getNumber()).length() + " ".length();
        if (totalPosition >= start || start <= position) {
            return true;
        }
        return false;
    }

    /**
     * whether change nested number
     * " + aa" --> "+ aa"
     *
     * @param editable the text
     * @param start    the start position
     * @param before   the delete number
     * @param after    the add number
     * @return TRUE --> should change nested number
     */
    private static boolean checkDeleteUnOrderListSpan(Editable editable, int start, int before, int after) {
        if (before == 0) {
            return false;
        }
        MDUnOrderListSpan mdUnOrderListSpan = getUnOrderListSpan(editable, start, true);
        if (mdUnOrderListSpan == null) {
            return false;
        }
        int position = EditUtils.findBeforeNewLineChar(editable, start) + 1;
        int totalPosition = position + mdUnOrderListSpan.getNested() + "* ".length();
        if (totalPosition >= start || start <= position) {
            return true;
        }
        return false;
    }
}
