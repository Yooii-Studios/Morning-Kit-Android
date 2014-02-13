package com.yooiistudios.morningkit.common.shadow;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class RoundShadowRelativeLayout extends RelativeLayout {
	private Paint mShadow;
	private Path mPath;
	private RectF mRect;
	private boolean mIsPressing = false;

    protected boolean mIsTouchedEnabled = true;
	protected int mRoundRectRadius;
	protected int mBlurRadius;
	protected int mSolidColor;
	protected int mPressedColor;
	protected int mShadowColor;
	
	public RoundShadowRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		configPadding();
	}
	
	public RoundShadowRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		configPadding();
	}
	
	private void init(){
		initColor();
		
		mPath = new Path();
		mShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		configShadow();
		checkLayerType();
	}
	
	protected void initColor() {
		setBackgroundColor(Color.TRANSPARENT);
		
		mRoundRectRadius = DPToPixel(getContext(), 10);
		mBlurRadius = DPToPixel(getContext(), 4);
		
		mSolidColor = Color.parseColor("#e7e7e7");
		mPressedColor = Color.parseColor("#ffffff");
		
		mShadowColor = Color.argb(180, 0, 0, 0);
	}
	
	protected void configPadding(){
		//config padding
		int left;
		int top;
		int right;
		int bottom;

        if (getPaddingLeft() < mBlurRadius){
			left = mBlurRadius;
		}
		else{
			left = getPaddingLeft();
		}
		if (getPaddingTop() < mBlurRadius){
			top = mBlurRadius;
		}
		else{
			top = getPaddingTop();
		}
		if (getPaddingRight() < mBlurRadius){
			right = mBlurRadius;
		}
		else{
			right = getPaddingRight();
		}
		if (getPaddingBottom() < mBlurRadius){
			bottom = mBlurRadius;
		}
		else{
			bottom = getPaddingBottom();
		}
//        Log.i("RoundShadowRelativeLayout", "padding: " + left + "/" + top + "/" + right + "/" + bottom);
        super.setPadding(left, top, right, bottom);
		
		invalidate();
	}

    public void setTouchEnabled(boolean isTouchedEnabled) {
        this.mIsTouchedEnabled = isTouchedEnabled;
    }

	public void setSolidAreaColor(int color){
		mSolidColor = color;
		configShadow();
		invalidate();
	}
	public int getSolidAreaColor() {
		return mSolidColor;
	}
	public void setPressedColor(int color){
		mPressedColor = color;
	}
	private void configShadow() {
		mShadow.setShadowLayer(mBlurRadius, 0, 0, mShadowColor);
		mShadow.setColor(mSolidColor);
	}
    public void setBlurRadius(int color) {
        mBlurRadius = color;
        configShadow();
        invalidate();
    }
    public void setRoundRectRadius(int radius) {
        mRoundRectRadius = radius;
    }

    private void configPath() {
        mPath.reset();
        mPath.addRoundRect(mRect,
        		mRoundRectRadius, mRoundRectRadius, Path.Direction.CW);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		int action = event.getAction();

		boolean isInRect = x >= 0 && x <= getWidth() &&
				y >= 0 && y <= getHeight();
		
		if (action == MotionEvent.ACTION_DOWN) {
            //down
            if (mIsTouchedEnabled) {
                applyPressedColor();
            }
        } else if (action == MotionEvent.ACTION_MOVE){
			//move
			if (isInRect){
				if (!mIsPressing) {
                    if (mIsTouchedEnabled) {
                        applyPressedColor();
                    }
                }
            }
			else{
				if (mIsPressing){
					applySolidColor();
				}
			}
		}
		else if (action == MotionEvent.ACTION_UP){
			//up or cancel
			if (mIsPressing){
				applySolidColor();
			}
			mIsPressing = false;
            if (isClickable()) {
                performClick();
            }
		}
		else{
			mShadow.setColor(mSolidColor);
			mIsPressing = false;
			invalidate();
		}

		return true;
	}
	private void applyPressedColor() {
		mShadow.setColor(mPressedColor);
		mIsPressing = true;
		invalidate();
	}
	private void applySolidColor() {
		mShadow.setColor(mSolidColor);
		mIsPressing = false;
		invalidate();
	}
	
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.setPadding(left, top, right, bottom);

		configPadding();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
//		Log.i("", "onLayout : " + getLayoutParams());
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

//		Log.i("RoundShadowRelativeLayout", "onSizeChanged" + getLayoutParams());
        mRect = new RectF(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
		configPath();
	}
//@Override
//protected void dispatchDraw(Canvas canvas) {
//	// TODO Auto-generated method stub
//	canvas.drawPath(mPath, mShadow);
//	super.dispatchDraw(canvas);
//}	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawPath(mPath, mShadow);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void checkLayerType(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        	setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

	protected static int DPToPixel(Context c, float dp){
		int result = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
		if (result < 1)
			result = 1;
		return result;
	}
}
