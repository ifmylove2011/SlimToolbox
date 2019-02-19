package com.xter.slimtoolbox.function.alarm;

import java.io.Serializable;

/**
 * Created by XTER on 2019/2/19.
 * 删除进程事件
 */

public class RemoveAppItemEvent implements Serializable {
	public String packageName;

	public RemoveAppItemEvent(String packageName) {
		this.packageName = packageName;
	}
}
