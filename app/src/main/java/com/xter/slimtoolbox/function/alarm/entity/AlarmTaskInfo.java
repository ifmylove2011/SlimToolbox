package com.xter.slimtoolbox.function.alarm.entity;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by XTER on 2019/2/19.
 *定时任务信息
 */

public class AlarmTaskInfo implements Serializable {
	public String appName;
	public Drawable appIcon;
	public String time;
	public String packageName;
	public PendingIntent intent;

	public AlarmTaskInfo(String appName, Drawable appIcon, String time) {
		this.appName = appName;
		this.appIcon = appIcon;
		this.time = time;
	}

	public AlarmTaskInfo(String appName, Drawable appIcon, String time, String packageName) {
		this.appName = appName;
		this.appIcon = appIcon;
		this.time = time;
		this.packageName = packageName;
	}

	public AlarmTaskInfo(String appName, Drawable appIcon, String time, String packageName, PendingIntent intent) {
		this.appName = appName;
		this.appIcon = appIcon;
		this.time = time;
		this.packageName = packageName;
		this.intent = intent;
	}

	@Override
	public String toString() {
		return "AlarmTaskInfo{" +
				"appName='" + appName + '\'' +
				", time='" + time + '\'' +
				'}';
	}
}
