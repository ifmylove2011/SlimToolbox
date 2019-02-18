package com.xter.slimtoolbox.component;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.xter.slimtoolbox.util.L;


/**
 * Created by XTER on 2016/9/19.
 * application其类
 */
public abstract class BaseApp extends Application implements Application.ActivityLifecycleCallbacks {

	private static BaseApp INSTANCE;

	public static BaseApp getInstance() {
		return INSTANCE;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerActivityLifecycleCallbacks(this);

		INSTANCE = this;
		init();
	}

	/**
	 * 应用中的初始化操作，由子类覆写
	 */
	protected abstract void init();

	public static Context getContext() {
		return INSTANCE.getApplicationContext();
	}

	/* ------------------------ActivityLifecycleCallbacks------------------------*/

	@Override
	public void onActivityCreated(Activity activity, Bundle bundle) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivityStarted(Activity activity) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivityResumed(Activity activity) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivityPaused(Activity activity) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivityStopped(Activity activity) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
		L.w(activity.getClass().getSimpleName());
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		L.w(activity.getClass().getSimpleName());
	}
}
