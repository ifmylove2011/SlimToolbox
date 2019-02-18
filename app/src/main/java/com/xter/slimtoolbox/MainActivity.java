package com.xter.slimtoolbox;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xter.slimtoolbox.adapter.QuickItemDecoration;
import com.xter.slimtoolbox.adapter.QuickRecycleAdapter;
import com.xter.slimtoolbox.adapter.function.ToolsAdapter;
import com.xter.slimtoolbox.component.BaseActivity;
import com.xter.slimtoolbox.entity.ToolInfo;
import com.xter.slimtoolbox.fragment.AlarmFragment;
import com.xter.slimtoolbox.util.ActivityUtil;
import com.xter.slimtoolbox.util.L;
import com.xter.slimtoolbox.util.RootCmd;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

	/**
	 * 功能列表
	 */
	RecyclerView rvTools;

	ToolsAdapter toolsAdapter;

	public static final String ALARM = "alarm";

	private static final int CODE_KILLPROCESS = 0;

	@Override
	protected void initView() {
		L.DEBUG = true;
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		rvTools = findViewById(R.id.rv_tools);
		rvTools.addItemDecoration(new QuickItemDecoration(this, QuickItemDecoration.VERTICAL_LIST));
		rvTools.setLayoutManager(new GridLayoutManager(this, 3));
	}

	@Override
	protected void initData() {
		List<ToolInfo> toolInfoList = new ArrayList<>();
		toolInfoList.add(new ToolInfo("定时开关APP或者开关机", "定时开关", R.drawable.alarm_clock));

		toolsAdapter = new ToolsAdapter(this, R.layout.tool_info, toolInfoList);
		toolsAdapter.setOnItemClickLitener(new QuickRecycleAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				selectTool(position);
			}

			@Override
			public void onItemLongClick(View view, int position) {

			}
		});
		rvTools.setAdapter(toolsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean checkPermission(Activity context, String[] perms) {
		return EasyPermissions.hasPermissions(context, perms);
	}

	private void selectTool(int index) {
		switch (index) {
			case 0:
				String[] perms = {Manifest.permission.KILL_BACKGROUND_PROCESSES};
				if (checkPermission(this, perms)&&RootCmd.haveRoot()) {
					AlarmFragment alarmFragment = AlarmFragment.newInstance();
					ActivityUtil.showFragment(getSupportFragmentManager(), R.id.fl_content, alarmFragment, ALARM);
				} else {
					EasyPermissions.requestPermissions(this, "需要杀死后台进程的权限", 0, perms);
				}
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		switch (requestCode){
			case CODE_KILLPROCESS:
				L.w("已获取杀死后台进程权限");
				selectTool(0);
				break;
		}
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		switch (requestCode){
			case CODE_KILLPROCESS:
				L.w("杀死后台进程权限被限制");
				break;
		}
	}
}