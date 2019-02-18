package com.xter.slimtoolbox.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XTER on 2016/9/20.
 * 系统相关
 */
public class SysUtil {

	/**
	 * 得到当前日期
	 *
	 * @return time
	 */
	public static String getDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(d);
	}

	/**
	 * 得到当前时间
	 *
	 * @return time
	 */
	public static String getNow() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdf.format(d);
	}

	/**
	 * 得到当前时间
	 *
	 * @return time
	 */
	public static String getNow2() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		return sdf.format(d);
	}

	/**
	 * 得到转换时间
	 *
	 * @param time 数
	 * @return time
	 */
	public static String getTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdf.format(time);
	}

	/**
	 * 将String转换为utf-8
	 */
	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlUTF8;
	}

	/**
	 * 获得状态栏高度
	 * @param context 上下文
	 * @return int 高度
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 使用反射获得状态栏高度
	 * @param context 上下文
	 * @return  int 高度
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static int getStatusBarHeight2(Context context) {
		int result = 0;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			result = context.getResources().getDimensionPixelSize(height);
		} catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取操作拦高度
	 * @param context 上下文
	 * @return int 高度
	 */
	public static int getActionBarHeight(Context context) {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	/**
	 * 得到系统栏高度
	 * @param context 上下文
	 * @return int 高度
	 */
	public static int getSystemBarHeight(Context context) {
		return getActionBarHeight(context) + getStatusBarHeight(context);
	}

	/**
	 * 得到系统栏（包括状态栏和操作栏）参数
	 * @param context 上下文
	 * @return int 参数
	 */
	public static LinearLayout.LayoutParams getSystemBarParam(Context context) {
		int occupyHeight = getActionBarHeight(context) + getStatusBarHeight(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, occupyHeight);
		return layoutParams;
	}

	/**
	 * 判断系统中是否存在可以启动的相机应用
	 *
	 * @return 存在返回true，不存在返回false
	 */
	public static boolean hasCamera(Context context) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	public static int getLargerScreenSide(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return Math.max(dm.widthPixels, dm.heightPixels);
	}

	public static int getShorterScreenSide(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return Math.min(dm.widthPixels, dm.heightPixels);
	}

	public static int getShorterScreenSideToHight(Context context) {
		return Math.round(getShorterScreenSide(context) * getScreenRatio(context));
	}

	public static float getScreenRatio(Context context) {
		return (float) getShorterScreenSide(context) / getLargerScreenSide(context);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 *
	 * @param dpValue dp单位
	 * @return px单位
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 *
	 * @param pxValue pxValue px单位
	 * @return dp单位
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue sp单位
	 * @return px单位
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue px单位
	 * @return sp单位
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 退出
	 */
	public static void exit() {
		//获取PID
		android.os.Process.killProcess(android.os.Process.myPid());
		//常规java、c#的标准退出法，返回值为0代表正常退出
		System.exit(0);
	}

	/**
	 * 安装apk
	 * @param context 环境
	 * @param filePath 文件路径
	 */
	public static void installApk(Context context,String filePath) {
		// 核心是下面几句代码
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static int getVersionCode(Context context){
		PackageManager packageManager=context.getPackageManager();
		PackageInfo packageInfo;
		int versionCode=0;
		try {
			packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
			versionCode=packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getVersionName(Context context){
		PackageManager packageManager=context.getPackageManager();
		PackageInfo packageInfo;
		String versionName="";
		try {
			packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
			versionName=packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
}
