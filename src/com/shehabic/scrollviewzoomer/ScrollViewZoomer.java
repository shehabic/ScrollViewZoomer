package com.shehabic.scrollviewzoomer;

import com.nineoldandroids.view.animation.AnimatorProxy;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

public class ScrollViewZoomer extends CustomScrollerScrollView {

	private AttributeSet attrs;
	private Context mContext;
	public int initialScroll = 100;
	public OnScrollCallback callable = null;
	protected ValueAnimator mAnimator;
	protected AnimatorProxy mScrollAnimatorProxy;

	public interface OnEndScrollListener {
		public void onEndScroll();
	}

	private Runnable scrollerTask;
	private int initialPosition;

	private int newCheck = 40;

	private boolean mIsFling;

	public void setCallback(OnScrollCallback callable) {
		this.callable = callable;
	}

	public ScrollViewZoomer(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.attrs = attrs;
		initBounceScrollView();
	}

	public ScrollViewZoomer(Context context) {
		super(context);
		this.mContext = context;
		initBounceScrollView();
	}

	public ScrollViewZoomer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.attrs = attrs;
		this.mContext = context;
		initBounceScrollView();
	}

	@SuppressLint("NewApi")
	private void initBounceScrollView() {
		TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.ScrollViewZoomer, 0, 0);
		try {
			initialScroll = a.getInteger(R.styleable.ScrollViewZoomer_initial_scroll, this.initialScroll);
		} finally {
			a.recycle();
		}
		
		this.setScrollY(initialScroll);
		this.mScrollAnimatorProxy = AnimatorProxy.wrap(this);
		scrollerTask = new Runnable() {
			public void run() {

				int newPosition = getScrollY();
				if (initialPosition - newPosition == 0) {// has stopped
					if (callable != null) {
						callable.call(getScrollY());
						animateViewBackIfNeeded(getScrollY());
					}
				} else {
					initialPosition = getScrollY();
					ScrollViewZoomer.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};
	}

	@Override
	public void fling(int velocityY) {
		if (getScrollY() <= initialScroll) {
			super.fling(0);	
		} else {		
			mIsFling = true;
			super.fling(velocityY);
		}
	}

	public void setMaxYOverscrollDistance(int maxYOverscrollDistance) {
		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		final float density = metrics.density;
		initialScroll = (int) (density * maxYOverscrollDistance);
	}

	public Object initScroller() {
		return new OverScroller(this);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (this.callable != null) {
			callable.call(t);
		}
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		boolean bool = super.onTouchEvent(paramMotionEvent);
		this.mHasBeenTouched = true;
		int action = paramMotionEvent.getAction();
		if (action == MotionEvent.ACTION_OUTSIDE || action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_POINTER_UP) {
			initialPosition = getScrollY();
			this.postDelayed(scrollerTask, newCheck);
		}
		return bool;
	}

	protected void animateViewBackIfNeeded(int scrollY) {
		if (scrollY < initialScroll) {
			mIsFling = false;
			animateScrollY(scrollY, this.initialScroll);
		}
	}

	private boolean mHasBeenTouched = false;

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
		if (!this.mHasBeenTouched) {
			scrollTo(0, this.initialScroll);
		}
	}

	private void animateScrollY(int fromY, int toY) {
		if ((this.mAnimator != null) && (this.mAnimator.isRunning())) {
			return;
		}
		if (fromY == toY) {
			return;
		}

		this.mAnimator = ObjectAnimator.ofInt(this.mScrollAnimatorProxy, "scrollY", new int[] { fromY, toY })
						.setDuration(300L);
		this.mAnimator = ObjectAnimator.ofInt(this, "scrollY", new int[] { fromY, toY }).setDuration(300L);
		this.mAnimator.setInterpolator(new DecelerateInterpolator(2f));
		this.mAnimator.start();
	}

}
