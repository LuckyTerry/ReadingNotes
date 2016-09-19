# 第6章 Android绘图机制与处理技巧

## 1 屏幕的尺寸信息

### 1.1 屏幕参数

* 屏幕大小 对角线长度
* 分辨路 720x1280指宽有720个像素点，高有1280个像素点
* PPI 每英寸像素（Pixels Per Inch），又称DPI（Dots Per Inch），对角线像素点除以屏幕大小

### 1.2 系统屏幕密度

| 密度    |  ldpi   |  mdpi   |  hdpi   |  xhdpi   | xxhdpi  |
| --------| :----:  | :----:  | :----:  | :----:   | :----:  |
| 密度值  |   120   |   160   |   240   |   320    |   480   | 
| 比例    |   0.75  |   1     |   1.5   |    2     |    3    |
| 分辨率  | 240x320 | 320x480 | 480x800 | 720x1280 |1080x1920|

### 1.3 独立像素密度dp

160的屏幕上1dp == 1px，换算比例见上表。

### 1.4 单位转换

```
public class DisplayUtil {
    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    
     /**
     * dp转px
     * 通过系统提供的函数
     */
    public static int dp2pxByTypedValue(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    
     /**
     * sp转px
     * 通过系统提供的函数
     */
    public static int sp2pxByTypedValue(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
```

----------


## 2 2D绘图基础


----------


## 3 Android XML绘图

### 3.1 Bitmap

### 3.2 Shape

### 3.3 Layer

### 3.4 Selector


----------


## 4 Android绘图技巧
### 4.1 Canvas
### 4.2 Layer图层


----------


## 5 Android图像处理之色彩特效处理
### 5.1 色彩矩阵分析
### 5.2 Android颜色矩阵——ColorMatrix
### 5.3 常用图像颜色矩阵处理效果
### 5.4 像素点分析
### 5.5 常用图像像素点处理效果


----------


## 6 Android图像处理之图形特效处理
### 6.1 Android变形矩阵——Matrix
### 6.2 像素块分析


----------


## 7 Android图像处理之画笔特效处理
### 7.1 PorterDuffXfermode
### 7.2 Shader
### 7.3 PathEffect


----------


## 8 View之孪生兄弟——SurfaceView
### 8.1 SurfaceView与View的区别
### 8.2 SurfaceView的使用
### 8.3 SurfaceView实例



