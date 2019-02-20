package com.xter.slimtoolbox.function.alarm;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.xter.slimtoolbox.function.alarm.entity.AppInfo;
import com.xter.slimtoolbox.util.L;
import com.xter.slimtoolbox.util.RootCmd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XTER on 2019/2/20.
 * 为获取app相关信息而生
 */

public class AppInfoCatcher {

	/**
	 * 第三方库，拿到所有后台运行应用的信息，目前是7.1以下可用
	 *
	 * @return appInfo
	 */
	public static List<AppInfo> getAppList(PackageManager pm) {
		List<AppInfo> appInfoList = new ArrayList<>();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
			//执行ps命令
			String result = RootCmd.execRootCmd("ps");
			String[] resultProcessInfo = result.split("\r\n");
			//从命令结果中拆出当前用户进程，并抓出包名
			for (String processFullInfo : resultProcessInfo) {
				if (processFullInfo.startsWith("u")) {
					String packageName = getPackageName(processFullInfo);
					if (!TextUtils.isEmpty(packageName)) {
						AppInfo appInfo = getAppInfo(pm, packageName);
						if (appInfo != null && !appInfoList.contains(appInfo)) {
							appInfoList.add(appInfo);
						}
					}
				}
			}
		} else {
			List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
			for (AndroidAppProcess process : processes) {
				AppInfo appInfo = getAppInfo(pm, process.getPackageName());
				if (appInfo != null && !appInfoList.contains(appInfo)) {
					appInfoList.add(appInfo);
				}
			}
		}
		return appInfoList;
	}

	/**
	 * 通过包名拿到信息
	 *
	 * @param packageName 包名
	 * @return appInfo
	 */
	public static AppInfo getAppInfo(PackageManager pm, String packageName) {
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return new AppInfo(packageName, (String) pm.getApplicationLabel(applicationInfo), pm.getApplicationIcon(applicationInfo));
		} catch (PackageManager.NameNotFoundException e) {
			L.w("找不到包名对应的app--" + packageName);
		}
		return null;
	}

	public static String getPackageName(String processFullInfo) {
		int index = processFullInfo.lastIndexOf(" ");
		String packageName = processFullInfo.substring(index + 1, processFullInfo.length());
		//排除子进程
		if (packageName.contains(":")) {
			packageName = packageName.substring(0, packageName.indexOf(":"));
		}
		//排除shell进程
		if ("su".equals(packageName) || packageName.contains("/")) {
			packageName = null;
		}
		return packageName;
	}
}
