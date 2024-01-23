package com.example.debatazo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
public class imagenCircular extends androidx.appcompat.widget.AppCompatImageView {
    public imagenCircular(Context context) {
        super(context);
    }

    public imagenCircular(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public imagenCircular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /*重写 onDraw 方法*/
    protected void onDraw(Canvas canvas) {
        /*检查imageView的Drawable对象*/
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        /*检查imageView的宽度和高度是否为0*/
        if (getWidth() == 0 || getHeight() == 0) return;

        /*使用getRoundedBitmap方法获取圆形Bitmap对象*/
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            bitmap = getBitmapFromVectorDrawable((VectorDrawable) drawable);
        }
        Bitmap circularbitmap = getRoundedBitmap(bitmap);
        /*在Canvas上绘制圆形Bitmap*/
        canvas.drawBitmap(circularbitmap, 0, 0, null);
    }

    /*获取圆形Bitmap对象*/
    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = (float) (bitmap.getWidth() / 2);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private Bitmap getBitmapFromVectorDrawable(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}

