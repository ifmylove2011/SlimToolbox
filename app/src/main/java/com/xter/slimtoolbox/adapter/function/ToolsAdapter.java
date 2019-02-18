package com.xter.slimtoolbox.adapter.function;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xter.slimtoolbox.R;
import com.xter.slimtoolbox.adapter.QuickRecycleAdapter;
import com.xter.slimtoolbox.adapter.ViewHolder;
import com.xter.slimtoolbox.entity.ToolInfo;

import java.util.List;

/**
 * Created by XTER on 2019/2/18.
 * 工具箱列表
 */

public class ToolsAdapter extends QuickRecycleAdapter<ToolInfo> {

	public ToolsAdapter(Context context, int res, List<ToolInfo> data) {
		super(context, res, data);
	}

	@Override
	public void bindView(ViewHolder holder, int position) {
		ToolInfo info = data.get(position);
		TextView tvName = holder.getView(R.id.tv_tool_info);
		ImageView ivTool = holder.getView(R.id.iv_tool_info);

		tvName.setText(info.toolName);
		ivTool.setBackgroundResource(info.resId);
	}
}
