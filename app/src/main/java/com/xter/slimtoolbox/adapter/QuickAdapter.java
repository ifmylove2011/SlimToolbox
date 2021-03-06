package com.xter.slimtoolbox.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter 快速使用基类
 * 适用于ListView、GridView
 */
public abstract class QuickAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> data;
	protected int res;

	public QuickAdapter(Context context, int res, List<T> data) {
		this.context = context;
		this.data = new ArrayList<>();
		if (data != null)
			this.data.addAll(data);
		this.res = res;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= data.size()) {
			return null;
		}
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 使用该getItemView方法替换原来的getView方法，需要子类实现
	 */
	public abstract View getItemView(int position, View convertView, ViewHolder holder);

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(res, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return getItemView(position, convertView, holder);
	}

	public void addAll(List<T> elem) {
		data.addAll(elem);
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}

	public void remove(T elem) {
		data.remove(elem);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		data.remove(index);
		notifyDataSetChanged();
	}

	public void replaceAll(List<T> elem) {
		if (elem == null) {
			data.clear();
			notifyDataSetChanged();
			return;
		}
		data.clear();
		data.addAll(elem);
		notifyDataSetChanged();
	}


	public static class ViewHolder {
		private SparseArray<View> views = new SparseArray<View>();
		private View convertView;

		public ViewHolder(View convertView) {
			this.convertView = convertView;
		}

		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int resId) {
			View v = views.get(resId);
			if (null == v) {
				v = convertView.findViewById(resId);
				views.put(resId, v);
			}
			return (T) v;
		}
	}
}