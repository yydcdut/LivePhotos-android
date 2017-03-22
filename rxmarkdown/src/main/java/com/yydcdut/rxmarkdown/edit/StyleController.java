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

import android.graphics.Typeface;
import android.text.Editable;
import android.text.style.StyleSpan;

import com.yydcdut.rxmarkdown.factory.AbsGrammarFactory;
import com.yydcdut.rxmarkdown.grammar.IGrammar;
import com.yydcdut.rxmarkdown.grammar.edit.EditGrammarFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * RxMDEditText, bold and italic controller.
 * <p>
 * Created by yuyidong on 16/7/21.
 */
public class StyleController extends AbsEditController {

    private static final String KEY = "*";
    private static final String KEY_1 = "_";

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
        //*11*ss** --> **ss**
        if (deleteString.contains(KEY) || KEY.equals(beforeString) || KEY.equals(afterString) ||
                deleteString.contains(KEY_1) || KEY_1.equals(beforeString) || KEY_1.equals(afterString)) {
            shouldFormat = true;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {
        if (mRxMDConfiguration == null || !(s instanceof Editable)) {
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
        StyleSpan[] beforeSpan = null;
        addString = s.subSequence(start, start + after).toString();
        if (start + 1 <= s.length()) {
            afterString = s.subSequence(start, start + 1).toString();
        }
        if (start > 0) {
            beforeString = s.subSequence(start - 1, start).toString();
            beforeSpan = ((Editable) s) .getSpans(start-1, start, StyleSpan.class);
        }
        //**ss** --> *11*ss**
        if (addString.contains(KEY) || KEY.equals(beforeString) || KEY.equals(afterString)
                || addString.contains(KEY_1) || KEY_1.equals(beforeString) || KEY_1.equals(afterString)
                || (beforeSpan !=null && beforeSpan.length > 0 && (beforeSpan[0].getStyle() > Typeface.NORMAL && beforeSpan[0].getStyle() <= Typeface.BOLD_ITALIC))){
            format((Editable) s, start);
        }
    }

    private void format(Editable editable, int start) {
        EditUtils.removeSpans(editable, start, StyleSpan.class);
        IGrammar boldGrammar = EditGrammarFacade.getAndroidGrammar(AbsGrammarFactory.GRAMMAR_BOLD, mRxMDConfiguration);
        IGrammar italicGrammar = EditGrammarFacade.getAndroidGrammar(AbsGrammarFactory.GRAMMAR_ITALIC, mRxMDConfiguration);
        List<EditToken> editTokenList = new ArrayList<>();
        editTokenList.addAll(EditUtils.getMatchedEditTokenList(editable, boldGrammar.format(editable), start));
        editTokenList.addAll(EditUtils.getMatchedEditTokenList(editable, italicGrammar.format(editable), start));
        EditUtils.setSpans(editable, editTokenList);
    }
}
