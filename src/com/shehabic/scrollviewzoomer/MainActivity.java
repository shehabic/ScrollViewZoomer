package com.shehabic.scrollviewzoomer;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	protected ImageView imv;
	protected RelativeLayout imgHolder;
	protected int imgHeight;
	protected ScrollViewZoomer sv;
	protected FrameLayout gh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sv = (ScrollViewZoomer) findViewById(R.id.scrollable);
		imv = (ImageView) findViewById(R.id.img);
		initImageHeight();
		sv.setCallback(new OnScrollCallback() {			
			@Override
			public void call(int scrollY) {
				getActionBar().setTitle(String.valueOf(scrollY));
				if (scrollY < sv.initialScroll) {
					setImageHeight(scrollY);
				}
			}
		});
		sv.initScroller();
		sv.setVerticalScrollBarEnabled(false);
	}
	
	protected int big = 0;
	
	protected void setImageHeight(int scrollY)
	{
		int height = (int) (Math.ceil(0.8 * (sv.initialScroll - scrollY)));
		
		LayoutParams lp = (LayoutParams) imv.getLayoutParams();
		lp.height  = Math.max(0, imgHeight + height);		
		imv.setLayoutParams(lp);
		adjustScroll(scrollY);
	}
	
	protected void initImageHeight()
	{
		LayoutParams lp = (LayoutParams) imv.getLayoutParams();
		imgHeight = lp.height;
	}
	
	protected void adjustScroll(int scroll)
	{
		imgHolder.setScrollY(- scroll / 2);
	}
}
