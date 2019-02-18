package com.xter.slimtoolbox.component;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类，需注意用到了ButterKnife、BasePresenter
 */
public abstract class BaseActivity extends AppCompatActivity {

	/**
	 * butterknife工具
	 */
	private Unbinder unbinder;

	/**
	 * BackStack监听
	 */
	private FragmentManager.OnBackStackChangedListener backStackChangedListener;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView
		initView();
		//解析视图控件
		unbinder = ButterKnife.bind(this);
		//监听下属Fragment
		backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				onBackStackChangedInBaseActivity();
			}
		};
		getSupportFragmentManager().addOnBackStackChangedListener(backStackChangedListener);
		//初始化其他数据
		initData();
	}

	/**
	 * 用于setContentView，或者requestWindowFeature之类需要在渲染layout之前的方法
	 */
	protected abstract void initView();

	/**
	 * 初始化必要数据
	 */
	protected abstract void initData();

	/**
	 * 监听依附于此Activity的Fragment栈，前提是每个Fragment都进入BackStack
	 * 由子类覆写，基类暂不做额外处理
	 */
	protected void onBackStackChangedInBaseActivity() {
	}

	/**
	 * {@link BaseActivityUIHandler }
	 * 用于Handler基类
	 *
	 * @param msg 消息
	 */
	@Deprecated
	protected void uiHandleMessage(Message msg) {
	}

	@Override
	protected void onDestroy() {
		//解除视图绑定
		unbinder.unbind();
		//移除BackStack的监听
		getSupportFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
		super.onDestroy();
	}

	/**
	 * 如果用到Handler，则继承此类
	 * 不过目前已经很少用到了
	 */
	@Deprecated
	protected static class BaseActivityUIHandler extends Handler {
		WeakReference<BaseActivity> baseActivityWeakReference;

		public BaseActivityUIHandler(BaseActivity baseActivity) {
			baseActivityWeakReference = new WeakReference<>(baseActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			BaseActivity baseActivity = baseActivityWeakReference.get();
			baseActivity.uiHandleMessage(msg);
		}

		public void recycle() {
			if (baseActivityWeakReference != null && baseActivityWeakReference.get() != null) {
				baseActivityWeakReference.clear();
				baseActivityWeakReference = null;
			}
		}
	}
}