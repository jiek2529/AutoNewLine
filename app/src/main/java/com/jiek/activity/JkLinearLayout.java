package com.jiek.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Hashtable;

public class JkLinearLayout extends LinearLayout {

    private boolean mHasMore = false;//是否是部分显示
    private boolean mFlag = true;//是否是折叠状态
    private int mLinesMarginTop = 15;//每行的PaddingTop
    private int mRawTextViewMarginRight = 15;//item的文字PaddingRight值
    private int mShowMaxLines = 3;//折叠状态最多显示行数

    private int mLeft, mRight, mTop, mBottom;
    private Hashtable map = new Hashtable();

    private int[] mMeaureHights = new int[]{0, 0};//{完整高度，折叠后高度}

    public JkLinearLayout(Context context) {
        super(context);
    }

    public JkLinearLayout(Context context, int horizontalSpacing,
                          int verticalSpacing) {
        super(context);
    }

    public JkLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mCount = getChildCount();
        int mY = 0;
        mLeft = 0;
        mRight = 0;
        mTop = mLinesMarginTop;
        mBottom = 0;

        int frontRowLastItemIndex = 0;//上一行的最后一个ItemIndex

        int lines = 0;
        for (int itemIndex = 0; itemIndex < mCount; itemIndex++) {
            final View child = getChildAt(itemIndex);

            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            mLeft = getPosition(itemIndex - frontRowLastItemIndex, itemIndex);
            mRight = mLeft + child.getMeasuredWidth();
            if (mRight >= mWidth) {
                mY += childHeight;
                frontRowLastItemIndex = itemIndex;
                mLeft = 0;
                mRight = mLeft + child.getMeasuredWidth();
                mTop = mY + mLinesMarginTop;
                lines++;
                if (!mHasMore && mShowMaxLines > 0 && lines >= mShowMaxLines) {//最大行数控制
                    mHasMore = true;
                    mMeaureHights[1] = mBottom + mLinesMarginTop;
                }
            }
            mBottom = mTop + child.getMeasuredHeight();
            mY = mTop;
            Position position = new Position();
            position.left = mLeft;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);
        }
        mBottom += mLinesMarginTop;
        mMeaureHights[0] = mBottom;
        if (mMeaureHights[1] == 0) {
            mMeaureHights[1] = mBottom;
        }
        if (mHasMore) {
            setMeasuredDimension(mWidth, mMeaureHights[mFlag ? 1 : 0]);
        } else {
            setMeasuredDimension(mWidth, mBottom);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        try {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                Position pos = (Position) map.get(child);
                if (pos != null) {
                    child.layout(pos.left, pos.top, pos.right, pos.bottom);
                    Log.e("TAG", i + " ::onLayout: " + pos);
                }
            }
        } catch (Exception e) {
        }
    }

    private class Position {
        int left, top, right, bottom;

        @Override
        public String toString() {
            return "left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom;
        }
    }

    /**
     * 获取当前行的前PaddingLeft值
     *
     * @param indexInRow
     * @param childIndex
     * @return
     */
    private int getPosition(int indexInRow, int childIndex) {
        if (indexInRow > 0) {
            return getPosition(indexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + mRawTextViewMarginRight;
        }
        return getPaddingLeft();
    }

    public void setShowMaxLines(int showMaxLines) {
        mShowMaxLines = showMaxLines;
    }

    /**
     * 是否折叠显示
     *
     * @param flag true:折叠， false:展开
     */
    public void setCollapseFlag(boolean flag) {
        if (mHasMore) {
            mFlag = flag;
            ViewGroup.LayoutParams lp = this.getLayoutParams();
            lp.height = mMeaureHights[mFlag ? 1 : 0];
            setLayoutParams(lp);
            postInvalidate();
        }
    }

    /**
     * 用于外部设置Item的margin的top与right,要用兼容的值传递进来
     *
     * @param top
     * @param right
     */
    public void setYllyMargin(int top, int right) {
        mLinesMarginTop = top;
        mRawTextViewMarginRight = right;
    }
}