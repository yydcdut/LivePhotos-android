package com.yydcdut.rxmarkdown.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Uri parse util. This util compat to Nougat.
 */

public class UriParseCompat {

    private static final String TAG = UriParseCompat.class.getName();

    /**
     * Return a content URI for a given {@link Uri}
     */
    public static Uri convertFileUriToFileProviderUri(Context context, Uri uri) {
        if(uri == null) {
            return getUriForFile(context, new File(uri.getPath()));
        }
        if(ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return null;
        }
        return uri;
    }

    /**
     * Return a content URI for a given {@link File}
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProviderName(context), file);
    }

    /**
     * Return a default configuration content URI.
     */
    public static Uri getTempUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file = new File(Environment.getExternalStorageDirectory(), "/images/" + timeStamp + ".jpg");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * Return a content URI by specify path.
     */
    public static Uri getTempUri(Context context, String path) {
        File file = new File(path);
        return getTempUri(context, file);
    }

    /**
     * Return a content URI by specify file.
     */
    public static Uri getTempUri(Context context, File file) {
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return getUriForFile(context,file);
    }

    /**
     * Return {@link FileProvider} name
     */
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * Reverse parse uri to path, this uri provided by {@link UriParseCompat}
     */
    public static String parseOwnUriToPath(Context context, Uri uri) {
        if(uri == null) {
            return null;
        }
        String path;
        if(TextUtils.equals(uri.getAuthority(), getFileProviderName(context))) {
            path = new File(uri.getPath().replace("picture/","")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    /**
     * Return file path with URI
     */
    public static String getFilePathWithUri(Uri uri, Context context) throws RuntimeException {
        if(uri == null) {
            throw new RuntimeException("uri is null, activity may have been recovered?");
        }
        File picture = getFileWithUri(uri, context);
        String picturePath = picture == null ? null : picture.getPath();
        return picturePath;
    }

    /**
     * Return file object with URI
     */
    public static File getFileWithUri(Uri uri, Context context) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if(ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            /**
             * Query the given URI
             */
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if(columnIndex >= 0) {
                picturePath = cursor.getString(columnIndex);
            } else {
                picturePath = parseOwnUriToPath(context, uri);
            }
            cursor.close();
        } else if(ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }
}
