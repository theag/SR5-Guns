package com.sr5guns.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private static final float BULLET_WIDTH_DP = 2;
    private static final float BULLET_HORIZONTAL_SPACE_DP = 5;
    private static final float BULLET_VERTICAL_SPACE_DP = 2;
    private static final float BULLET_HEAD_PERCENT = 0.3f;

    private float dpToPx;
    private Clip clip;
    private int count, rowCount;

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
    }

    public void setClip(Clip clip) {
        boolean newLayout = clip.size != this.clip.size;
        this.clip = clip;
        if(newLayout) {
            requestLayout();
        }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                body.setColor(getContext().getColor(R.color.bulletBody));
                head.setColor(getContext().getColor(R.color.bulletHead));
            } else {
                body.setColor(getResources().getColor(R.color.bulletBody));
                head.setColor(getResources().getColor(R.color.bulletHead));
            }
            float bwidth = BULLET_WIDTH_DP*dpToPx;
            float bbody = BULLET_HEIGHT_DP*dpToPx;
            float bhead = BULLET_HEIGHT_DP*BULLET_HEAD_PERCENT*dpToPx;
            int count = 0;
            float x = 0, y = 0;
            for(int row = 0; row < rowCount; row++) {
                for(int col = 0; col < count; col++) {
                    if(count == clip.getBulletCount()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            body.setColor(getContext().getColor(R.color.bulletBodyTrans));
                            head.setColor(getContext().getColor(R.color.bulletHeadTrans));
                        } else {
                            body.setColor(getResources().getColor(R.color.bulletBodyTrans));
                            head.setColor(getResources().getColor(R.color.bulletHeadTrans));
                        }
                    }
                    canvas.drawRect(x, y, x+bwidth, y+bbody, body);
                    canvas.drawRect(x, y, x+bwidth, y+bhead, head);
                    count++;
                    if(count > clip.size)
                        break;
                    x += BULLET_HORIZONTAL_SPACE_DP*dpToPx;
                }
                if(count > clip.size)
                    break;
                y += (BULLET_HEIGHT_DP + BULLET_VERTICAL_SPACE_DP)*dpToPx;
                x = 0;
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
                count = floor(width / ((BULLET_WIDTH_DP + BULLET_HORIZONTAL_SPACE_DP) * dpToPx));
                if(width - count*(BULLET_WIDTH_DP+BULLET_HORIZONTAL_SPACE_DP)*dpToPx > BULLET_WIDTH_DP*dpToPx) {
                    count += 1;
                }
                rowCount = ceil(clip.size*1.0f/count);
                height = ceil((BULLET_HEIGHT_DP*rowCount + BULLET_VERTICAL_SPACE_DP*(rowCount-1))*dpToPx);
            }
            setMeasuredDimension(width+getPaddingLeft()+getPaddingRight(), height+getPaddingTop()+getPaddingBottom());
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

    private int ceil(float v) {
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
