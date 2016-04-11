package com.sr5guns.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.sr5guns.R;
import com.sr5guns.items.Clip;

/**
 * Created by nbp184 on 2016/04/08.
 */
public class ClipView extends View {

    private static final float BULLET_HEIGHT_DP = 20;
    private static final float BULLET_WIDTH_DP = 4;
    private static final float BULLET_HORIZONTAL_SPACE_DP = 5;
    private static final float BULLET_VERTICAL_SPACE_DP = 2;
    private static final float BULLET_OUTLINE_SPACE_DP = 2;
    private static final float BULLET_HEAD_PERCENT = 0.3f;

    private float dpToPx;
    private Clip clip;
    private int toFire;
    private int colCount, rowCount;

    public ClipView(Context context) {
        super(context);
        init();
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dpToPx = getContext().getResources().getDisplayMetrics().densityDpi/160f;
        clip = null;
        toFire = 0;
    }

    public void setClip(Clip clip, int toFire) {
        boolean newLayout = this.clip == null || clip.size != this.clip.size;
        this.clip = clip;
        this.toFire = toFire;
        if(newLayout) {
            requestLayout();
        }
        invalidate();
    }

    public void setToFire(int toFire) {
        this.toFire = toFire;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(clip != null) {
            canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            canvas.translate(getPaddingLeft(), getPaddingTop());
            Paint body = new Paint();
            Paint head = new Paint();
            Paint divisor = new Paint();
            Paint outline = new Paint();
            divisor.setStrokeWidth(BULLET_WIDTH_DP*dpToPx/2f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                body.setColor(getContext().getColor(R.color.bulletBody));
                head.setColor(getContext().getColor(R.color.bulletHead));
                divisor.setColor(getContext().getColor(android.R.color.black));
                outline.setColor(getContext().getColor(R.color.bulletFiring));
            } else {
                body.setColor(getResources().getColor(R.color.bulletBody));
                head.setColor(getResources().getColor(R.color.bulletHead));
                divisor.setColor(getResources().getColor(android.R.color.black));
                outline.setColor(getResources().getColor(R.color.bulletFiring));
            }
            float boutline = BULLET_OUTLINE_SPACE_DP*dpToPx;
            float bwidth = BULLET_WIDTH_DP*dpToPx;
            float bbody = BULLET_HEIGHT_DP*dpToPx;
            float bhead = BULLET_HEIGHT_DP*BULLET_HEAD_PERCENT*dpToPx;
            int currentBullet = 1;
            float x = boutline, y = boutline;
            for(int row = 0; row < rowCount; row++) {
                for(int col = 0; col < colCount; col++) {
                    if(currentBullet == clip.getBulletCount() + 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            body.setColor(getContext().getColor(R.color.bulletBodyTrans));
                            head.setColor(getContext().getColor(R.color.bulletHeadTrans));
                        } else {
                            body.setColor(getResources().getColor(R.color.bulletBodyTrans));
                            head.setColor(getResources().getColor(R.color.bulletHeadTrans));
                        }
                    }
                    if(clip.getBulletCount() - currentBullet < toFire && currentBullet <= clip.getBulletCount()) {
                        canvas.drawRect(x-boutline, y-boutline, x+bwidth+boutline, y+bbody+boutline, outline);
                    }
                    canvas.drawRect(x, y, x+bwidth, y+bbody, body);
                    canvas.drawRect(x, y, x+bwidth, y+bhead, head);
                    if(currentBullet%5 == 0 && currentBullet < clip.size) {
                        canvas.drawLine(x + bwidth + BULLET_HORIZONTAL_SPACE_DP * dpToPx / 2f, y, x + bwidth + BULLET_HORIZONTAL_SPACE_DP*dpToPx/2f, y + bbody, divisor);
                    }

                    currentBullet++;
                    if(currentBullet > clip.size)
                        break;
                    x += bwidth + BULLET_HORIZONTAL_SPACE_DP*dpToPx;
                }
                if(currentBullet > clip.size)
                    break;
                y += (BULLET_HEIGHT_DP + BULLET_VERTICAL_SPACE_DP)*dpToPx;
                x = boutline;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = ceil(BULLET_HEIGHT_DP * dpToPx);
            if(clip != null) {
                colCount = floor(width / ((BULLET_WIDTH_DP + BULLET_HORIZONTAL_SPACE_DP) * dpToPx));
                rowCount = ceil(clip.size*1.0f/ colCount);
                height = ceil((BULLET_HEIGHT_DP*rowCount + BULLET_VERTICAL_SPACE_DP*(rowCount-1))*dpToPx);
            }
            setMeasuredDimension(width+ceil(2*BULLET_OUTLINE_SPACE_DP*dpToPx)+getPaddingLeft()+getPaddingRight(), height+ceil(2*BULLET_OUTLINE_SPACE_DP*dpToPx)+getPaddingTop()+getPaddingBottom());
        }
    }

    private int floor(float v) {
        int rv;
        if(v >= 0) {
            for(rv = 0; rv <= v; rv++);
            rv--;
        } else {
            for(rv = 0; rv > v; rv--);
        }
        return rv;
    }

    public static int ceil(float v) {
        int rv;
        if(v >= 0) {
            for(rv = 0; rv < v; rv++);
        } else {
            for(rv = 0; rv >= v; rv--);
            rv++;
        }
        return rv;
    }
}
