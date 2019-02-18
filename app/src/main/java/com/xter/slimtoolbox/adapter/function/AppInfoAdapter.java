package com.xter.slimtoolbox.adapter.function;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xter.slimtoolbox.R;
import com.xter.slimtoolbox.adapter.QuickAdapter;
import com.xter.slimtoolbox.entity.AppInfo;

import java.util.List;

/**
 * Created by XTER on 2019/2/18.
 * 应用信息
 */

public class AppInfoAdapter extends QuickAdapter<AppInfo> {
	public AppInfoAdapter(Context context, int res, List<AppInfo> data) {
		super(context, res, data);
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		AppInfo appInfo = data.get(position);
		TextView tvName = holder.getView(R.id.tv_app_info);
		ImageView ivApp = holder.getView(R.id.iv_app_info);

		tvName.setText(appInfo.appName);
		ivApp.setImageDrawable(appInfo.drawable);

		return convertView;
	}
}
