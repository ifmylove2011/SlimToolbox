package com.xter.slimtoolbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xter.slimtoolbox.util.ActivityUtil;

/**
 * Created by XTER on 2019/2/18.
 * 要做的事情
 */

public class AlarmKillBroadCast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//通过intent传递过来的包名
		String packageName = intent.getStringExtra("packageName");
		ActivityUtil.killProcess(context,packageName);
	}
}
