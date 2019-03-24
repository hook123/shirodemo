package org.joker.shirodemo.permission.bo;

import java.io.Serializable;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joker.shirodemo.common.model.URole;

@Data
public class URoleBo extends URole implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID (用String， 考虑多个ID，现在只有一个ID)
	 */
	private String userId;
	/**
	 * 是否勾选
	 */
	private String marker;

	public boolean isCheck(){
		return StringUtils.equals(userId,marker);
	}
	
}
