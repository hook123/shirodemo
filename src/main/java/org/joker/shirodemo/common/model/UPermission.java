package org.joker.shirodemo.common.model;

import java.io.Serializable;

import lombok.*;
import org.joker.shirodemo.common.utils.JsonUtil;

/**
 * 权限
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UPermission implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	/** 操作的url */
	private String url;
	/** 操作的名称 */
	private String name;

	/**
	 * 将类封装成json字符串返回
	 * @return
	 */
	public String toString() {
		return JsonUtil.toJson(this);
	}
}