package com.xter.slimtoolbox.function.main.entity;

import java.io.Serializable;

/**
 * Created by XTER on 2019/2/18.
 * 工具总览
 */

public class ToolInfo implements Serializable {

	/**
	 * 功能描述
	 */
	public String desc;
	/**
	 * 功能名称
	 */
	public String toolName;
	/**
	 * 功能图标
	 */
	public int resId;
	/**
	 * 外载图标，如果有的话...
	 */
	public String externalIconPath;

	public ToolInfo(String desc, String toolName, int resId) {
		this.desc = desc;
		this.toolName = toolName;
		this.resId = resId;
	}

	public ToolInfo(String desc, String toolName, int resId, String externalIconPath) {
		this.desc = desc;
		this.toolName = toolName;
		this.resId = resId;
		this.externalIconPath = externalIconPath;
	}

	@Override
	public String toString() {
		return "ToolInfo{" +
				"desc='" + desc + '\'' +
				", toolName='" + toolName + '\'' +
				", resId=" + resId +
				", externalIconPath='" + externalIconPath + '\'' +
				'}';
	}
}
