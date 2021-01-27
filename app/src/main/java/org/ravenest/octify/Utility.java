package org.ravenest.octify;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Utility {
    public static Bitmap drawableToBitmap(Drawable drawable, int height, int width, int centerX, int centerY, int color){

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int left = centerX-(width/2);
        int top = centerY-(height/2);

        Bitmap bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setBounds(left, top, left+width, top+height);
        drawable.setColorFilter(color, mode);
        drawable.draw(canvas);

        return bitmap;
    }

    public String dateToString(Date date, String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }
    public Date stringToDate(String dateStr, String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public Long regionConverter(Long time, String format, Locale locale){
        SimpleDateFormat sdf = new SimpleDateFormat(format ,locale);
        Date date = new Date(time);
        try {
            date = sdf.parse(dateToString(date, format, locale));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
