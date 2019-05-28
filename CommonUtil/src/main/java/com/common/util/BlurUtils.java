package com.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BlurUtils {
	@SuppressLint("NewApi")
	public static void applyBlur(Activity activity, float scale, float radius) {
		ViewGroup activityDecorView = (ViewGroup) activity.getWindow().getDecorView();
		activityDecorView.setDrawingCacheEnabled(true);
		Bitmap src = activityDecorView.getDrawingCache();
		if (src == null) {
			activityDecorView.destroyDrawingCache();
			activityDecorView.setDrawingCacheEnabled(false);
			return;
		}
		int height = DeviceUtils.getStatusBarHeight(activity);
		Bitmap des = Bitmap.createBitmap(src, 0, height, activityDecorView.getWidth(), activityDecorView.getHeight()
				- height);
		des = doBlur(des, scale, radius);
		View blurView = new View(activity);
		FrameLayout.LayoutParams blurLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		blurLp.topMargin = height;
		blurView.setTag("blur");
		activityDecorView.addView(blurView, blurLp);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			blurView.setBackground(new BitmapDrawable(activity.getResources(), des));
		} else {
			blurView.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), des));
		}
		activityDecorView.destroyDrawingCache();
		activityDecorView.setDrawingCacheEnabled(false);
	}

	public static void cancelBlur(Activity activity) {
		ViewGroup activityDecorView = (ViewGroup) activity.getWindow().getDecorView();
		if (activityDecorView.findViewWithTag("blur") != null) {
			activityDecorView.removeView(activityDecorView.findViewWithTag("blur"));
		}
	}

	public static Bitmap doBlur(Bitmap bkg, float scaleFactor, float radius) {
		try {
			float width = bkg.getWidth() / scaleFactor;
			float height = bkg.getHeight() / scaleFactor;
			Bitmap overlay = Bitmap.createBitmap((int) width, (int) (height), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(overlay);
			canvas.scale(1 / scaleFactor, 1 / scaleFactor);
			Paint paint = new Paint();
			paint.setFlags(Paint.FILTER_BITMAP_FLAG);
			canvas.drawBitmap(bkg, 0, 0, paint);
			canvas.drawARGB(125, 0, 0, 0);
			overlay = BitmapUtil.doBlur(overlay, (int) radius, true);
			return overlay;
		} catch (Throwable t) {
		}
		return null;
	}
}
