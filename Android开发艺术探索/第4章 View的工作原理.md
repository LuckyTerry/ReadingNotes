# 第4章 View的工作原理

[TOC]

----------


## 1 初识ViewRoot和DecorView
### 1.1 两者的关系
当Activity被创建完毕后，PhoneWindow中会创建ViewRootImpl对象，，并将ViewRootImpl对象和DecorView建立关联，源码如下：
```
root = new ViewRootImpl(view.getContext(), display);
root.setView(view, wparams, panelParentView);
```
### 1.2 整体流程

```
//入口，依次调用performMeasure、performLayout、performDraw
ViewRootImpl的performTraversals

//测量
ViewRootImpl的performMeasure ->
ViewGroup的measure ->
ViewGroup的onMeasure ->
View的measure ->
View的onMeasure -> end

//布局
ViewRootImpl的performLayout ->
ViewGroup的layout ->
ViewGroup的onLayout ->
View的layout ->
View的onLayout -> end

//绘制
ViewRootImpl的performDraw ->
ViewGroup的draw ->
ViewGroup的onDraw ->
View的draw ->
View的onDraw -> end
```

## 2 理解MeasureSpec

### 2.1 MeasureSpec介绍

MeasureSpec，32位int值，高两位mode，低30位size

```
//通过size和mode整合MeasureSpec
public static int makeMeasureSpec(int size, int mode)

//通过MeasureSpec拆分出mode
public static int getMode(int measureSpec)

//通过MeasureSpec拆分出size
public static int getSize(int measureSpec)
```

mode有三种模式

* UNSPECIFIED 要多大有多大
* EXACTLY 精确大小，对应match_parent 和 dp/px
* AT_MOST 不大于 parentSize，对应 wrap_content

### 2.2 MeasureSpec是如何确定的

MeasureSpec 由父容器 MeasureSpec 及自身 LayoutParam 共同确定，特别的，DecorView 由屏幕大小和自身 LayoutParam 共同确定

看下面ViewRootImpl和ViewGrooup中的两段代码

```
/**
* ViewRootImpl中产生DecorView的MeasureSpec的过程（DecorView是一个FrameLayout，相当于ViewGroup）
* @desiredWindowWidth 窗口宽
* @desiredWindowHeight 窗口高
* @childWidthMeasureSpec 目标MeasureSpec
* @childHeightMeasureSpec 目标MeasureSpec
*/
private boolean measureHierarchy(final View host, final WindowManager.LayoutParams lp,
            final Resources res, final int desiredWindowWidth, final int desiredWindowHeight) {
    ...
    childWidthMeasureSpec = getRootMeasureSpec(desiredWindowWidth, lp.width);
    childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, lp.height);
    performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
    ...
}
private static int getRootMeasureSpec(int windowSize, int rootDimension) {
    int measureSpec;
    switch (rootDimension) {
        case ViewGroup.LayoutParams.MATCH_PARENT:
        // Window can't resize. Force root view to be windowSize.
        measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.EXACTLY);
        break;
    case ViewGroup.LayoutParams.WRAP_CONTENT:
        // Window can resize. Set max size for root view.
        measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.AT_MOST);
        break;
    default:
        // Window wants to be an exact size. Force root view to be that size.
        measureSpec = MeasureSpec.makeMeasureSpec(rootDimension, MeasureSpec.EXACTLY);
        break;
    }
    return measureSpec;
}
```

```
/**
* ViewGroup中产生ViewGroup或View的MeasureSpec的过程
* @parentWidthMeasureSpec 父MeasureSpec
* @parentHeightMeasureSpec 父MeasureSpec
* @childWidthMeasureSpec 目标MeasureSpec
* @childHeightMeasureSpec 目标MeasureSpec
*/
protected void measureChild(View child, int parentWidthMeasureSpec,
    int parentHeightMeasureSpec) {
    final LayoutParams lp = child.getLayoutParams();
    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
        mPaddingLeft + mPaddingRight, lp.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
        mPaddingTop + mPaddingBottom, lp.height);
    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}

public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
    int specMode = MeasureSpec.getMode(spec);
    int specSize = MeasureSpec.getSize(spec);

    int size = Math.max(0, specSize - padding);

    int resultSize = 0;
    int resultMode = 0;

    switch (specMode) {
    // Parent has imposed an exact size on us
    case MeasureSpec.EXACTLY:
        if (childDimension >= 0) {
            resultSize = childDimension;
            resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
            // Child wants to be our size. So be it.
            resultSize = size;
            resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            // Child wants to determine its own size. It can't be
            // bigger than us.
            resultSize = size;
            resultMode = MeasureSpec.AT_MOST;
        }
        break;

    // Parent has imposed a maximum size on us
    case MeasureSpec.AT_MOST:
        if (childDimension >= 0) {
            // Child wants a specific size... so be it
            resultSize = childDimension;
            resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
            // Child wants to be our size, but our size is not fixed.
            // Constrain child to not be bigger than us.
            resultSize = size;
            resultMode = MeasureSpec.AT_MOST;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            // Child wants to determine its own size. It can't be
            // bigger than us.
            resultSize = size;
            resultMode = MeasureSpec.AT_MOST;
        }
        break;

    // Parent asked to see how big we want to be
    case MeasureSpec.UNSPECIFIED:
        if (childDimension >= 0) {
            // Child wants a specific size... let him have it
            resultSize = childDimension;
            resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
            // Child wants to be our size... find out how big it should
            // be
            resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
            resultMode = MeasureSpec.UNSPECIFIED;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            // Child wants to determine its own size.... find out how
            // big it should be
            resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
            resultMode = MeasureSpec.UNSPECIFIED;
        }
        break;
    }
    //noinspection ResourceType
    return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
}
```

## 3 View的工作流程

先看三组API

* View
```
public final void measure(int widthMeasureSpec, int heightMeasureSpec){
    ...
    onMeasure(...);
    ...
}
public void layout(int l, int t, int r, int b){
    ...
    onLayout(...);
    ...
}
public void draw(Canvas canvas){
    ...
    drawBackground(canvas);
    ...
    onDraw(canvas);
    ...
    dispatchDraw(canvas);
    ...
    onDrawForeground(canvas);
    ...
}
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
            getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}
protected void	onLayout(boolean changed, int left, int top, int right, int bottom){
}，这是一个空实现
protected void onDraw(Canvas canvas) {
}，这是一个空实现
```

* ViewGroup extends View
```
protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
    final int size = mChildrenCount;
    final View[] children = mChildren;
    for (int i = 0; i < size; ++i) {
        final View child = children[i];
        if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }
}
public abstract void onLayout(boolean changed, int l, int t, int r, int b);
```

* LinearLayout extends ViewGroup
```
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    ...
}
protected void onLayout(boolean changed, int l, int t, int r, int b){
    ...
}
protected void onDraw(Canvas canvas){
    ...
}
```

### 3.1 measure过程
#### 3.1.1 ViewGroup的measure
#### 3.1.2 ViewGroup的onMeasure
#### 3.1.3 View的measure
#### 3.1.4 View的onMeasure

### 3.2 layout过程
#### 3.2.1 ViewGroup的layout
#### 3.2.2 ViewGroup的onLayout
#### 3.2.3 View的layout
#### 3.2.4 View的onLayout

### 3.3 draw过程
#### 3.3.1 ViewGroup的draw
#### 3.3.2 ViewGroup的onDraw
#### 3.3.3 View的draw
#### 3.3.4 View的onDraw

## 4 自定义View
### 4.1 自定义View的分类
### 4.2 自定义View须知
### 4.3 自定义View示例
### 4.4 自定义View的思想

首先一个常识：ViewGroup继承自View

View的final void measure(int widthMeasureSpec, int heightMeasureSpec)，意味着不能被Override
View的void onMeasure(int widthMeasureSpec, int heightMeasureSpec)，有默认实现
measure调用onMeasure

ViewGroup的measure，继承自View，与View完全相同
ViewGroup的onMeasure，继承自View，与View完全相同，不同的ViewGroup有不同的特性，因此没有且不应该Override。
measure调用onMeasure

LinearLayout的measure，继承自ViewGroup，与View完全相同
LinearLayout的onMeasure，继承自ViewGroup，有自己的特性，因此Override
measure调用onMeasure

api:
View    void layout(int l, int t, int r, int b)

View的layout，根据测量值设置自己四个顶点的坐标，
View的onLayout，没有默认实现，因为没有子View

api:
ViewGroup   
public final void layout(int l, int t, int r, int b)
protected abstract void	onLayout(boolean changed, int l, int t, int r, int b)

ViewGroup的layout，同View的
ViewGroup的onLayout，
