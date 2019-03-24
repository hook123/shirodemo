package org.joker.shirodemo.common.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joker.shirodemo.common.utils.JsonUtil;

/**
 *
 * 权限角色
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class URole  implements Serializable{
	private static final long serialVersionUID = 1L;
    private Long id;
    /**角色名称*/
    private String name;
    /**角色类型*/
    private String type;
    //***做 role --> permission 一对多处理
    private List<UPermission> permissions = new LinkedList<UPermission>();

    /**
     * 将类封装成json字符串返回
     * @return
     */
    public String toString() {
        return JsonUtil.toJson(this);
    }
}