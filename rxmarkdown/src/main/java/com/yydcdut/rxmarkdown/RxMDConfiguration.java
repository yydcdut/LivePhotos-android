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
package com.yydcdut.rxmarkdown;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.yydcdut.rxmarkdown.callback.BlockquoteBackgroundNestedColorFetcher;
import com.yydcdut.rxmarkdown.callback.OnLinkClickCallback;
import com.yydcdut.rxmarkdown.loader.DefaultLoader;
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader;

/**
 * The display configuration of RxMarkdown
 * <p>
 * Created by yuyidong on 16/6/22.
 */
public class RxMDConfiguration {
    private final int[] defaultImageSize;
    @ColorInt
    private final int blockQuotesColor;
    private final float header1RelativeSize;
    private final float header2RelativeSize;
    private final float header3RelativeSize;
    private final float header4RelativeSize;
    private final float header5RelativeSize;
    private final float header6RelativeSize;
    private final float blockQuoteRelativeSize;
    @ColorInt
    private final int horizontalRulesColor;
    private int horizontalRulesHeight;
    @ColorInt
    private final int inlineCodeBgColor;
    @ColorInt
    private final int codeBgColor;
    @ColorInt
    private final int todoColor;
    @ColorInt
    private final int todoDoneColor;
    @ColorInt
    private final int blockQuoteBgColor;
    private final BlockquoteBackgroundNestedColorFetcher colorFetcher;
    @ColorInt
    private int unOrderListColor;
    @ColorInt
    private int linkColor;
    private boolean isLinkUnderline;
    private boolean isAppendNewlineAfterLastLine;
    private RxMDImageLoader rxMDImageLoader;
    private OnLinkClickCallback onLinkClickCallback;


    private boolean isDebug = true;

    /**
     * Constructor
     *
     * @param defaultImageSize             default image size
     * @param blockQuotesColor             block quotes color
     * @param header1RelativeSize          header1 relative size
     * @param header2RelativeSize          header2 relative size
     * @param header3RelativeSize          header3 relative size
     * @param header4RelativeSize          header4 relative size
     * @param header5RelativeSize          header5 relative size
     * @param header6RelativeSize          header6 relative size
     * @param blockQuoteRelativeSize       blockQuote relative size
     * @param horizontalRulesColor         horizontal rules color
     * @param horizontalRulesHeight        horizontal rules height
     * @param inlineCodeBgColor            inline code bg color
     * @param codeBgColor                  code bg color
     * @param todoColor                    to do color
     * @param todoDoneColor                to do done color
     * @param unOrderListColor             unorder list color
     * @param blockQuoteBgColor            blockQuote background color
     * @param linkColor                    link color
     * @param isLinkUnderline              link underline
     * @param rxMDImageLoader              loader
     * @param onLinkClickCallback          lick click callback
     * @param isDebug                      debug
     * @param isAppendNewlineAfterLastLine add newline after last line
     * @param colorFetcher                 the nested blockQuote color callback
     */
    private RxMDConfiguration(int[] defaultImageSize, int blockQuotesColor,
                              float header1RelativeSize, float header2RelativeSize,
                              float header3RelativeSize, float header4RelativeSize,
                              float header5RelativeSize, float header6RelativeSize,
                              float blockQuoteRelativeSize, int horizontalRulesColor, int horizontalRulesHeight, int inlineCodeBgColor, int codeBgColor,
                              int todoColor, int todoDoneColor, int unOrderListColor,
                              int blockQuoteBgColor, int linkColor, boolean isLinkUnderline,
                              RxMDImageLoader rxMDImageLoader, OnLinkClickCallback onLinkClickCallback,
                              boolean isDebug, boolean isAppendNewlineAfterLastLine, BlockquoteBackgroundNestedColorFetcher colorFetcher) {
        this.defaultImageSize = defaultImageSize;
        this.blockQuotesColor = blockQuotesColor;
        this.header1RelativeSize = header1RelativeSize;
        this.header2RelativeSize = header2RelativeSize;
        this.header3RelativeSize = header3RelativeSize;
        this.header4RelativeSize = header4RelativeSize;
        this.header5RelativeSize = header5RelativeSize;
        this.header6RelativeSize = header6RelativeSize;
        this.blockQuoteRelativeSize = blockQuoteRelativeSize;
        this.horizontalRulesColor = horizontalRulesColor;
        this.horizontalRulesHeight = horizontalRulesHeight;
        this.inlineCodeBgColor = inlineCodeBgColor;
        this.codeBgColor = codeBgColor;
        this.todoColor = todoColor;
        this.todoDoneColor = todoDoneColor;
        this.unOrderListColor = unOrderListColor;
        this.blockQuoteBgColor = blockQuoteBgColor;
        this.linkColor = linkColor;
        this.isLinkUnderline = isLinkUnderline;
        this.rxMDImageLoader = rxMDImageLoader;
        this.onLinkClickCallback = onLinkClickCallback;
        this.isDebug = isDebug;
        this.isAppendNewlineAfterLastLine = isAppendNewlineAfterLastLine;
        this.colorFetcher = colorFetcher;
    }

    /**
     * get image default size
     *
     * @return the array size is 2, [width, height]
     */
    public final int[] getDefaultImageSize() {
        return defaultImageSize;
    }

    /**
     * get block quote color
     *
     * @return the color
     */
    public final int getBlockQuotesColor() {
        return blockQuotesColor;
    }

    /**
     * get header1 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader1RelativeSize() {
        return header1RelativeSize;
    }

    /**
     * get header2 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader2RelativeSize() {
        return header2RelativeSize;
    }

    /**
     * get header3 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader3RelativeSize() {
        return header3RelativeSize;
    }

    /**
     * get header4 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader4RelativeSize() {
        return header4RelativeSize;
    }

    /**
     * get header5 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader5RelativeSize() {
        return header5RelativeSize;
    }

    /**
     * get header6 relative size
     *
     * @return the size relative to current text
     */
    public final float getHeader6RelativeSize() {
        return header6RelativeSize;
    }

    /**
     * get blockquote relative size
     *
     * @return the size relative to current text
     */
    public final float getBlockQuoteRelativeSize() {
        return blockQuoteRelativeSize;
    }

    /**
     * get horizontal rules color
     *
     * @return the color
     */
    public final int getHorizontalRulesColor() {
        return horizontalRulesColor;
    }

    /**
     * get horizontal rules height
     *
     * @return the height of horizontal rules
     */
    public int getHorizontalRulesHeight() {
        return horizontalRulesHeight;
    }

    /**
     * get inline code background color
     *
     * @return the color
     */
    public final int getInlineCodeBgColor() {
        return inlineCodeBgColor;
    }

    /**
     * get code background color
     *
     * @return the color
     */
    public final int getCodeBgColor() {
        return codeBgColor;
    }

    /**
     * get to do color
     *
     * @return the color
     */
    public final int getTodoColor() {
        return todoColor;
    }

    /**
     * get done color
     *
     * @return the color
     */
    public final int getTodoDoneColor() {
        return todoDoneColor;
    }

    /**
     * get unorder list color
     *
     * @return the color
     */
    public final int getUnOrderListColor() {
        return unOrderListColor;
    }

    /**
     * get blockquote background color
     *
     * @return the color
     */
    public final int getBlockQuoteBgColor() {
        return blockQuoteBgColor;
    }

    /**
     * get link color
     *
     * @return the color
     */
    public int getLinkColor() {
        return linkColor;
    }

    /**
     * whether link underline
     *
     * @return whether link underline
     */
    public boolean isLinkUnderline() {
        return isLinkUnderline;
    }

    /**
     * get loader
     *
     * @return {@link RxMDImageLoader}
     */
    public RxMDImageLoader getRxMDImageLoader() {
        return rxMDImageLoader;
    }

    /**
     * get link click callback
     *
     * @return {@link OnLinkClickCallback}
     */
    public OnLinkClickCallback getOnLinkClickCallback() {
        return onLinkClickCallback;
    }

    /**
     * whether is debug or not
     *
     * @return TRUE:debug
     */
    public boolean isDebug() {
        return isDebug;
    }


    /**
     * whether to append a newline character to the last line of the parsed text
     *
     * @return <code>true</code> if newline should be appended
     */
    public boolean isAppendNewlineAfterLastLine() {
        return isAppendNewlineAfterLastLine;
    }

    /**
     * Get the color fetcher for background colors for nested blockquote levels.
     * If not set, the standard background color will be used.
     *
     * @return the fetcher
     */
    public BlockquoteBackgroundNestedColorFetcher getBlockQuoteBackgroundNestedColorFetcher() {
        return colorFetcher;
    }

    /**
     * the build of configuration
     */
    public static class Builder {

        private int[] defaultImageSize;

        @ColorInt
        private int blockQuotesColor;

        private float header1RelativeSize;
        private float header2RelativeSize;
        private float header3RelativeSize;
        private float header4RelativeSize;
        private float header5RelativeSize;
        private float header6RelativeSize;

        private float blockQuoteRelativeSize;

        @ColorInt
        private int horizontalRulesColor;
        private int horizontalRulesHeight;

        @ColorInt
        private int inlineCodeBgColor;

        @ColorInt
        private int codeBgColor;

        @ColorInt
        private int todoColor;

        @ColorInt
        private int todoDoneColor;

        @ColorInt
        private int unOrderListColor;

        @ColorInt
        private int blockQuoteBgColor;

        @ColorInt
        private int linkColor;
        private boolean isLinkUnderline;

        private RxMDImageLoader rxMDImageLoader;

        private OnLinkClickCallback mOnLinkClickCallback;

        private boolean isDebug = true;

        private boolean isAppendNewlineAfterLastLine;

        private BlockquoteBackgroundNestedColorFetcher colorFetcher = null;


        /**
         * Constructor
         *
         * @param context Context
         */
        public Builder(@NonNull Context context) {
            defaultImageSize = new int[]{100, 100};
            blockQuotesColor = Color.LTGRAY;
            header1RelativeSize = 1.6f;
            header2RelativeSize = 1.5f;
            header3RelativeSize = 1.4f;
            header4RelativeSize = 1.3f;
            header5RelativeSize = 1.2f;
            header6RelativeSize = 1.1f;
            blockQuoteRelativeSize = 1f;
            horizontalRulesColor = Color.LTGRAY;
            horizontalRulesHeight = -1;
            inlineCodeBgColor = Color.LTGRAY;
            codeBgColor = Color.LTGRAY;
            todoColor = Color.DKGRAY;
            todoDoneColor = Color.DKGRAY;
            unOrderListColor = Color.BLACK;
            blockQuoteBgColor = Color.TRANSPARENT;
            linkColor = Color.RED;
            isLinkUnderline = true;
            rxMDImageLoader = new DefaultLoader(context);
            mOnLinkClickCallback = null;
            isAppendNewlineAfterLastLine = true;
        }

        /**
         * set image default size
         *
         * @param width  the default width to display
         * @param height the default height to display
         * @return self
         */
        public Builder setDefaultImageSize(int width, int height) {
            defaultImageSize = new int[]{width, height};
            return this;
        }

        /**
         * set block quote color
         *
         * @param blockQuotesColor the color
         * @return self
         */
        public Builder setBlockQuotesColor(@ColorInt int blockQuotesColor) {
            this.blockQuotesColor = blockQuotesColor;
            return this;
        }

        /**
         * set header h1 relative size
         *
         * @param header1RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader1RelativeSize(float header1RelativeSize) {
            this.header1RelativeSize = header1RelativeSize;
            return this;
        }

        /**
         * set header h2 relative size
         *
         * @param header2RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader2RelativeSize(float header2RelativeSize) {
            this.header2RelativeSize = header2RelativeSize;
            return this;
        }

        /**
         * set header h3 relative size
         *
         * @param header3RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader3RelativeSize(float header3RelativeSize) {
            this.header3RelativeSize = header3RelativeSize;
            return this;
        }

        /**
         * set header h4 relative size
         *
         * @param header4RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader4RelativeSize(float header4RelativeSize) {
            this.header4RelativeSize = header4RelativeSize;
            return this;
        }

        /**
         * set header h5 relative size
         *
         * @param header5RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader5RelativeSize(float header5RelativeSize) {
            this.header5RelativeSize = header5RelativeSize;
            return this;
        }

        /**
         * set header h6 relative size
         *
         * @param header6RelativeSize relative to current text size
         * @return self
         */
        public Builder setHeader6RelativeSize(float header6RelativeSize) {
            this.header6RelativeSize = header6RelativeSize;
            return this;
        }

        /**
         * set the blockquote relative size compared to standard size
         *
         * @param blockQuoteRelativeSize the size
         * @return self
         */
        public Builder setBlockQuoteRelativeSize(float blockQuoteRelativeSize) {
            this.blockQuoteRelativeSize = blockQuoteRelativeSize;
            return this;
        }

        /**
         * set horizontal rules color
         *
         * @param horizontalRulesColor the color
         * @return self
         */
        public Builder setHorizontalRulesColor(@ColorInt int horizontalRulesColor) {
            this.horizontalRulesColor = horizontalRulesColor;
            return this;
        }

        /**
         * set horizontal rules height
         *
         * @param horizontalRulesHeight horizontal rules height
         * @return self
         */
        public Builder setHorizontalRulesHeight(int horizontalRulesHeight) {
            this.horizontalRulesHeight = horizontalRulesHeight;
            return this;
        }

        /**
         * set inline code background color
         *
         * @param inlineCodeBgColor the color
         * @return self
         */
        public Builder setInlineCodeBgColor(@ColorInt int inlineCodeBgColor) {
            this.inlineCodeBgColor = inlineCodeBgColor;
            return this;
        }

        /**
         * set code background color
         *
         * @param codeBgColor the color
         * @return self
         */
        public Builder setCodeBgColor(@ColorInt int codeBgColor) {
            this.codeBgColor = codeBgColor;
            return this;
        }

        /**
         * set done color
         *
         * @param todoDoneColor the color
         * @return self
         */
        public Builder setTodoDoneColor(@ColorInt int todoDoneColor) {
            this.todoDoneColor = todoDoneColor;
            return this;
        }

        /**
         * set to do color
         *
         * @param todoColor the color
         * @return self
         */
        public Builder setTodoColor(@ColorInt int todoColor) {
            this.todoColor = todoColor;
            return this;
        }

        /**
         * set unorder list color
         *
         * @param unOrderListColor the color
         * @return self
         */
        public Builder setUnOrderListColor(int unOrderListColor) {
            this.unOrderListColor = unOrderListColor;
            return this;
        }

        /**
         * set the blockquote background color
         *
         * @param blockQuoteBgColor the color
         * @return self
         */
        public Builder setBlockQuoteBgColor(int blockQuoteBgColor) {
            this.blockQuoteBgColor = blockQuoteBgColor;
            return this;
        }

        /**
         * set link color
         *
         * @param linkColor the color
         * @return self
         */
        public Builder setLinkColor(int linkColor) {
            this.linkColor = linkColor;
            return this;
        }

        /**
         * is link underline
         *
         * @param linkUnderline boolean, whether link underline
         * @return self
         */
        public Builder setLinkUnderline(boolean linkUnderline) {
            isLinkUnderline = linkUnderline;
            return this;
        }

        /**
         * set loader
         *
         * @param rxMDImageLoader the loader
         * @return self
         */
        public Builder setRxMDImageLoader(RxMDImageLoader rxMDImageLoader) {
            this.rxMDImageLoader = rxMDImageLoader;
            return this;
        }

        /**
         * set link click callback
         *
         * @param onLinkClickCallback OnLinkClickCallback, the callback
         * @return self
         */
        public Builder setOnLinkClickCallback(OnLinkClickCallback onLinkClickCallback) {
            mOnLinkClickCallback = onLinkClickCallback;
            return this;
        }

        /**
         * whether debug or not
         * default is true
         *
         * @param debug boolean
         * @return self
         */
        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        /**
         * whether to append a newline character to the last line of the parsed text
         *
         * @param append <code>true if newline should be appended</code>
         * @return self
         */
        public Builder setAppendNewlineAfterLastLine(boolean append) {
            isAppendNewlineAfterLastLine = append;
            return this;
        }

        /**
         * set nested block quote background color
         *
         * @param colorFetcher the callback
         * @return self
         */
        public Builder setBlockquoteBackgroundNestedColorFetcher(BlockquoteBackgroundNestedColorFetcher colorFetcher) {
            this.colorFetcher = colorFetcher;
            return this;
        }

        /**
         * get RxMDConfiguration
         *
         * @return RxMDConfiguration
         */
        public RxMDConfiguration build() {
            return new RxMDConfiguration(
                    defaultImageSize, blockQuotesColor,
                    header1RelativeSize,
                    header2RelativeSize,
                    header3RelativeSize,
                    header4RelativeSize,
                    header5RelativeSize,
                    header6RelativeSize,
                    blockQuoteRelativeSize, horizontalRulesColor, horizontalRulesHeight, inlineCodeBgColor, codeBgColor,
                    todoColor, todoDoneColor, unOrderListColor,
                    blockQuoteBgColor, linkColor, isLinkUnderline,
                    rxMDImageLoader, mOnLinkClickCallback,
                    isDebug, isAppendNewlineAfterLastLine, colorFetcher);
        }

    }
}
