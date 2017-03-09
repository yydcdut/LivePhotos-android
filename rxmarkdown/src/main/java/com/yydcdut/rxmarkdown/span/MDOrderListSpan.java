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
package com.yydcdut.rxmarkdown.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.BulletSpan;

import java.util.List;

/**
 * order list grammar span
 * <p>
 * Created by yuyidong on 16/7/8.
 */
public class MDOrderListSpan extends BulletSpan {

    private MDOrderListSpan mParent;//// TODO: 2017/3/5 添加父节点
    private List<MDOrderListSpan> mChildren;//// TODO: 2017/3/5 添加子节点 

    private int mNumber;
    private int mNested;
    private static final int NESTED_MARGIN_LENGTH = 9;
    private static final int GAP_WIDTH_PLUS = 10;

    /**
     * Constructor
     *
     * @param gapWidth {@link MDUnOrderListSpan}
     * @param nested   the nested number
     * @param number   the number of order list
     */
    public MDOrderListSpan(int gapWidth, int nested, int number) {
        super(gapWidth, Color.TRANSPARENT);
        mNested = nested;
        mNumber = number;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return super.getLeadingMargin(first) + GAP_WIDTH_PLUS * (mNested + 1);
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        super.drawLeadingMargin(c, p, x + (mNested + 1) * NESTED_MARGIN_LENGTH, dir, top, baseline, bottom, text, start, end, first, l);
    }

    /**
     * get order list number
     *
     * @return the list number
     */
    public int getNumber() {
        return mNumber;
    }

    /**
     * get list nested
     *
     * @return the nested number
     */
    public int getNested() {
        return mNested;
    }
}
