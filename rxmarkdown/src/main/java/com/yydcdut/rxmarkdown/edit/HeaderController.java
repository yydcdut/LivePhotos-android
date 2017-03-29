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
import android.text.style.RelativeSizeSpan;

import com.yydcdut.rxmarkdown.factory.AbsGrammarFactory;
import com.yydcdut.rxmarkdown.grammar.IGrammar;
import com.yydcdut.rxmarkdown.grammar.edit.EditGrammarFacade;

import java.util.List;

/**
 * RxMDEditText, header controller.
 * <p>
 * Created by yuyidong on 16/7/21.
 */
public class HeaderController extends AbsEditController {

    private static final String KEY = "#";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        super.beforeTextChanged(s, start, before, after);
        if (before == 0 || mRxMDConfiguration == null) {
            return;
        }
        String deleteString = s.subSequence(start, start + before).toString();
        String beforeString = null;
        String afterString = null;
        if (start > 0) {
            beforeString = s.subSequence(start - 1, start).toString();
        }
        if (start + before + 1 <= s.length()) {
            afterString = s.subSequence(start + before, start + before + 1).toString();
        }
        //#12# ss(##12 ss) --> ## ss
        if (deleteString.contains(KEY) || KEY.equals(beforeString) || KEY.equals(afterString)) {
            shouldFormat = true;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {
        if (mRxMDConfiguration == null && !(s instanceof Editable)) {
            return;
        }
        if (shouldFormat) {
            format((Editable) s, start);
            return;
        }
        if (after == 0) {
            return;
        }
        String addString;
        String beforeString = null;
        String afterString = null;
        addString = s.subSequence(start, start + after).toString();
        if (start + 1 <= s.length()) {
            afterString = s.subSequence(start, start + 1).toString();
        }
        if (start > 0) {
            beforeString = s.subSequence(start - 1, start).toString();
        }
        //## ss --> #12# ss(##12 ss)
        if (addString.contains(KEY) || KEY.equals(beforeString) || KEY.equals(afterString)) {
            format((Editable) s, start);
        } else {
            int lineFirstCharPosition = EditUtils.findBeforeNewLineChar(s, start);
            if (s.subSequence(lineFirstCharPosition + 1, lineFirstCharPosition + 2).toString().equals(KEY)) {
                format((Editable) s, lineFirstCharPosition + 1);
            }
        }
    }

    private void format(Editable editable, int start) {
        EditUtils.removeSpans(editable, start, RelativeSizeSpan.class);
        IGrammar iGrammar = EditGrammarFacade.getAndroidGrammar(AbsGrammarFactory.GRAMMAR_HEADER_LINE, mRxMDConfiguration);
        List<EditToken> editTokenList = EditUtils.getMatchedEditTokenList(editable, iGrammar.format(editable), start);
        EditUtils.setSpans(editable, editTokenList);
    }
}
