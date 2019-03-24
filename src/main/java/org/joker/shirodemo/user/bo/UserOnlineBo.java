package org.joker.shirodemo.user.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.joker.shirodemo.common.model.UUser;
/**
 * Session  + User Bo
 * @author sojson.com
 *
 */

@Data
public class UserOnlineBo extends UUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Session Id
	private String sessionId;
	//Session Host
	private String host;
	//Session创建时间
	private Date startTime;
	//Session最后交互时间
	private Date lastAccess;
	//Session timeout
	private long timeout;
	//session 是否踢出
	private boolean sessionStatus = Boolean.TRUE;

	public UserOnlineBo(UUser user) {
		super(user.getId(),user.getNickname(),user.getEmail(),user.getPswd(),user.getCreateTime(),user.getLastLoginTime(),user.getStatus());
	}

}
