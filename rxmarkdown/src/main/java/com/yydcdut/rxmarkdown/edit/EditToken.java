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

import android.support.annotation.NonNull;
import android.text.Spannable;

/**
 * The class to remain information for editing mode.
 * <p>
 * Created by yuyidong on 16/6/28.
 */
public class EditToken {
    private final Object span;
    private final int start;
    private final int end;
    private final int flag;//todo 替换

    /**
     * Constructor
     *
     * @param span  the span
     * @param start the start position
     * @param end   the edn position
     */
    public EditToken(@NonNull Object span, int start, int end) {
        this(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public EditToken(@NonNull Object span, int start, int end, int flag) {
        this.span = span;
        this.start = start;
        this.end = end;
        this.flag = flag;
    }

    public Object getSpan() {
        return span;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getFlag() {
        return flag;
    }
}
