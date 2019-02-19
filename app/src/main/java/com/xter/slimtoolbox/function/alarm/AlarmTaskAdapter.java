package com.xter.slimtoolbox.function.alarm;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xter.slimtoolbox.R;
import com.xter.slimtoolbox.adapter.QuickRecycleAdapter;
import com.xter.slimtoolbox.adapter.ViewHolder;
import com.xter.slimtoolbox.function.alarm.entity.AlarmTaskInfo;

import java.util.Iterator;
import java.util.List;

/**
 * Created by XTER on 2019/2/19.
 * 任务列表适配器
 */

public class AlarmTaskAdapter extends QuickRecycleAdapter<AlarmTaskInfo> {
	public AlarmTaskAdapter(Context context, int res, List<AlarmTaskInfo> data) {
		super(context, res, data);
	}

	@Override
	public void bindView(ViewHolder holder, int position) {
		AlarmTaskInfo info = data.get(position);

		ImageView ivAlarm = holder.getView(R.id.iv_alarm_task_icon);
		TextView tvAlarm = holder.getView(R.id.tv_alarm_task_name);
		TextView tvTime = holder.getView(R.id.tv_alarm_task_time);

		ivAlarm.setImageDrawable(info.appIcon);
		tvAlarm.setText(info.appName);
		tvTime.setText(info.time);
	}

	public void remove(String packageName){
		Iterator<AlarmTaskInfo> infoIterator = data.iterator();
		while(infoIterator.hasNext()){
			if(infoIterator.next().packageName.equals(packageName)){
				infoIterator.remove();
				break;
			}
		}
		notifyDataSetChanged();
	}
}
