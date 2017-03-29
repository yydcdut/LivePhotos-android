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

import android.text.Editable;

import java.util.ArrayList;
import java.util.List;

/**
 * utility class for edit controller in RxMarkdown
 * <p>
 * Created by yuyidong on 16/7/21.
 */
class EditUtils {

    /**
     * find '\n' from "start" position
     * if not find, return -1
     *
     * @param s     text
     * @param start start position
     * @return the '\n' position
     */
    protected static int findNextNewLineChar(CharSequence s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                return i;
            }
        }
        return -1;
    }

    /**
     * find '\n' from "start" position
     * if not find, return s.length()
     *
     * @param s     text
     * @param start start position
     * @return the '\n' position
     */
    protected static int findNextNewLineCharCompat(CharSequence s, int start) {
        int position = findNextNewLineChar(s, start);
        if (position == -1) {
            return s.length();
        }
        return position;
    }

    /**
     * find '\n' before "start" position
     *
     * @param s     text
     * @param start start position
     * @return the '\n' position
     */
    protected static int findBeforeNewLineChar(CharSequence s, int start) {
        for (int i = start - 1; i > 0; i--) {
            if (s.charAt(i) == '\n') {
                return i;
            }
        }
        return -1;
    }

    /**
     * remove spans
     *
     * @param editable Editable, the text
     * @param start    int, the selection position
     * @param clazz    class
     * @param <T>      span
     */
    protected static <T> void removeSpans(Editable editable, int start, Class<T> clazz) {
        int startPosition = findBeforeNewLineChar(editable, start) + 1;
        int endPosition = findNextNewLineCharCompat(editable, start);
        T[] ts = editable.getSpans(startPosition, endPosition, clazz);
        for (T t : ts) {
            editable.removeSpan(t);
        }
    }

    /**
     * set spans
     *
     * @param editable      Editable, the text
     * @param editTokenList List, the edit token collection
     */
    protected static void setSpans(Editable editable, List<EditToken> editTokenList) {
        for (EditToken editToken : editTokenList) {
            editable.setSpan(editToken.getSpan(), editToken.getStart(), editToken.getEnd(), editToken.getFlag());
        }
    }

    /**
     * get matched edit token list
     *
     * @param editable Editable, the text
     * @param allList  List, the edit token collection
     * @param start    the selection position
     * @return the matched edit token list
     */
    protected static List<EditToken> getMatchedEditTokenList(Editable editable, List<EditToken> allList, int start) {
        List<EditToken> matchEditTokenList = new ArrayList<>();
        int startPosition = findBeforeNewLineChar(editable, start) + 1;
        int endPosition = findNextNewLineCharCompat(editable, start);
        for (EditToken editToken : allList) {
            if (editToken.getStart() >= startPosition && editToken.getEnd() <= endPosition) {
                matchEditTokenList.add(editToken);
            }
        }
        return matchEditTokenList;
    }
}
