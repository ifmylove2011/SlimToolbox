package com.xter.slimtoolbox.component;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类，同样使用了ButterKnife、BasePresenter
 */
public abstract class BaseFragment extends Fragment {

	private Unbinder unbinder;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflate(inflater, container);
		unbinder = ButterKnife.bind(this, rootView);
		initData(rootView,savedInstanceState);
		return rootView;
	}

	/**
	 * 返回fragment的布局视图，由子类覆写
	 *
	 * @param inflater  布局渲染
	 * @param container 父容器
	 * @return view
	 */
	public abstract View inflate(LayoutInflater inflater, ViewGroup container);

	/**
	 * 需要初始化操作的数据
	 * @param savedInstanceState 状态数据
	 */
	public abstract void initData(View rootView,Bundle savedInstanceState);

	/**
	 * {@link BaseFragmentUIHandler }
	 * 用于Handler基类
	 *
	 * @param msg 消息
	 */
	@Deprecated
	protected void uiHandleMessage(Message msg) {
	}

	@Override
	public void onDestroyView() {
		unbinder.unbind();
		super.onDestroyView();
	}

	/**
	 * 如果用到Handler，则继承此类
	 * 不过目前已经很少用到了
	 */
	@Deprecated
	protected static class BaseFragmentUIHandler extends Handler {
		WeakReference<BaseFragment> baseFragmentWeakReference;

		public BaseFragmentUIHandler(BaseFragment baseFragment) {
			baseFragmentWeakReference = new WeakReference<>(baseFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			BaseFragment baseFragment = baseFragmentWeakReference.get();
			baseFragment.uiHandleMessage(msg);
		}

		public void recycle() {
			if (baseFragmentWeakReference != null && baseFragmentWeakReference.get() != null) {
				baseFragmentWeakReference.clear();
				baseFragmentWeakReference = null;
			}
		}
	}
}
