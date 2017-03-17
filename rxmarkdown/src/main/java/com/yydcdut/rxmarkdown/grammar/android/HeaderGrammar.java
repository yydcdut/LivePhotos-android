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
import android.text.style.RelativeSizeSpan;

import com.yydcdut.rxmarkdown.RxMDConfiguration;

/**
 * The implementation of grammar for header.
 * Grammar:
 * "# " for h1
 * <p>
 * "## " for h2
 * <p>
 * "### " for h3
 * <p>
 * "#### " for h4
 * <p>
 * "##### " for h5
 * <p>
 * "###### " for h6
 * <p>
 * Created by yuyidong on 16/5/20.
 */
class HeaderGrammar extends AbsAndroidGrammar {

    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_0_HEADER}
     */
    protected static final String KEY_0_HEADER = "#";
    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_1_HEADER}
     */
    protected static final String KEY_1_HEADER = "##";
    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_2_HEADER}
     */
    protected static final String KEY_2_HEADER = "###";
    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_3_HEADER}
     */
    protected static final String KEY_3_HEADER = "####";
    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_4_HEADER}
     */
    protected static final String KEY_4_HEADER = "#####";
    /**
     * {@link com.yydcdut.rxmarkdown.grammar.edit.HeaderGrammar#KEY_5_HEADER}
     */
    protected static final String KEY_5_HEADER = "######";

    private float mHeader1RelativeSize;
    private float mHeader2RelativeSize;
    private float mHeader3RelativeSize;
    private float mHeader4RelativeSize;
    private float mHeader5RelativeSize;
    private float mHeader6RelativeSize;

    HeaderGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
        mHeader1RelativeSize = rxMDConfiguration.getHeader1RelativeSize();
        mHeader2RelativeSize = rxMDConfiguration.getHeader2RelativeSize();
        mHeader3RelativeSize = rxMDConfiguration.getHeader3RelativeSize();
        mHeader4RelativeSize = rxMDConfiguration.getHeader4RelativeSize();
        mHeader5RelativeSize = rxMDConfiguration.getHeader5RelativeSize();
        mHeader6RelativeSize = rxMDConfiguration.getHeader6RelativeSize();
    }

    @Override
    boolean isMatch(@NonNull String text) {
        return text.startsWith(KEY_0_HEADER) ||
                text.startsWith(KEY_1_HEADER) ||
                text.startsWith(KEY_2_HEADER) ||
                text.startsWith(KEY_3_HEADER) ||
                text.startsWith(KEY_4_HEADER) ||
                text.startsWith(KEY_5_HEADER);
    }

    @NonNull
    @Override
    SpannableStringBuilder encode(@NonNull SpannableStringBuilder ssb) {
        return ssb;
    }

    @NonNull
    @Override
    SpannableStringBuilder format(@NonNull SpannableStringBuilder ssb) {
        String text = ssb.toString();
        if (text.startsWith(KEY_5_HEADER)) {
            ssb.delete(0, KEY_5_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader6RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith(KEY_4_HEADER)) {
            ssb.delete(0, KEY_4_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader5RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith(KEY_3_HEADER)) {
            ssb.delete(0, KEY_3_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader4RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith(KEY_2_HEADER)) {
            ssb.delete(0, KEY_2_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader3RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith(KEY_1_HEADER)) {
            ssb.delete(0, KEY_1_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader2RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith(KEY_0_HEADER)) {
            ssb.delete(0, KEY_0_HEADER.length());
            ssb.setSpan(new RelativeSizeSpan(mHeader1RelativeSize), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    @NonNull
    @Override
    SpannableStringBuilder decode(@NonNull SpannableStringBuilder ssb) {
        return ssb;
    }
}
