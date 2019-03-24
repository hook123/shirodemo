package org.joker.shirodemo.permission.bo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joker.shirodemo.common.model.UPermission;

import java.io.Serializable;


/**
 * 
 * 权限选择
 * @author zhou-baicheng
 *
 */
@Data
public class UPermissionBo extends UPermission implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 是否勾选
	 */
	private String marker;
	/**
	 * role Id
	 */
	private String roleId;

	public boolean isCheck(){
		return StringUtils.equals(roleId,marker);
	}
	
}
