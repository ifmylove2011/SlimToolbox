package com.xter.slimtoolbox.function.alarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.xter.slimtoolbox.R;
import com.xter.slimtoolbox.adapter.QuickItemDecoration;
import com.xter.slimtoolbox.adapter.QuickRecycleAdapter;
import com.xter.slimtoolbox.component.BaseFragment;
import com.xter.slimtoolbox.function.alarm.entity.AlarmTaskInfo;
import com.xter.slimtoolbox.function.alarm.entity.AppInfo;
import com.xter.slimtoolbox.util.L;
import com.xter.slimtoolbox.util.SysUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by XTER on 2019/2/18.
 * 定时开关设置页面
 */

public class AlarmFragment extends BaseFragment {

	public static AlarmFragment newInstance() {
		Bundle args = new Bundle();
		AlarmFragment fragment = new AlarmFragment();
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * 时间选择器，自带的凑合用
	 */
	TimePicker picker;

	Button btnOk;
	/**
	 * 列表选择框，自带的凑合用
	 */
	Spinner spinner;

	RecyclerView rvAddedTask;

	/**
	 * 各种系统级管理器
	 */
	ActivityManager manager;
	AlarmManager am;
	PackageManager pm;

	AppInfoAdapter appInfoAdapter;
	AlarmTaskAdapter alarmTaskAdapter;

	@Override
	public View inflate(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_alarm, container, false);
	}

	@Override
	public void initData(View rootView, Bundle savedInstanceState) {
		EventBus.getDefault().register(this);

		am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
		manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		pm = getActivity().getPackageManager();

		picker = rootView.findViewById(R.id.tp_alarm);
		picker.setIs24HourView(true);
		picker.setCurrentHour(0);
		picker.setCurrentMinute(0);
		picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				L.d(hourOfDay + "," + minute);
			}
		});

		spinner = rootView.findViewById(R.id.sp_process);
		final List<AppInfo> appInfoList = getAppList();
		appInfoAdapter = new AppInfoAdapter(getActivity(), R.layout.app_info, appInfoList);
		spinner.setAdapter(appInfoAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				L.d(appInfoList.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnOk = rootView.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setAlarm(picker.getCurrentHour(), picker.getCurrentMinute(), appInfoList.get(spinner.getSelectedItemPosition()));
			}
		});

		rvAddedTask = rootView.findViewById(R.id.rv_added_task);
		alarmTaskAdapter = new AlarmTaskAdapter(getActivity(), R.layout.alarm_task_info, null);
		rvAddedTask.addItemDecoration(new QuickItemDecoration(getActivity(), QuickItemDecoration.VERTICAL_LIST));
		rvAddedTask.setLayoutManager(new LinearLayoutManager(getActivity()));
		rvAddedTask.setAdapter(alarmTaskAdapter);
		alarmTaskAdapter.setOnItemClickLitener(new QuickRecycleAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

			}

			@Override
			public void onItemLongClick(View view, int position) {
				//取消任务
				AlarmTaskInfo info = alarmTaskAdapter.getItem(position);
				am.cancel(info.intent);
				alarmTaskAdapter.remove(info);
			}
		});
	}

	/**
	 * 给明时间与目标
	 *
	 * @param hour    时
	 * @param minute  分
	 * @param appInfo 目标信息
	 */
	private void setAlarm(int hour, int minute, AppInfo appInfo) {
		//PendingIntent，否则多个alarm会被覆盖为一个
		Intent intent = new Intent(getActivity(), AlarmKillBroadCast.class);
		intent.putExtra("packageName", appInfo.processName);

		PendingIntent pi = PendingIntent.getBroadcast(getActivity(), appInfo.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);

		am.set(AlarmManager.RTC_WAKEUP,
				c.getTimeInMillis(), pi);

		alarmTaskAdapter.add(new AlarmTaskInfo(appInfo.appName, appInfo.drawable, SysUtil.getTime(c.getTimeInMillis()), appInfo.processName, pi));
	}

	/**
	 * 第三方库，拿到所有后台运行应用的信息，目前是7.1以下可用
	 *
	 * @return appInfo
	 */
	private List<AppInfo> getAppList() {
		List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
		List<AppInfo> appInfoList = new ArrayList<>();

		for (AndroidAppProcess process : processes) {
			AppInfo appInfo = getAppInfo(process.getPackageName());
			if (appInfo != null) {
				appInfoList.add(appInfo);
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
	private AppInfo getAppInfo(String packageName) {
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return new AppInfo(packageName, (String) pm.getApplicationLabel(applicationInfo), pm.getApplicationIcon(applicationInfo));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRemoveItem(RemoveAppItemEvent event) {
		appInfoAdapter.remove(event.packageName);
		alarmTaskAdapter.remove(event.packageName);
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}
}
