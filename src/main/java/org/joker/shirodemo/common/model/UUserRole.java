package org.joker.shirodemo.common.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joker.shirodemo.common.utils.JsonUtil;

/**
 * 用户{@link UUser} 和角色 {@link URole} 中间表
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UUserRole  implements Serializable{
	private static final long serialVersionUID = 1L;
	 /**{@link UUser.id}*/
    private Long uid;
    /**{@link URole.id}*/
    private Long rid;

    /**
     * 将类封装成json字符串返回
     * @return
     */
    public String toString() {
        return JsonUtil.toJson(this);
    }
}