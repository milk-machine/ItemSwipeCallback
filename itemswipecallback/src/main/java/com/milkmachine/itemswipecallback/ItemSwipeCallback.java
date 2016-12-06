package com.milkmachine.itemswipecallback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by kefir on 06.12.2016.
 */

public abstract class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = "ItemSwipeCallback";

    private final float mDensity;
    private final float mScaleDensity;
    private Drawable mRightIcon;
    private Drawable mLeftIcon;
    private final int mRightColor;
    private final int mLeftColor;
    private String mRightText;
    private String mLeftText;
    private float mHorizontalTextMarginPx;
    private float mTextOffsetY;
    private float mTextSizePx;
    private Paint mTextPaint = null;
    private float mHorizontalIconMarginPx;
    private float mIconSizePx = -1;

    //Settings
    private int RIGHT_ICON = -1; //use only square vector drawables
    private int LEFT_ICON = -1; //use only square vector drawables
    private int RIGHT_COLOR = android.R.color.white;
    private int LEFT_COLOR = android.R.color.white;
    private int RIGHT_TEXT = -1;
    private int LEFT_TEXT = -1;
    private int TEXT_COLOR = android.R.color.black;
    private float TEXT_SIZE_SP = 15f;
    private float ICON_SIZE_DP = 24f;
    private float HORIZONTAL_MARGIN_ICON_DP = 16f;
    private float HORIZONTAL_MARGIN_TEXT_DP = 48f;
    private float VERTICAL_OFFSET_TEXT_DP = 1f;

    //region ============================== Configurator ==============================

    public static Configurator configure(ItemSwipeCallback itemSwipeCallback) {
        return new Configurator(itemSwipeCallback);
    }

    public static class Configurator {
        private ItemSwipeCallback mSwipeCallback;

        private Configurator(ItemSwipeCallback swipeCallback) {
            mSwipeCallback = swipeCallback;
        }

        public Configurator rightIcon(int resId) {
            mSwipeCallback.setRightIcon(resId);
            return this;
        }

        public Configurator leftIcon(int resId) {
            mSwipeCallback.setLeftIcon(resId);
            return this;
        }

        public Configurator rightColor(int resId) {
            mSwipeCallback.setRightColor(resId);
            return this;
        }

        public Configurator leftColor(int resId) {
            mSwipeCallback.setLeftColor(resId);
            return this;
        }

        public Configurator rightText(int resId) {
            mSwipeCallback.setRightText(resId);
            return this;
        }

        public Configurator leftText(int resId) {
            mSwipeCallback.setLeftText(resId);
            return this;
        }

        public Configurator textColor(int resId) {
            mSwipeCallback.setTextColor(resId);
            return this;
        }

        public Configurator textSizeSp(float sp) {
            mSwipeCallback.setTextSizSp(sp);
            return this;
        }

        public Configurator iconSizeDp(float dp) {
            mSwipeCallback.setIconSizeDp(dp);
            return this;
        }

        public Configurator iconHorizontalMarginDp(float dp) {
            mSwipeCallback.setHorizontalMarginIconDp(dp);
            return this;
        }

        public Configurator textHorizontalMarginDp(float dp) {
            mSwipeCallback.setHorizontalMarginTextDp(dp);
            return this;
        }

        public Configurator textOffsetY(float dp) {
            mSwipeCallback.setVerticalOffsetTextDp(dp);
            return this;
        }

        public ItemSwipeCallback configurate() {
            return mSwipeCallback;
        }
    }

    //endregion

    public ItemSwipeCallback(@NonNull Context context, int swipeDirs) {
        super(0, swipeDirs);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;
        mScaleDensity = metrics.scaledDensity;

        mRightColor = ContextCompat.getColor(context, RIGHT_COLOR);
        mLeftColor = ContextCompat.getColor(context, LEFT_COLOR);

        if (RIGHT_ICON != -1) {
            mRightIcon = AppCompatDrawableManager.get().getDrawable(context, RIGHT_ICON);
        }

        if (LEFT_ICON != -1) {
            mLeftIcon = AppCompatDrawableManager.get().getDrawable(context, LEFT_ICON);
        }

        if (RIGHT_TEXT != -1) {
            mRightText = context.getString(RIGHT_TEXT).toUpperCase();
        }

        if (LEFT_TEXT != -1) {
            mLeftText = context.getString(LEFT_TEXT).toUpperCase();
        }

        if (RIGHT_TEXT != -1 || LEFT_TEXT != -1) {
            mHorizontalTextMarginPx = dpToPx(HORIZONTAL_MARGIN_TEXT_DP);
            mTextOffsetY = dpToPx(VERTICAL_OFFSET_TEXT_DP);
            mTextSizePx = spToPx(TEXT_SIZE_SP);

            mTextPaint = new Paint();
            mTextPaint.setColor(ContextCompat.getColor(context, TEXT_COLOR));
            mTextPaint.setTextSize(mTextSizePx);
            mTextPaint.setAntiAlias(true);
        }

        if (RIGHT_ICON != -1 || LEFT_ICON != -1) {
            mHorizontalIconMarginPx = dpToPx(HORIZONTAL_MARGIN_ICON_DP);
            mIconSizePx = dpToPx(ICON_SIZE_DP);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            float itemHeightPx = (float) itemView.getBottom() - (float) itemView.getTop();

            if (dX > 0) {
                //swipe to right

                //drawing background
                float leftBound = (float) itemView.getLeft();
                float topBound = (float) itemView.getTop();
                float rightBound = dX;
                float bottomBound = (float) itemView.getBottom();
                RectF clipShape = new RectF(leftBound, topBound, rightBound, bottomBound);
                c.clipRect(clipShape);
                c.drawColor(mLeftColor);

                //drawing text
                if (mTextPaint != null) {
                    mTextPaint.setTextAlign(Paint.Align.LEFT);
                    int textX = (int) (itemView.getLeft() + mHorizontalTextMarginPx);
                    int textY = (int) (itemView.getTop() + itemHeightPx / 2 + mTextSizePx / 2 - mTextOffsetY);
                    c.drawText(mLeftText, textX, textY, mTextPaint);
                }


                //drawing icon
                if (mIconSizePx != -1) {
                    int iconLeft = (int) (itemView.getLeft() + mHorizontalIconMarginPx);
                    int iconTop = (int) (itemView.getTop() + itemHeightPx / 2 - mIconSizePx / 2);
                    int iconRight = (int) (itemView.getLeft() + mHorizontalIconMarginPx + mIconSizePx);
                    int iconBottom = (int) (itemView.getBottom() - itemHeightPx / 2 + mIconSizePx / 2);
                    mLeftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    mLeftIcon.draw(c);
                }

            } else {
                //swipe to left

                //drawing background
                float leftBound = (float) itemView.getRight() + dX;
                float topBound = (float) itemView.getTop();
                float rightBound = (float) itemView.getRight();
                float bottomBound = (float) itemView.getBottom();
                RectF clipShape = new RectF(leftBound, topBound, rightBound, bottomBound);
                c.clipRect(clipShape);
                c.drawColor(mRightColor);

                //drawing text
                if (mTextPaint != null) {
                    mTextPaint.setTextAlign(Paint.Align.RIGHT);
                    int textX = (int) (itemView.getRight() - mHorizontalTextMarginPx);
                    int textY = (int) (itemView.getTop() + itemHeightPx / 2 + mTextSizePx / 2 - mTextOffsetY);
                    c.drawText(mRightText, textX, textY, mTextPaint);
                }

                //drawing icon
                if (mIconSizePx != -1) {
                    int iconLeft = (int) (itemView.getRight() - mHorizontalIconMarginPx - mIconSizePx);
                    int iconTop = (int) (itemView.getTop() + itemHeightPx / 2 - mIconSizePx / 2);
                    int iconRight = (int) (itemView.getRight() - mHorizontalIconMarginPx);
                    int iconBottom = (int) (itemView.getBottom() - itemHeightPx / 2 + mIconSizePx / 2);
                    mRightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    mRightIcon.draw(c);
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private float dpToPx(float dp) {
        return dp * mDensity;
    }

    private float spToPx(float sp) {
        return sp * mScaleDensity;
    }

    //region ============================== Getters and Setters ==============================

    private void setRightIcon(int resId) {
        this.RIGHT_ICON = resId;
    }

    private void setLeftIcon(int resId) {
        this.LEFT_ICON = resId;
    }

    private void setRightColor(int resId) {
        this.RIGHT_COLOR = resId;
    }

    private void setLeftColor(int resId) {
        this.LEFT_COLOR = resId;
    }

    private void setRightText(int resId) {
        this.RIGHT_TEXT = resId;
    }

    private void setLeftText(int resId) {
        this.LEFT_TEXT = resId;
    }

    private void setTextColor(int resId) {
        this.TEXT_COLOR = resId;
    }

    private void setTextSizSp(float TEXT_SIZE_SP) {
        this.TEXT_SIZE_SP = TEXT_SIZE_SP;
    }

    private void setIconSizeDp(float ICON_SIZE_DP) {
        this.ICON_SIZE_DP = ICON_SIZE_DP;
    }

    private void setHorizontalMarginIconDp(float HORIZONTAL_MARGIN_ICON_DP) {
        this.HORIZONTAL_MARGIN_ICON_DP = HORIZONTAL_MARGIN_ICON_DP;
    }

    private void setHorizontalMarginTextDp(float HORIZONTAL_MARGIN_TEXT_DP) {
        this.HORIZONTAL_MARGIN_TEXT_DP = HORIZONTAL_MARGIN_TEXT_DP;
    }

    private void setVerticalOffsetTextDp(float VERTICAL_OFFSET_TEXT_DP) {
        this.VERTICAL_OFFSET_TEXT_DP = VERTICAL_OFFSET_TEXT_DP;
    }


    //endregion
}