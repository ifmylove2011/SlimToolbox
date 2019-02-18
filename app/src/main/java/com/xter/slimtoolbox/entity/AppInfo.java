package com.xter.slimtoolbox.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by XTER on 2019/2/18.
 * 应用信息
 */

public class AppInfo implements Serializable {
	/**
	 * 目前暂等同于包名
	 */
	public String processName;
	/**
	 * launcher名字
	 */
	public String appName;
	/**
	 * launcher图标
	 */
	public Drawable drawable;

	public AppInfo(String processName, Drawable drawable) {
		this.processName = processName;
		this.drawable = drawable;
	}

	public AppInfo(String processName, String appName, Drawable drawable) {
		this.processName = processName;
		this.appName = appName;
		this.drawable = drawable;
	}

	@Override
	public String toString() {
		return "AppInfo{" +
				"processName='" + processName + '\'' +
				", appName='" + appName + '\'' +
				'}';
	}
}
