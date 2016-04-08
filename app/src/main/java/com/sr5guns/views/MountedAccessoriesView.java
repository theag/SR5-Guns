package com.sr5guns.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sr5guns.items.Arrays;
import com.sr5guns.items.Gun;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class MountedAccessoriesView extends View implements GestureDetector.OnGestureListener {

    private int gunIndex;
    private Rect[] mountRects;
    private float dpToPx;
    private GestureDetector mDetector;
    private OnTapListener listener;

    public MountedAccessoriesView(Context context) {
        super(context);
        init();
    }

    public MountedAccessoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MountedAccessoriesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gunIndex = -1;
        mountRects = new Rect[3];
        dpToPx = getContext().getResources().getDisplayMetrics().densityDpi/160f;
        mDetector = new GestureDetector(getContext(), this);
        listener = null;
    }

    public void setGunIndex(int gunIndex) {
        this.gunIndex = gunIndex;
        invalidate();
    }

    public void setListener(OnTapListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(gunIndex >= 0) {
            canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            canvas.translate(getPaddingLeft(), getPaddingTop());
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingTop() - getPaddingBottom();

            Paint main = new Paint();
            main.setColor(0xFF000000);
            main.setTextSize(dpToPx * 20);
            Paint white = new Paint();
            white.setColor(0xFFFFFFFF);
            Paint permanent = new Paint();
            permanent.setColor(0xFF808080);
            permanent.setTextSize(main.getTextSize());
            int textMargin = Math.round(5 * dpToPx);
            int descent = Math.round(main.getFontMetrics().bottom);
            int textBoxHeight = Math.round(-main.getFontMetrics().top) + descent + 2*textMargin;
            int gunWidth, gunHeight, barrelHeight, gripWidth, xmargin, ymargin;

            barrelHeight = textBoxHeight + 2*textMargin;
            gunWidth = 3*barrelHeight;
            gunHeight = 2*gunWidth/3;
            if(gunWidth > width) {
                gunWidth = width;
                gunHeight = 2*gunWidth/3;
                if(gunHeight + textBoxHeight > height) {
                    gunHeight = height - textBoxHeight;
                    gunWidth = 3*gunHeight/2;
                }
                barrelHeight = gunWidth/3;
            } else if(gunHeight + textBoxHeight > height) {
                gunHeight = height - textBoxHeight;
                gunWidth = 3*gunHeight/2;
                barrelHeight = gunWidth/3;
            }
            gripWidth = barrelHeight;
            xmargin = (width - gunWidth)/2;
            ymargin = (height - (gunHeight + textBoxHeight))/2;

            canvas.drawRect(xmargin, ymargin + textBoxHeight, xmargin + gunWidth, ymargin + textBoxHeight + barrelHeight, main);
            canvas.drawRect(xmargin, ymargin + textBoxHeight, xmargin + gripWidth, ymargin + textBoxHeight + gunHeight, main);

            Gun gun = Arrays.getInstance().guns.get(gunIndex);
            String mount;
            //Top
            if(gun.canHaveTopMount()) {
                mount = gun.getTopMountString();
                if(mountRects[0] == null) {
                    mountRects[0] = new Rect();
                }
                main.getTextBounds(mount, 0, mount.length(), mountRects[0]);
                mountRects[0].bottom += descent + 2*textMargin;
                mountRects[0].right += 2*textMargin;
                mountRects[0].offsetTo(xmargin + gunWidth/2 - mountRects[0].width()/2, ymargin);
                canvas.drawRect(mountRects[0], white);
                if(gun.isTopMountPermanent()) {
                    canvas.drawText(mount, mountRects[0].left + textMargin, mountRects[0].bottom - textMargin - descent, permanent);
                } else {
                    canvas.drawText(mount, mountRects[0].left + textMargin, mountRects[0].bottom - textMargin - descent, main);
                }
            } else {
                mountRects[0] = null;
            }
            //Barrel
            if(gun.canHaveBarrelMount()) {
                mount = gun.getBarrelMountString();
                if(mountRects[1] == null) {
                    mountRects[1] = new Rect();
                }
                main.getTextBounds(mount, 0, mount.length(), mountRects[1]);
                mountRects[1].bottom += descent + 2*textMargin;
                mountRects[1].right += 2*textMargin;
                mountRects[1].offsetTo(xmargin + gunWidth - textMargin - mountRects[1].width(), ymargin + textBoxHeight + barrelHeight/2 - mountRects[1].height()/2);
                canvas.drawRect(mountRects[1], white);
                if(gun.isBarrelMountPermanent()) {
                    canvas.drawText(mount, mountRects[1].left + textMargin, mountRects[1].bottom - textMargin - descent, permanent);
                } else {
                    canvas.drawText(mount, mountRects[1].left + textMargin, mountRects[1].bottom - textMargin - descent, main);
                }
            } else {
                mountRects[0] = null;
            }
            //Under
            if(gun.canHaveUnderMount()) {
                mount = gun.getUnderMountString();
                if(mountRects[2] == null) {
                    mountRects[2] = new Rect();
                }
                main.getTextBounds(mount, 0, mount.length(), mountRects[2]);
                mountRects[2].bottom += descent + 2*textMargin;
                mountRects[2].right += 2*textMargin;
                mountRects[2].offsetTo(xmargin + gripWidth, ymargin + textBoxHeight + barrelHeight);
                canvas.drawRect(mountRects[2], white);
                if(gun.isUnderMountPermanent()) {
                    canvas.drawText(mount, mountRects[2].left + textMargin, mountRects[2].bottom - textMargin - descent, permanent);
                } else {
                    canvas.drawText(mount, mountRects[2].left + textMargin, mountRects[2].bottom - textMargin - descent, main);
                }
            } else {
                mountRects[2] = null;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                Paint main = new Paint();
                main.setTextSize(dpToPx * 20);
                int textMargin = (int)(5*dpToPx);
                Rect bounds = new Rect();
                main.getTextBounds("Tp", 0, 2, bounds);
                float descent = main.getFontMetrics().descent;
                bounds.bottom += descent;
                int textBoxHeight = bounds.height() + 2*textMargin;
                int gunWidth, gunHeight, barrelHeight;

                barrelHeight = textBoxHeight + 2*textMargin;
                gunWidth = 3*barrelHeight;
                gunHeight = 2*gunWidth/3;
                if(gunWidth > width - getPaddingLeft() - getPaddingRight()) {
                    gunWidth = width - getPaddingLeft() - getPaddingRight();
                    gunHeight = 2*gunWidth/3;
                    if(gunHeight + textBoxHeight > height - getPaddingTop() - getPaddingBottom()) {
                        gunHeight = height - getPaddingTop() - getPaddingBottom() - textBoxHeight;
                    }
                } else if(gunHeight + textBoxHeight > height) {
                    gunHeight = height - getPaddingTop() - getPaddingBottom() - textBoxHeight;
                }
                height = gunHeight + textBoxHeight;
            }
            setMeasuredDimension(width, height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(listener != null) {
            byte mount = 4;
            for(Rect r : mountRects) {
                if(r != null && r.contains((int)e.getX(), (int)e.getY())) {
                    listener.onTapAccessory(mount);
                    return true;
                }
                mount /= 2;
            }
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnTapListener {
        void onTapAccessory(byte mount);
    }
}
