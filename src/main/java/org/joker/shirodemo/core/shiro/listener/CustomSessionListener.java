package org.joker.shirodemo.core.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.joker.shirodemo.core.shiro.session.ShiroSessionRepository;

/**
 * @Author joker
 * @Date 3/26/19 6:48 PM
 * @Description
 */
public class CustomSessionListener implements SessionListener {

    private ShiroSessionRepository shiroSessionRepository;
    @Override
    public void onStart(Session session) {
        System.out.println(session.getId()+"==>"+"Start");

    }

    @Override
    public void onStop(Session session) {
        System.out.println(session.getId()+"==>"+"Stop");
    }

    @Override
    public void onExpiration(Session session) {
        shiroSessionRepository.deleteSession(session.getId());
    }

    public ShiroSessionRepository getShiroSessionRepository() {
        return shiroSessionRepository;
    }

    public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
        this.shiroSessionRepository = shiroSessionRepository;
    }
}
