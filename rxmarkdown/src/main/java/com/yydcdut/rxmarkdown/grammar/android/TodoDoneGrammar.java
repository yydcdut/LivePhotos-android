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

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.span.MDTodoDoneSpan;

/**
 * The implementation of grammar for to do.
 * Grammar:
 * "- [x] "
 * <p>
 * "- [X] "
 * <p>
 * Created by yuyidong on 16/5/17.
 */
class TodoDoneGrammar extends AbsAndroidGrammar {

    protected static final String KEY_0_TODO_DONE = "- [x] ";
    protected static final String KEY_1_TODO_DONE = "- [X] ";
    protected static final String KEY_2_TODO_DONE = "* [x] ";
    protected static final String KEY_3_TODO_DONE = "* [X] ";

    private static final int START_POSITION = 6;

    private int mColor;

    TodoDoneGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
        mColor = rxMDConfiguration.getTodoDoneColor();
    }

    @Override
    boolean isMatch(@NonNull String text) {
        return text.startsWith(KEY_0_TODO_DONE) ||
                text.startsWith(KEY_1_TODO_DONE) ||
                text.startsWith(KEY_2_TODO_DONE) ||
                text.startsWith(KEY_3_TODO_DONE);
    }

    @NonNull
    @Override
    SpannableStringBuilder encode(@NonNull SpannableStringBuilder ssb) {
        return ssb;
    }

    @Override
    SpannableStringBuilder format(@NonNull SpannableStringBuilder ssb) {
        ssb.delete(0, START_POSITION);
        ssb.setSpan(new MDTodoDoneSpan(mColor), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @NonNull
    @Override
    SpannableStringBuilder decode(@NonNull SpannableStringBuilder ssb) {
        return ssb;
    }
}
