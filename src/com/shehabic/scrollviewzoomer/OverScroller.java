package com.shehabic.scrollviewzoomer;

import java.lang.ref.WeakReference;

public class OverScroller extends android.widget.OverScroller {
	private WeakReference<ScrollViewZoomer> mScrollViewRef;

	public OverScroller(
			ScrollViewZoomer paramScrollViewZoomer) {
		super(paramScrollViewZoomer.getContext());
		this.mScrollViewRef = new WeakReference(paramScrollViewZoomer);
	}

	public void fling(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, int paramInt5, int paramInt6, int paramInt7,
			int paramInt8, int paramInt9, int paramInt10) {
		ScrollViewZoomer localHotelDetailsScrollView = (ScrollViewZoomer) this.mScrollViewRef
				.get();
		if (localHotelDetailsScrollView == null)
			return;
		int i = localHotelDetailsScrollView.initialScroll;
		int j = paramInt7;
		int k = paramInt8;
		if ((paramInt2 > i) && (paramInt4 < 0))
			j = Math.max(i, paramInt7);
		while (true) {
			super.fling(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5,
					paramInt6, j, k, 0, 20);
			return;
		}
	}
}