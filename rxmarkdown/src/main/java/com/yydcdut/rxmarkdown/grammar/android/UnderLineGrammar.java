package com.yydcdut.rxmarkdown.grammar.android;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;

import com.yydcdut.rxmarkdown.RxMDConfiguration;

import java.util.regex.Pattern;

public class UnderLineGrammar extends AbsAndroidGrammar {

    private static final String KEY_UNDEL_LINE = "\\u";

    protected static final String KEY_BACKSLASH_VALUE = BackslashGrammar.KEY_BACKSLASH + "\\u";

    public UnderLineGrammar(@NonNull final RxMDConfiguration rxMDConfiguration) {
        super(rxMDConfiguration);
    }

    @Override
    boolean isMatch(@NonNull final String text) {
        if (!text.contains(KEY_UNDEL_LINE)) {
            return false;
        }
        boolean match = false;
        Pattern pattern = Pattern.compile(".*[\\\\u].*[\\\\u].*");

        match |= pattern.matcher(text).matches();
        if (match) {
            return true;
        }

        return match;
    }

    @NonNull
    @Override
    SpannableStringBuilder encode(@NonNull final SpannableStringBuilder ssb) {
        int index;
        while (true) {
            String text = ssb.toString();
            index = text.indexOf(KEY_BACKSLASH_VALUE);
            if (index == -1) {
                break;
            }
            ssb.replace(index, index + KEY_BACKSLASH_VALUE.length(), BackslashGrammar.KEY_ENCODE);
        }
        return ssb;
    }

    @NonNull
    @Override
    SpannableStringBuilder format(@NonNull final SpannableStringBuilder ssb) {
        return parse(KEY_UNDEL_LINE, ssb.toString(), ssb);
    }

    @NonNull
    @Override
    SpannableStringBuilder decode(@NonNull final SpannableStringBuilder ssb) {
        int index;
        while (true) {
            String text = ssb.toString();
            index = text.indexOf(BackslashGrammar.KEY_ENCODE);
            if (index == -1) {
                break;
            }
            ssb.replace(index, index + BackslashGrammar.KEY_ENCODE.length(), KEY_BACKSLASH_VALUE);
        }
        return ssb;
    }

    /**
     * parse
     *
     * @param key  {@link BoldGrammar#KEY_BOLD} or {@link BoldGrammar#KEY_BOLD_1}
     * @param text the original content,the class type is {@link String}
     * @param ssb  the original content,the class type is {@link SpannableStringBuilder}
     * @return the content after parsing
     */
    private SpannableStringBuilder parse(@NonNull String key, @NonNull String text,
            @NonNull SpannableStringBuilder ssb) {
        int keyLength = key.length();
        SpannableStringBuilder tmp = new SpannableStringBuilder();
        String tmpTotal = text;
        while (true) {
            int positionHeader = findPosition(key, tmpTotal, ssb, tmp);
            if (positionHeader == -1) {
                tmp.append(tmpTotal.substring(0, tmpTotal.length()));
                break;
            }
            tmp.append(tmpTotal.substring(0, positionHeader));
            int index = tmp.length();
            tmpTotal = tmpTotal.substring(positionHeader + keyLength, tmpTotal.length());
            int positionFooter = findPosition(key, tmpTotal, ssb, tmp);
            if (positionFooter != -1) {
                ssb.delete(tmp.length(), tmp.length() + keyLength);
                tmp.append(tmpTotal.substring(0, positionFooter));
                ssb.setSpan(new UnderlineSpan(Parcel.obtain()), index, tmp.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.delete(tmp.length(), tmp.length() + keyLength);
            } else {
                tmp.append(key);
                tmp.append(tmpTotal.substring(0, tmpTotal.length()));
                break;
            }
            tmpTotal = tmpTotal.substring(positionFooter + keyLength, tmpTotal.length());
        }
        return ssb;
    }

    /**
     * find the position of next "\\u"
     * ignore the "\\u" in inline code grammar,
     *
     * @param tmpTotal the original content, the class type is {@link String}
     * @param ssb      the original content, the class type is {@link SpannableStringBuilder}
     * @param tmp      the content that has parsed
     * @return the next position of "\\u"
     */
    private int findPosition(@NonNull String key, @NonNull String tmpTotal,
            @NonNull SpannableStringBuilder ssb, @NonNull SpannableStringBuilder tmp) {
        String tmpTmpTotal = tmpTotal;
        int position = tmpTmpTotal.indexOf(key);
        if (position == -1) {
            return -1;
        } else {
            if (checkInInlineCode(ssb, tmp.length() + position, key.length())) {//key是否在inlineCode中
                StringBuilder sb = new StringBuilder(tmpTmpTotal.substring(0, position))
                        .append("$$").append(tmpTmpTotal
                                .substring(position + key.length(), tmpTmpTotal.length()));
                return findPosition(key, sb.toString(), ssb, tmp);
            } else {
                return position;
            }
        }
    }

}
