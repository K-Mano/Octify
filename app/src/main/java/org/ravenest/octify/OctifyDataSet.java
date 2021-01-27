package org.ravenest.octify;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;

public class OctifyDataSet {
    // データセット(デフォルト)
    static class DefaultDataSet {
        DefaultDataSet(String name){
            this.name = name;
        }
        private String name;
        public String getName() {
            return name;
        }
    }

    static class DiskInfoDataSet extends DefaultDataSet{
        DiskInfoDataSet(File path, String name, float max, float used, boolean isPrimary, boolean isRemovable){
            //デフォルトコンストラクタ
            super(name);

            this.path = path;
            this.max   = max;
            this.used  = used;

            percentage = (used /max)*100;

            this.isPrimary   = isPrimary;
            this.isRemovable = isRemovable;
        }
        private String name;
        // ドライブのパス
        private File path;
        // ストレージ容量
        private float max;
        // ストレージ使用量
        private float used;
        // ストレージ使用率
        private float percentage;
        // プライマリストレージデバイス
        private boolean isPrimary;
        // リムーバブルストレージデバイス
        private boolean isRemovable;

        public File getPath() {
            return path;
        }

    }

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

    public boolean isExternalStorageWritable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
