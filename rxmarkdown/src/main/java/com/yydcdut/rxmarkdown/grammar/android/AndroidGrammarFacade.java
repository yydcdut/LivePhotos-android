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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.grammar.IGrammar;

import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_BACKSLASH;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_BLOCK_QUOTES;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_BOLD;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_CENTER_ALIGN;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_CODE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_FOOTNOTE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_HEADER_LINE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_HORIZONTAL_RULES;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_HYPERLINK;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_IMAGE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_INLINE_CODE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_ITALIC;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_ORDER_LIST;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_STRIKE_THROUGH;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_TODO;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_TODO_DONE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_UNDER_LINE;
import static com.yydcdut.rxmarkdown.factory.AbsGrammarFactory.GRAMMAR_UNORDER_LIST;

/**
 * Provide grammars.
 * <p>
 * Created by yuyidong on 16/5/12.
 */
public class AndroidGrammarFacade {

    private AndroidGrammarFacade() {
    }

    /**
     * Provide grammars.
     *
     * @param grammar           {@link com.yydcdut.rxmarkdown.factory.AbsGrammarFactory}
     * @param rxMDConfiguration configuration
     * @return the interface {@link IGrammar}
     */
    public static IGrammar getAndroidGrammar(@IntRange(from = -1, to = 33) int grammar, @NonNull RxMDConfiguration rxMDConfiguration) {
        switch (grammar) {
            case GRAMMAR_HORIZONTAL_RULES:
                return new HorizontalRulesGrammar(rxMDConfiguration);
            case GRAMMAR_BLOCK_QUOTES:
                return new BlockQuotesGrammar(rxMDConfiguration);
            case GRAMMAR_CENTER_ALIGN:
                return new CenterAlignGrammar(rxMDConfiguration);
            case GRAMMAR_HEADER_LINE:
                return new HeaderGrammar(rxMDConfiguration);
            case GRAMMAR_BOLD:
                return new BoldGrammar(rxMDConfiguration);
            case GRAMMAR_ITALIC:
                return new ItalicGrammar(rxMDConfiguration);
            case GRAMMAR_INLINE_CODE:
                return new InlineCodeGrammar(rxMDConfiguration);
            case GRAMMAR_STRIKE_THROUGH:
                return new StrikeThroughGrammar(rxMDConfiguration);
            case GRAMMAR_FOOTNOTE:
                return new FootnoteGrammar(rxMDConfiguration);
            case GRAMMAR_IMAGE:
                return new ImageGrammar(rxMDConfiguration);
            case GRAMMAR_HYPERLINK:
                return new HyperLinkGrammar(rxMDConfiguration);
            case GRAMMAR_TODO:
                return new TodoGrammar(rxMDConfiguration);
            case GRAMMAR_TODO_DONE:
                return new TodoDoneGrammar(rxMDConfiguration);
            case GRAMMAR_CODE:
                return new CodeGrammar(rxMDConfiguration);
            case GRAMMAR_UNORDER_LIST:
                return new UnOrderListGrammar(rxMDConfiguration);
            case GRAMMAR_ORDER_LIST:
                return new OrderListGrammar(rxMDConfiguration);
            case GRAMMAR_BACKSLASH:
                return new BackslashGrammar(rxMDConfiguration);
            case GRAMMAR_UNDER_LINE:
                return new UnderLineGrammar(rxMDConfiguration);
            default:
                return new NormalGrammar(rxMDConfiguration);
        }
    }
}

