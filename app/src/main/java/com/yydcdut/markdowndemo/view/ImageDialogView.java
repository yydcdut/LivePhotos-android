package com.yydcdut.markdowndemo.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yydcdut.markdowndemo.R;
import com.yydcdut.rxmarkdown.utils.UriParseCompat;

import java.io.File;

/**
 * Created by yuyidong on 16/7/20.
 */
public class ImageDialogView extends LinearLayout implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
    private static final int REQUEST_CAMERA = 10;
    private static final int REQUEST_GALLERY = 11;

    private static final String DEFAULT_PATH = "drawable://" + R.mipmap.ic_launcher;

    private int mCurrentCameraPictureIndex = 0;

    private ImageView mTargetImageView;
    private String mPath;

    private EditText mWidthEditText;
    private EditText mHeightEditText;
    private EditText mDescriptionEditText;

    public ImageDialogView(Context context) {
        super(context);
        init(context);
    }

    public ImageDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ImageDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_image, this, true);
        mTargetImageView = (ImageView) v.findViewById(R.id.img_image);
        mTargetImageView.setOnClickListener(this);
        mWidthEditText = (EditText) v.findViewById(R.id.edit_width);
        mHeightEditText = (EditText) v.findViewById(R.id.edit_height);
        mDescriptionEditText = (EditText) v.findViewById(R.id.edit_description);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mTargetImageView.getContext());
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheSize(50 * 1024 * 1024);
            config.tasksProcessingOrder(QueueProcessingType.LIFO);
            config.writeDebugLogs();
            ImageLoader.getInstance().init(config.build());
        }
    }

    @Override
    public void onClick(View v) {
        PopupMenu popup = new PopupMenu(getContext(), mTargetImageView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            mPath = "file:/" + getContext().getExternalCacheDir().getAbsolutePath() + File.separator + "tmp" + mCurrentCameraPictureIndex + ".jpg";
            ImageLoader.getInstance().displayImage(mPath, mTargetImageView);
            mCurrentCameraPictureIndex++;
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            mPath = "file:/" + UriParseCompat.getFilePathWithUri(uri, mTargetImageView.getContext());
            ImageLoader.getInstance().displayImage(mPath, mTargetImageView);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        } else {
            Log.e("yuyidong", "not activity");
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_gallery:
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activity.startActivityForResult(albumIntent, REQUEST_GALLERY);
                break;
            case R.id.action_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getContext().getExternalCacheDir().getAbsolutePath() + File.separator + "tmp" + mCurrentCameraPictureIndex + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                activity.startActivityForResult(intent, REQUEST_CAMERA);
                break;
        }
        return false;
    }

    public void clear() {
        ImageLoader.getInstance().displayImage(DEFAULT_PATH, mTargetImageView);
        mWidthEditText.setText("200");
        mHeightEditText.setText("200");
        mPath = DEFAULT_PATH;
    }

    public int getImageWidth() {
        return Integer.parseInt(mWidthEditText.getText().toString());
    }

    public int getImageHeight() {
        return Integer.parseInt(mHeightEditText.getText().toString());
    }

    public String getPath() {
        return mPath;
    }

    public String getDescription() {
        return mDescriptionEditText.getText().toString();
    }


}
