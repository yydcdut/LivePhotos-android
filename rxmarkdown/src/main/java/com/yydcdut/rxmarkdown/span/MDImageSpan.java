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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.view.View;

import com.yydcdut.rxmarkdown.drawable.ForwardingDrawable;
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader;
import com.yydcdut.rxmarkdown.utils.ImageUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * image grammar span
 * <p>
 * Created by yuyidong on 16/5/16.
 */
public class MDImageSpan extends DynamicDrawableSpan {

    private static Pattern sImageUrlPattern = Pattern.compile("^(.*?)/(\\d+)\\$(\\d+)$");

    private String mImageUri;
    private Drawable mPlaceHolder;
    private Drawable mFinalDrawable;
    private ForwardingDrawable mActualDrawable;
    private boolean mIsAttached;
    private View mAttachedView;
    private boolean mIsRequestSubmitted = false;

    private RxMDImageLoader mRxMDImageLoader;

    private int widthPixels;

    private static Drawable createEmptyDrawable(int width, int height) {
        ColorDrawable d = new ColorDrawable(Color.TRANSPARENT);
        d.setBounds(0, 0, width , height);
        return d;
    }

    /**
     * Constructor
     *
     * @param uri             the image url
     * @param width           the display width
     * @param height          the display height
     * @param rxMDImageLoader loader
     */
    public MDImageSpan(String uri, int width, int height, int widthPixels, RxMDImageLoader rxMDImageLoader) {
        this(uri, createEmptyDrawable(getSize(uri, width, height)[0], getSize(uri, width, height)[1]), rxMDImageLoader);
        this.widthPixels = widthPixels;
    }

    /**
     * Constructor
     *
     * @param uri             the image url
     * @param placeHolder     the place holder drawable
     * @param rxMDImageLoader loader
     */
    private MDImageSpan(String uri, Drawable placeHolder, RxMDImageLoader rxMDImageLoader) {
        super(ALIGN_BOTTOM);
        mRxMDImageLoader = rxMDImageLoader;
        mImageUri = uri;
        mPlaceHolder = placeHolder;
        mActualDrawable = new ForwardingDrawable(mPlaceHolder);
        Rect bounds = mPlaceHolder.getBounds();
        if (bounds.right == 0 || bounds.bottom == 0) {
            mActualDrawable.setBounds(0, 0, mPlaceHolder.getIntrinsicWidth(), mPlaceHolder.getIntrinsicHeight());
        } else {
            mActualDrawable.setBounds(bounds);
        }
    }

    @Override
    public Drawable getDrawable() {
        return mActualDrawable;
    }

    /**
     * invoke when view created
     *
     * @param view the view
     */
    public void onAttach(@NonNull View view) {
        mIsAttached = true;
        if (mAttachedView != view) {
            mActualDrawable.setCallback(null);
            if (mAttachedView != null) {
                throw new IllegalStateException("has been attached to view:" + mAttachedView);
            }
            mAttachedView = view;
            mActualDrawable.setCallback(mAttachedView);
        }
        if (!mIsRequestSubmitted) {
            submitRequest();
        }
    }



    private void submitRequest() {
        mIsRequestSubmitted = true;
        Observable.just(mImageUri)
                .observeOn(Schedulers.io())
                .map(new Func1<String, byte[]>() {
                    @Override
                    public byte[] call(String url) {
                        byte[] bytes = null;
                        try {
                            bytes = mRxMDImageLoader.loadSync(getUrl(url));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return bytes;
                    }
                })
                .map(new Func1<byte[], Drawable>() {
                    @Override
                    public Drawable call(byte[] bytes) {
                        if (bytes == null) {
                            return mPlaceHolder;
                        }
                        return getDrawable(bytes);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        setImageWithIntrinsicBounds(drawable);
                    }
                });
    }

    private void setImageWithIntrinsicBounds(@NonNull Drawable drawable) {
        if (mFinalDrawable != drawable && drawable != null) {
            mActualDrawable.setCurrent(drawable);
            mFinalDrawable = drawable;
        }
    }

    private BitmapDrawable createBitmapDrawable(Bitmap bitmap) {
        BitmapDrawable drawable;
        if (mAttachedView != null) {
            final Context context = mAttachedView.getContext();
            drawable = new BitmapDrawable(context.getResources(), bitmap);
        } else {
            drawable = new BitmapDrawable(null, bitmap);
        }
        return drawable;
    }

    private static int calculate(@NonNull BitmapFactory.Options options, int expectWidth, int expectHeight) {
        int sampleSize = 1;
        while (options.outHeight / sampleSize > expectWidth || options.outWidth / sampleSize > expectHeight) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private Drawable getDrawable(@NonNull byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        BitmapFactory.Options calculateOptions = new BitmapFactory.Options();
        calculateOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, calculateOptions);
        int expectWidth = mActualDrawable.getIntrinsicWidth();
        int expectHeight = mActualDrawable.getIntrinsicHeight();
        int sampleSize = 1;
        if (expectWidth >= 0 && expectHeight >= 0) {
            sampleSize = calculate(calculateOptions, expectWidth, expectHeight);
        } else if (mPlaceHolder.getBounds().width() >= 0 && mPlaceHolder.getBounds().height() >= 0) {
            Rect rect = mPlaceHolder.getBounds();
            sampleSize = calculate(calculateOptions, rect.width(), rect.height());
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        //----Scale to fit screen with and height----
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int targetHeight = (widthPixels * bitmapHeight) / bitmapWidth;
        float xScale = ((float) widthPixels) / bitmapWidth;
        float yScale = ((float) targetHeight) / bitmapHeight;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        int scaleWith = scaledBitmap.getWidth(); // re-use
        int scaleHeight = scaledBitmap.getHeight(); // re-use

        //----invalid-------
        mPlaceHolder = createEmptyDrawable(scaleWith, scaleHeight);
        mActualDrawable.setBounds(mPlaceHolder.getBounds());
        mActualDrawable.invalidateSelf();
//
//        mPlaceHolder = createEmptyDrawable(width, height);
//        mActualDrawable = new ForwardingDrawable(mPlaceHolder);
//        Rect bounds = mPlaceHolder.getBounds();
//        if (bounds.right == 0 || bounds.bottom == 0) {
//            mActualDrawable.setBounds(0, 0, mPlaceHolder.getIntrinsicWidth(), mPlaceHolder.getIntrinsicHeight());
//        } else {
//            mActualDrawable.setBounds(bounds);
//        }

//        mActualDrawable.setCallback(mAttachedView);

        return createBitmapDrawable(bitmap);
    }

    /**
     * invoke when view destroyed
     */
    public void onDetach() {
        if (!mIsAttached) {
            return;
        }
        mActualDrawable.setCallback(null);
        mAttachedView = null;
        mActualDrawable.setCurrent(mPlaceHolder);
    }

    /**
     * Parse the width and height in sourceUrl, Return the int array.
     */
    @NonNull
    private static int[] getSize(String sourceUrl, int defaultWidth, int defaultHeight) {
        Matcher m = sImageUrlPattern.matcher(sourceUrl);
        int[] size = new int[]{defaultWidth, defaultHeight};
        if (m.find()) {
            if (TextUtils.isDigitsOnly(m.group(2))) {
                size[0] = Integer.valueOf(m.group(2));
            }
            if (TextUtils.isDigitsOnly(m.group(3))) {
                size[1] = Integer.valueOf(m.group(3));
            }
        }
        return size;
    }

    /**
     * Return the url in sourceUrl
     */
    @NonNull
    private static String getUrl(String sourceUrl) {
        Matcher m = sImageUrlPattern.matcher(sourceUrl);
        if (m.find()) {
            return m.group(1);
        }
        return sourceUrl;
    }

}
