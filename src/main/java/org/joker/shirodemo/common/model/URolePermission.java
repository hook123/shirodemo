package org.joker.shirodemo.common.model;

import java.io.Serializable;

import lombok.*;
import org.joker.shirodemo.common.utils.JsonUtil;

/**

 * 角色{@link URole}和 权限{@link UPermission}中间表
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class URolePermission  implements Serializable{
	private static final long serialVersionUID = 1L;
	/**{@link URole.id}*/
    private Long rid;
    /**{@link UPermission.id}*/
    private Long pid;

    /**
     * 将类封装成json字符串返回
     * @return
     */
    public String toString() {
        return JsonUtil.toJson(this);
    }
}