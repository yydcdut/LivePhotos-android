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
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader;
import com.yydcdut.rxmarkdown.span.MDImageSpan;

import java.util.regex.Pattern;

import static com.yydcdut.rxmarkdown.grammar.android.BackslashGrammar.KEY_BACKSLASH;

/**
 * The implementation of grammar for image.
 * Grammar:
 * "![image](http://image.jpg)"
 * <p>
 * Created by yuyidong on 16/5/15.
 */
class ImageGrammar extends AbsAndroidGrammar {

    protected static final String KEY_0_IMAGE = "![";
    protected static final String KEY_1_IMAGE = "](";
    protected static final String KEY_2_IMAGE = ")";

    private static final String PLACE_HOLDER_2 = " ";

    protected static final String KEY_BACKSLASH_VALUE_0 = KEY_BACKSLASH + "!";
    protected static final String KEY_BACKSLASH_VALUE_2 = KEY_BACKSLASH + "]";
    protected static final String KEY_BACKSLASH_VALUE_4 = KEY_BACKSLASH + KEY_2_IMAGE;

    private int[] mSize;
    private RxMDImageLoader mRxMDImageLoader;
    private int widthPixels;
    private int heightPixels;

    ImageGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
        mSize = rxMDConfiguration.getDefaultImageSize();
        widthPixels = rxMDConfiguration.getWidthPixels();
        heightPixels = rxMDConfiguration.getHeightPixels();
        mRxMDImageLoader = rxMDConfiguration.getRxMDImageLoader();
    }

    @Override
    boolean isMatch(@NonNull String text) {
        if (!(text.contains(KEY_0_IMAGE) && text.contains(KEY_1_IMAGE) && text.contains(KEY_2_IMAGE))) {
            return false;
        }
        Pattern pattern = Pattern.compile(".*[!\\[]{1}.*[\\](]{1}.*[)]{1}.*");
        return pattern.matcher(text).matches();
    }

    @NonNull
    @Override
    SpannableStringBuilder encode(@NonNull SpannableStringBuilder ssb) {
        int index0;
        while (true) {
            String text = ssb.toString();
            index0 = text.indexOf(KEY_BACKSLASH_VALUE_0);
            if (index0 == -1) {
                break;
            }
            ssb.replace(index0, index0 + KEY_BACKSLASH_VALUE_0.length(), BackslashGrammar.KEY_ENCODE);
        }
        int index2;
        while (true) {
            String text = ssb.toString();
            index2 = text.indexOf(KEY_BACKSLASH_VALUE_2);
            if (index2 == -1) {
                break;
            }
            ssb.replace(index2, index2 + KEY_BACKSLASH_VALUE_2.length(), BackslashGrammar.KEY_ENCODE_2);
        }
        int index4;
        while (true) {
            String text = ssb.toString();
            index4 = text.indexOf(KEY_BACKSLASH_VALUE_4);
            if (index4 == -1) {
                break;
            }
            ssb.replace(index4, index4 + KEY_BACKSLASH_VALUE_4.length(), BackslashGrammar.KEY_ENCODE_4);
        }
        return ssb;
    }

    @Override
    SpannableStringBuilder format(@NonNull SpannableStringBuilder ssb) {
        String text = ssb.toString();
        return parse(text, ssb);
    }

    @NonNull
    @Override
    SpannableStringBuilder decode(@NonNull SpannableStringBuilder ssb) {
        int index0;
        while (true) {
            String text = ssb.toString();
            index0 = text.indexOf(BackslashGrammar.KEY_ENCODE);
            if (index0 == -1) {
                break;
            }
            ssb.replace(index0, index0 + BackslashGrammar.KEY_ENCODE.length(), KEY_BACKSLASH_VALUE_0);
        }
        int index2;
        while (true) {
            String text = ssb.toString();
            index2 = text.indexOf(BackslashGrammar.KEY_ENCODE_2);
            if (index2 == -1) {
                break;
            }
            ssb.replace(index2, index2 + BackslashGrammar.KEY_ENCODE_2.length(), KEY_BACKSLASH_VALUE_2);
        }
        int index4;
        while (true) {
            String text = ssb.toString();
            index4 = text.indexOf(BackslashGrammar.KEY_ENCODE_4);
            if (index4 == -1) {
                break;
            }
            ssb.replace(index4, index4 + BackslashGrammar.KEY_ENCODE_4.length(), KEY_BACKSLASH_VALUE_4);
        }
        return ssb;
    }

    /**
     * parse
     *
     * @param text the original content,the class type is {@link String}
     * @param ssb  the original content,the class type is {@link SpannableStringBuilder}
     * @return the content after parsing
     */
    @NonNull
    private SpannableStringBuilder parse(@NonNull String text, @NonNull SpannableStringBuilder ssb) {
        SpannableStringBuilder tmp = new SpannableStringBuilder();
        String tmpTotal = text;
        while (true) {
            int position4Key0 = tmpTotal.indexOf(KEY_0_IMAGE);
            int position4Key1 = tmpTotal.indexOf(KEY_1_IMAGE);
            int position4Key2 = tmpTotal.indexOf(KEY_2_IMAGE);
            if (position4Key0 == -1 || position4Key1 == -1 || position4Key2 == -1) {
                break;
            }
            if (position4Key0 < position4Key1 && position4Key1 < position4Key2) {
                //处理aa![bb![b](cccc)dddd
                int tmpCenter = tmpTotal.indexOf(KEY_1_IMAGE);
                String tmpLeft = tmpTotal.substring(0, tmpCenter);
                //正常流程
                int positionHeader = tmpLeft.lastIndexOf(KEY_0_IMAGE);
                tmp.append(tmpTotal.substring(0, positionHeader));
                int index = tmp.length();
                tmpTotal = tmpTotal.substring(positionHeader + KEY_0_IMAGE.length(), tmpTotal.length());
                int positionCenter = tmpTotal.indexOf(KEY_1_IMAGE);
                ssb.delete(tmp.length(), tmp.length() + KEY_0_IMAGE.length());
                tmp.append(tmpTotal.substring(0, positionCenter));
                tmpTotal = tmpTotal.substring(positionCenter + KEY_1_IMAGE.length(), tmpTotal.length());
                int positionFooter = tmpTotal.indexOf(KEY_2_IMAGE);
                String link = tmpTotal.substring(0, positionFooter);
                ssb.setSpan(new MDImageSpan(link, mSize[0], mSize[1], widthPixels, mRxMDImageLoader), index, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.delete(tmp.length(), tmp.length() + KEY_1_IMAGE.length() + link.length() + KEY_2_IMAGE.length());
                tmpTotal = tmpTotal.substring(positionFooter + KEY_2_IMAGE.length(), tmpTotal.length());
            } else if (position4Key0 < position4Key1 && position4Key0 < position4Key2 && position4Key2 < position4Key1) {
                //111![22)22](33333)
                tmpTotal = replaceFirstOne(tmpTotal, KEY_2_IMAGE, PLACE_HOLDER_2);
            } else if (position4Key1 < position4Key0 && position4Key1 < position4Key2) {
                //](在最前面的情况 111](2222![333)4444  1111](2222)3333![4444
                tmp.append(tmpTotal.substring(0, position4Key1 + KEY_1_IMAGE.length()));
                tmpTotal = tmpTotal.substring(position4Key1 + KEY_1_IMAGE.length(), tmpTotal.length());
            } else if (position4Key2 < position4Key0 && position4Key2 < position4Key1) {
                //)在最前面的情况 111)2222](333![4444  1111)2222![3333](4444
                tmp.append(tmpTotal.substring(0, position4Key2 + KEY_2_IMAGE.length()));
                tmpTotal = tmpTotal.substring(position4Key2 + KEY_2_IMAGE.length(), tmpTotal.length());
            }
        }
        return ssb;
    }

    /**
     * replace the key words
     *
     * @param content     the original content
     * @param target      the key words
     * @param replacement the replacement string
     * @return
     */
    @NonNull
    private String replaceFirstOne(@NonNull String content, @NonNull String target, @NonNull String replacement) {
        if (target == null) {
            throw new NullPointerException("target == null");
        }
        if (replacement == null) {
            throw new NullPointerException("replacement == null");
        }
        int matchStart = content.indexOf(target, 0);
        if (matchStart == -1) {
            return content;
        }
        int contentLength = content.length();
        int targetLength = target.length();
        if (targetLength == 0) {
            int resultLength = contentLength + (contentLength + 1) * replacement.length();
            StringBuilder result = new StringBuilder(resultLength);
            result.append(replacement);
            for (int i = 0; i != contentLength; ++i) {
                result.append(content.charAt(i));
                result.append(replacement);
            }
            return result.toString();
        }
        StringBuilder result = new StringBuilder(contentLength);
        for (int i = 0; i < matchStart; ++i) {
            result.append(content.charAt(i));
        }
        result.append(replacement);
        int over = matchStart + targetLength;
        for (int i = over; i < contentLength; ++i) {
            result.append(content.charAt(i));
        }
        return result.toString();
    }
}
