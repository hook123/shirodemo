package org.joker.shirodemo.permission.bo;

import lombok.Data;
import org.joker.shirodemo.common.model.UUser;

import java.io.Serializable;


/**
 * 用户角色分配 查询列表BO
 * @author zhou-baicheng
 *
 */
@Data
public class UserRoleAllocationBo extends UUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//Role Name列转行，以,分割
	private String roleNames;
	//Role Id列转行，以‘,’分割
	private String roleIds;
	
}
