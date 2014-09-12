package com.shehabic.scrollviewzoomer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import java.lang.reflect.Field;

public abstract class CustomScrollerScrollView extends ScrollView {
	private Object mCustomScroller;

	public CustomScrollerScrollView(Context paramContext) {
		this(paramContext, null);
	}

	public CustomScrollerScrollView(Context paramContext,
			AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 16842880);
	}

	public CustomScrollerScrollView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		setScroller(initScroller());
	}

	private void setScroller(Object paramObject) {
		try {
			Field localField = ScrollView.class.getDeclaredField("mScroller");
			localField.setAccessible(true);
			localField.set(this, paramObject);
			this.mCustomScroller = paramObject;
			return;
		} catch (Throwable localThrowable) {
			localThrowable.printStackTrace();
		}
	}

	public Object getScroller() {
		if (this.mCustomScroller == null)
			;
		try {
			Field localField = ScrollView.class.getDeclaredField("mScroller");
			localField.setAccessible(true);
			this.mCustomScroller = localField.get(this);
			return this.mCustomScroller;
		} catch (Throwable localThrowable) {
			while (true)
				localThrowable.printStackTrace();
		}
	}

	public abstract Object initScroller();

	public boolean isScrollerFinished() {
		Object localObject = getScroller();
		try {
			boolean bool = ((Boolean) localObject.getClass()
					.getMethod("isFinished", new Class[0])
					.invoke(localObject, new Object[0])).booleanValue();
			return bool;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return true;
	}
}