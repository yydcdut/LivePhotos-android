package com.yydcdut.rxmarkdown.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * Scale to bitmap
 */
public class ImageUtils {

    public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
        //计算最佳缩放倍数,以填充宽高为目标
        float scaleX = (float) width / bitmap.getWidth();
        float scaleY = (float) height / bitmap.getHeight();
        float bestScale = scaleX > scaleY ? scaleX : scaleY;
        //以填充高度的前提下，计算最佳缩放倍数
        float subX = (width - bitmap.getWidth() * bestScale) / 2;
        float subY = (height - bitmap.getHeight() * bestScale) / 2;

        Matrix imgMatrix = new Matrix();
        //缩放最佳大小
        imgMatrix.postScale(bestScale, bestScale);
        //移动到居中位置显示
        imgMatrix.postTranslate(subX, subY);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0 , 0, width, height, imgMatrix, true);
        return resizeBitmap;
    }

}
