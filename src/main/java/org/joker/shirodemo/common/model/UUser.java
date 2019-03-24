package org.joker.shirodemo.common.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joker.shirodemo.common.utils.JsonUtil;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UUser implements Serializable{
	private static final long serialVersionUID = 1L;
	//0:禁止登录
	public static final Long _0 = new Long(0);
	//1:有效
	public static final Long _1 = new Long(1);

	private Long id;
	/**昵称*/
    private String nickname;
    /**邮箱 | 登录帐号*/
    private String email;
    /**密码*/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//使密码为只写，不会Json传给前端
    private transient String pswd;
    /**创建时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**最后登录时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    /**1:有效，0:禁止登录*/
    private Long status;
    /**
     * 将类封装成json字符串返回
     * @return
     */
    public String toString() {
        return JsonUtil.toJson(this);
    }
}