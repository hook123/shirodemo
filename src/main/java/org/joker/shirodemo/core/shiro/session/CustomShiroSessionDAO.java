package org.joker.shirodemo.core.shiro.session;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.joker.shirodemo.common.utils.LoggerUtils;

/**
 *
 * Session 持久化操作
 *
 */
public class CustomShiroSessionDAO extends AbstractSessionDAO{


    private ISessionRepository iSessionRepository;

    public ISessionRepository getiSessionRepository() {
        return iSessionRepository;
    }

    public void setiSessionRepository(ISessionRepository iSessionRepository) {
        this.iSessionRepository = iSessionRepository;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {  
        getiSessionRepository().saveSession(session);
    }  
  
    @Override  
    public void delete(Session session) {  
        if (session == null) {  
        	LoggerUtils.error(getClass(), "Session 不能为null");
            return;  
        }  
        Serializable id = session.getId();  
        if (id != null)  
            getiSessionRepository().deleteSession(id);
    }  
  
    @Override  
    public Collection<Session> getActiveSessions() {  
        return getiSessionRepository().getAllSessions();
    }  
  
    @Override  
    protected Serializable doCreate(Session session) {  
        Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);  
        getiSessionRepository().saveSession(session);
        return sessionId;  
    }  

    @Override  
    protected Session doReadSession(Serializable sessionId) {  
        return getiSessionRepository().getSession(sessionId);
    }

}
