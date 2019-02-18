package com.xter.slimtoolbox.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 修改自googlesamples / android-architecture
 * Activity与Fragment的部分工具，可用于管理fragment
 */
public class ActivityUtil {

	public static void showFragment(FragmentManager fragmentManager, int containLayoutId, Fragment to, String tag, boolean addToStack) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (!to.isAdded())
			if (containLayoutId == 0)
				ft.add(to, tag);
			else
				ft.add(containLayoutId, to, tag);
		ft.show(to);
//		ft.replace(containLayoutId,to,tag);
		if (addToStack)
			ft.addToBackStack(tag);
		ft.commitAllowingStateLoss();
	}

	public static void showFragment(FragmentManager fragmentManager, int containLayoutId, Fragment to, String tag) {
		showFragment(fragmentManager, containLayoutId, to, tag, true);
	}

	public static void showFragment(FragmentManager fragmentManager, Fragment to) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (to.isAdded()) {
			ft.show(to);
		}
		ft.commitAllowingStateLoss();
	}

	public static void hideFragment(FragmentManager fragmentManager, Fragment to) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (to.isAdded())
			ft.hide(to);
		ft.commitAllowingStateLoss();
	}

	public static String getTopFragmentTag(FragmentManager fragmentManager) {
		int stackCount = fragmentManager.getBackStackEntryCount();
		if (stackCount > 0) {
			return fragmentManager.getBackStackEntryAt(stackCount - 1).getName();
		}
		return "";
	}

	public static Fragment findFragment(FragmentManager fragmentManager, String tag) {
		return fragmentManager.findFragmentByTag(tag);
	}

	public static boolean isFragmentVisble(FragmentManager fragmentManager, String tag) {
		Fragment fragment = findFragment(fragmentManager, tag);
		return fragment != null && fragment.isVisible();
	}

	public static boolean isFragmentExist(FragmentManager fragmentManager, String tag) {
		return findFragment(fragmentManager, tag) != null;
	}

	public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}

	public static void removeFragment(FragmentManager fragmentManager, String tag) {
		removeFragment(fragmentManager, findFragment(fragmentManager, tag));
	}

	public static void pop(FragmentManager fragmentManager) {
		fragmentManager.popBackStackImmediate(null, 0);
	}

	public static void popAll(FragmentManager fragmentManager) {
		fragmentManager.popBackStackImmediate(null, 1);
	}

	public static boolean isAnyFragmentExisted(FragmentManager fragmentManager) {
		return fragmentManager.getFragments().size() > 0;
	}

	/**
	 * tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
	 * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
	 * 如果tag为null ，flags为1时，弹出回退栈中所有fragment。
	 * 如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该
	 * fragment以上的Fragment，如果是1，弹出该fragment（包括该fragment）以
	 * 上的fragment。
	 *
	 * @param fragmentManager fm
	 * @param tag             tag
	 */
	public static void popTopExcept(FragmentManager fragmentManager, String tag) {
		fragmentManager.popBackStackImmediate(tag, 0);
	}

	public static void popTopInclude(FragmentManager fragmentManager, String tag) {
		fragmentManager.popBackStackImmediate(tag, 1);
	}

	public static void showState(FragmentManager fragmentManager) {
		List<Fragment> fragmentList = fragmentManager.getFragments();
		if (null != fragmentList && fragmentList.size() > 0)
			for (Fragment fragment : fragmentList) {
				if (fragment != null)
					L.w(5, "tag:" + fragment.getTag() + " | " + "isVisible:" + fragment.isVisible() + " | " + "isHidden:" + fragment.isHidden() + " | " + "isInLayout:" + fragment.isInLayout() + " | " + "userVisibleHint:" + fragment.getUserVisibleHint() + " | " + "isRemoving:" + fragment.isRemoving() + " | " + "activity:" + fragment.getActivity().getClass().getSimpleName() + " | " + "stackCount:" + fragmentManager.getBackStackEntryCount() + " | " + "fragmentCount:" + fragmentList.size());
			}
	}

	/**
	 * 判断某一Service是否正在运行
	 *
	 * @param context     上下文
	 * @param serviceName Service的全路径： 包名 + service的类名
	 * @return true 表示正在运行，false 表示没有运行
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
		if (runningServiceInfos.size() <= 0) {
			return false;
		}
		for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
			if (serviceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 杀死进程！
	 * @param context 上下文
	 * @param packageName 包名
	 */
	public static void killProcess(Context context, String packageName) {
		//并没有什么用
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (activityManager != null)
			activityManager.killBackgroundProcesses(packageName);
		//用命令行最稳，但是需要root权限
		RootCmd.execRootCmd("am force-stop "+packageName);
	}


	@Deprecated
	public static void forceStopProgress(Context context, String pkgName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		try {
			Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(am, pkgName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
