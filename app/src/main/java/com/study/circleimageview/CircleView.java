package com.study.circleimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class CircleView extends View {
    private Bitmap bitmap;
    private float pictureHeight,pictureWidth;
    private BitmapFactory.Options options;
    private float left,top,radius;
    private Paint paint;
    private float scale ;
    private Path path;
    private Path path2;
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context);
        options = new BitmapFactory.Options();
        paint = new Paint();
        //  画笔颜色
        paint.setColor(Color.RED);
        //  填充风格，即充满绘制区域还是只绘制边框,STROKE:只绘制边框
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        path2 = new Path();
        decodePicture();
    }
    public void decodePicture(){
        //  不把图片加载进内存的情况下获取宽高
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(),R.drawable.jianli,options);
        pictureWidth = options.outWidth;
        pictureHeight = options.outHeight;

        //  单位转化
        left = dp2px(pictureWidth)/2;
        top = dp2px(pictureHeight)/2;
        radius = dp2px(Math.min(pictureHeight,pictureWidth))/2;
        /*
        *   注意：path是只绘制裁剪区域，path2是绘制裁剪区域外的
        * */
        //  为path添加形状，Path.Direction.CCW：顺时针方向绘制，CW为逆时针方向
        path.addCircle(left,top,radius,Path.Direction.CCW);
        path2.setFillType(Path.FillType.INVERSE_WINDING);
        path2.addCircle(left , top, radius, Path.Direction.CCW);
        //  解码图片
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.jianli);
    }
    //  将dp转化成为px
    /*
    *   在安卓中，图片是以dp的形式保存的，但是在显示时是以px为单位
    *       而如果要截取显示后的图片的圆形，有两种方法：1.获取偏显示后的宽高
    *       2.将dp转化成为px。这里采用的是第二种
    * */
    private int dp2px(float dp){
        /*
        *   获取图片的原始宽高和显示后的比例
        * 内存优化：获取scale的步骤应该放于构造函数中，从而可以避免在
        *   该方法被调用时仿佛去获取图片比例，本身上也是一种内存优化的方式
        * */
        scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp*scale + 0.5f);
    }
    /*
    *   安卓中绘制方法有多个，有绘制背景的，有绘制前景的，而ondraw只是负责主题的绘制，
    *   在实际的开发中，大多数都是主题的绘制，所以一般都是重写了ondraw方法
    * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        *   注：save()和restore()是一对方法，如果在裁剪之后不进行恢复，那么之后的所有绘制都会
        *       按照裁剪的标准，从而失去了我们想要的效果
        * */
        canvas.save();
        //  将图片安卓path进行裁剪，本样式为圆形裁剪

        /*
        *   注意：path：画圆里面的
        *        path2：画圆外面的
        * */
        canvas.clipPath(path2);
        //  绘制图片
        canvas.drawBitmap(bitmap,0,0,paint);
        canvas.restore();
        //  为图片添加边框线
        canvas.drawCircle(left,top,radius,paint);
    }
}