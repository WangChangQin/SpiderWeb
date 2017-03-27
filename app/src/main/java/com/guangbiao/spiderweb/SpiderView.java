package com.guangbiao.spiderweb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class SpiderView extends View {
    //雷达区画笔
    private Paint mainPaint;
    //文本画笔
    private Paint textPaint;
    //数据区画笔
    private Paint valuePaint;
    private int count = 6;
    private float angle = (float) (2 * Math.PI / count);
    private float radius;
    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    //标题文字
    private List<String> titles;
    //各维度分值
    private List<Double> data;
    //数据最大值
    private float maxValue = 100;

    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mainPaint = new Paint();
        mainPaint.setColor(Color.BLACK);
        mainPaint.setAntiAlias(true);/**抗锯齿*/
        mainPaint.setStrokeWidth(1);
        mainPaint.setStyle(Paint.Style.STROKE);/**描边*/

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);

        valuePaint=new Paint();
        valuePaint.setColor(Color.RED);
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL);

        titles = new ArrayList<>(count);
        titles.add("JAVA");
        titles.add("C++");
        titles.add("数据库");
        titles.add("算法");
        titles.add("Android");
        titles.add("Python");

        data=new ArrayList<>();
        data.add(60.0);
        data.add(100.0);
        data.add(45.0);
        data.add(85.0);
        data.add(99.0);
        data.add(66.0);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 * 0.7f;/**设置半径，但是距离两边边界仍有距离*/
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPolygon(canvas);
        drawLines(canvas);
        drawTitle(canvas);
        drawRegion(canvas);
    }

    private void drawRegion(Canvas canvas) {
        valuePaint.setAlpha(255);
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            /**算出这个分值所占的百分比*/
            double perCenter  = data.get(i) / maxValue;
            /**算出这个分值的长度*/
            double perRadius = perCenter * radius;
            float x = (float) (centerX + perRadius * Math.cos(angle * i));
            float y = (float) (centerY + perRadius * Math.sin(angle * i));

            if (i==0){
                path.moveTo(x,y);
            }else{
                path.lineTo(x,y);
            }
            canvas.drawCircle(x,y,10,valuePaint);
        }
        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        /**绘制连线*/
        canvas.drawPath(path,valuePaint);
        valuePaint.setAlpha(128);
        valuePaint.setStyle(Paint.Style.FILL);
        /**绘制填充区域*/
        canvas.drawPath(path,valuePaint);
    }

    private void drawTitle(Canvas canvas) {
        if (count != titles.size()) {
            return;
        }
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float textRadius = radius + fontHeight;
        double pi = Math.PI;
        for (int i = 0; i < count; i++) {
            float x = (float) (centerX + textRadius * Math.cos(angle * i));
            float y = (float) (centerY + textRadius * Math.sin(angle * i));
            //当前绘制标题所在顶点角度
            float degrees = angle * i;
            //从右下角开始顺时针画起,与真实坐标系相反
            if (degrees >= 0 && degrees < pi / 2) {//第四象限
                float dis=textPaint.measureText(titles.get(i))/(titles.get(i).length()-1);
                canvas.drawText(titles.get(i), x+dis, y, textPaint);
            } else if (degrees >= pi / 2 && degrees < pi) {//第三象限
                float dis=textPaint.measureText(titles.get(i))/(titles.get(i).length()-1);
                canvas.drawText(titles.get(i), x-dis, y, textPaint);
            } else if (degrees >= pi && degrees < 3 * pi / 2) {//第二象限
                float dis=textPaint.measureText(titles.get(i))/(titles.get(i).length());
                canvas.drawText(titles.get(i), x-dis, y, textPaint);
            } else if (degrees >= 3 * pi / 2 && degrees <= 2 * pi) {//第一象限
                canvas.drawText(titles.get(i), x, y, textPaint);
            }
        }
    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制正多边形
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / (count - 1);//r是蜘蛛丝之间的间距
        for (int i = 1; i < count; i++) {//中心点不用绘制
            float curR = r * i;//当前半径
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, mainPaint);
        }
    }

    public void setCount(int count) {
        this.count = count;
        postInvalidate();
    }

    public void setMainPaint(Paint paint) {
        this.mainPaint = paint;
        postInvalidate();
    }
    //设置标题颜色
    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }
    //设置覆盖局域颜色
    public void setValuePaint(Paint valuePaint) {
        this.valuePaint = valuePaint;
        postInvalidate();
    }
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
    //设置数值
    public void setData(List<Double> data) {
        this.data = data;
        postInvalidate();
    }

    public List<Double> getData() {
        return data;
    }
    //设置最大数值
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
