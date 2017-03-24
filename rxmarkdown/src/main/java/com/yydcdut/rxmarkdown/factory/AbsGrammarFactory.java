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
package com.yydcdut.rxmarkdown.factory;

import android.support.annotation.NonNull;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.grammar.IGrammar;

/**
 * Abstract factory pattern.
 * To provide grammars and parse content.
 * <p>
 * Created by yuyidong on 16/5/12.
 */
public abstract class AbsGrammarFactory {

    /**
     * for normal
     */
    public static final int GRAMMAR_NORMAL = -1;

    /**
     * for horizontal rules
     */
    public static final int GRAMMAR_HORIZONTAL_RULES = 0;
    /**
     * for block quotes
     */
    public static final int GRAMMAR_BLOCK_QUOTES = 1;
    /**
     * for center align
     */
    public static final int GRAMMAR_CENTER_ALIGN = 10;
    /**
     * for header
     */
    public static final int GRAMMAR_HEADER_LINE = 11;
    /**
     * for bold
     */
    public static final int GRAMMAR_BOLD = 14;
    /**
     * for italic
     */
    public static final int GRAMMAR_ITALIC = 15;
    /**
     * for inline code
     */
    public static final int GRAMMAR_INLINE_CODE = 16;
    /**
     * for strike through
     */
    public static final int GRAMMAR_STRIKE_THROUGH = 17;
    /**
     * for footnote
     */
    public static final int GRAMMAR_FOOTNOTE = 18;
    /**
     * for image
     */
    public static final int GRAMMAR_IMAGE = 19;
    /**
     * for hyper link
     */
    public static final int GRAMMAR_HYPERLINK = 20;
    /**
     * for to do
     */
    public static final int GRAMMAR_TODO = 21;
    /**
     * for done
     */
    public static final int GRAMMAR_TODO_DONE = 22;
    /**
     * for back slash
     */
    public static final int GRAMMAR_BACKSLASH = 23;

    /**
     * for code
     */
    public static final int GRAMMAR_CODE = 30;
    /**
     * for unorder list
     */
    public static final int GRAMMAR_UNORDER_LIST = 31;
    /**
     * for order list
     */
    public static final int GRAMMAR_ORDER_LIST = 32;

    /**
     * for under line
     */
    public static final int GRAMMAR_UNDER_LINE = 33;

    /**
     * Constructor
     */
    protected AbsGrammarFactory() {
    }

    /**
     * Get horizontal rules grammar.
     * Grammar:
     * "***"
     * <p>
     * "---"
     * <p>
     * "***********"
     * <p>
     * "----------------"
     *
     * @param rxMDConfiguration configuration, need HorizontalRulesColor
     * @return the interface {@link IGrammar} for horizontal rules grammar
     */
    protected abstract IGrammar getHorizontalRulesGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get block quotes grammar.
     * Grammar:
     * "&gt; "
     *
     * @param rxMDConfiguration configuration, need BlockQuotesColor
     * @return the interface {@link IGrammar} for block quotes grammar
     */
    protected abstract IGrammar getBlockQuotesGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get to do grammar.
     * Grammar:
     * "- [] "
     *
     * @param rxMDConfiguration configuration, need TodoColor
     * @return the interface {@link IGrammar} for to do grammar
     */
    protected abstract IGrammar getTodoGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get to do for "done" grammar.
     * Grammar:
     * "- [x] "
     * <p>
     * "- [X] "
     *
     * @param rxMDConfiguration configuration, need TodoDoneColor
     * @return the interface {@link IGrammar} for to do for "done" grammar
     */
    protected abstract IGrammar getTodoDoneGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get order list grammar.
     * Grammar:
     * "1. "
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for to do for "done" grammar
     */
    protected abstract IGrammar getOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get unorder list grammar.
     * Grammar:
     * "* "
     * <p>
     * "+ "
     * <p>
     * "- "
     *
     * @param rxMDConfiguration configuration, need UnOrderListColor
     * @return the interface {@link IGrammar} for unorder list grammar
     */
    protected abstract IGrammar getUnOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get center align grammar.
     * It's not the real grammar in Markdown.
     * Grammar:
     * "[content]"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for center align grammar
     */
    protected abstract IGrammar getCenterAlignGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get header grammar.
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
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for header grammar
     */
    protected abstract IGrammar getHeaderGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get bold grammar.
     * Grammar:
     * "**content**"
     * "__content__"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for bold grammar
     */
    protected abstract IGrammar getBoldGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get underline grammar.
     * Grammar:
     * "\\ucontent\\u"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for underline grammar
     */
    protected abstract IGrammar getUnderLine(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get italic grammar.
     * Grammar:
     * "*content*"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for bold grammar
     */
    protected abstract IGrammar getItalicGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get inline code grammar.
     * Grammar:
     * "`content`"
     *
     * @param rxMDConfiguration configuration, need InlineCodeBgColor
     * @return the interface {@link IGrammar} for inline code grammar
     */
    protected abstract IGrammar getInlineCodeGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get strike through grammar.
     * Grammar:
     * "~~content~~"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for strike through grammar
     */
    protected abstract IGrammar getStrikeThroughGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get footnote grammar.
     * Grammar:
     * "content[^footnote]"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for footnote grammar
     */
    protected abstract IGrammar getFootnoteGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get image grammar.
     * Grammar:
     * "![image](http://image.jpg)"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for image grammar
     */
    protected abstract IGrammar getImageGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get hyper link grammar.
     * Grammar:
     * "[content](http://link.html)"
     *
     * @param rxMDConfiguration configuration, need nothing
     * @return the interface {@link IGrammar} for hyper link grammar
     */
    protected abstract IGrammar getHyperLinkGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get code grammar.
     * Grammar:
     * "```
     * content
     * ```"
     *
     * @param rxMDConfiguration configuration, need CodeBgColor
     * @return the interface {@link IGrammar} for code grammar
     */
    protected abstract IGrammar getCodeGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * Get back slash grammar.
     * Grammar:
     * "\"
     *
     * @param rxMDConfiguration configuration, need CodeBgColor
     * @return the interface {@link IGrammar} for back slash grammar
     */
    protected abstract IGrammar getBackslashGrammar(@NonNull RxMDConfiguration rxMDConfiguration);

    /**
     * parse content
     *
     * @param charSequence      content
     * @param rxMDConfiguration configuration
     * @return the content after parsing
     */
    @NonNull
    public abstract CharSequence parse(@NonNull CharSequence charSequence, @NonNull RxMDConfiguration rxMDConfiguration);
}
