package com.yydcdut.markdowndemo.controller;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yydcdut.markdowndemo.view.ImageDialogView;
import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMDEditText;

/**
 * Created by yuyidong on 16/7/20.
 */
public class ImageController {
    private ImageDialogView mImageDialogView;
    private RxMDEditText mRxMDEditText;
    private RxMDConfiguration mRxMDConfiguration;

    private AlertDialog mAlertDialog;

    public ImageController(RxMDEditText rxMDEditText, RxMDConfiguration rxMDConfiguration) {
        mRxMDEditText = rxMDEditText;
        mRxMDConfiguration = rxMDConfiguration;
        mImageDialogView = new ImageDialogView(mRxMDEditText.getContext());
        mImageDialogView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void doImage() {
        if (mAlertDialog == null) {
            initDialog();
        }
        mImageDialogView.clear();
        mAlertDialog.show();
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        mImageDialogView.handleResult(requestCode, resultCode, data);
    }

    private void initDialog() {
        mAlertDialog = new AlertDialog.Builder(mRxMDEditText.getContext())
                .setView(mImageDialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WindowManager windowManager = (WindowManager) mImageDialogView.getContext().getSystemService(Context.WINDOW_SERVICE);
                        DisplayMetrics dm = new DisplayMetrics();
                        windowManager.getDefaultDisplay().getMetrics(dm);

                        dialog.dismiss();
                        Bitmap bitmap = getBitmap(mImageDialogView.getPath().replace("file://", ""));
                        if(bitmap != null) {
//                            int width = (int) (bitmap.getWidth() * dm.scaledDensity);
//                            int height = (int) (bitmap.getHeight() * dm.scaledDensity);

                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            String path = mImageDialogView.getPath();
                            String description = mImageDialogView.getDescription();
                            doRealImage(width, height, path, description);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
    }

    private int resize(int initValue, int maxValue) {
        int temp = 1;
        for(int i = 1; i < 10; i++) {
            temp = initValue * i;
            if(temp > maxValue) {
                return i - 1;
            }
        }
        return 1;
    }

    /**
     * 获取bitmap
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(String filePath) {
        if (isSpace(filePath)) return null;
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    private void doRealImage(int width, int height, String path, String description) {
        int start = mRxMDEditText.getSelectionStart();
        description = "e";
        if (TextUtils.isEmpty(description)) {
            mRxMDEditText.getText().insert(start, "![](" + path + "/" + width + "$" + height + ")\n");
            mRxMDEditText.setSelection(start + 2);
        } else {
            mRxMDEditText.getText().insert(start, "![" + description + "](" + path + "/" + width + "$" + height + ")\n");
        }
    }

}
