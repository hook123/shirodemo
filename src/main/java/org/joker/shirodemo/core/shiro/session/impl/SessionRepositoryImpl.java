package org.joker.shirodemo.core.shiro.session.impl;

import org.apache.shiro.session.Session;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.common.utils.SerializeUtil;
import org.joker.shirodemo.core.redis.JedisManager;
import org.joker.shirodemo.core.shiro.session.ISessionRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author joker
 * @Date 3/29/19 11:25 AM
 * @Description
 */
public class SessionRepositoryImpl implements ISessionRepository {

    public static final String REDIS_SHIRO_SESSION = "shiro-demo-session:";
    //这里有个小BUG，因为Redis使用序列化后，Key反序列化回来发现前面有一段乱码，解决的办法是存储缓存不序列化
    private static final int DB_INDEX = 1;

    private JedisManager jedisManager;

    public JedisManager getJedisManager() {
        return jedisManager;
    }

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    @Override
    public void saveSession(Session session) {

        if (session == null || session.getId() == null)
            throw new NullPointerException("session is empty");
        try {
            byte[] key = buildRedisSessionKey(session.getId());

            byte[] value = SerializeUtil.serialize(session);

            /*
            直接使用 (int) (session.getTimeout() / 1000) 的话，session失效和redis的TTL 同时生效
             */
            getJedisManager().saveValueByKey(DB_INDEX, key, value, (int) (session.getTimeout() / 1000));
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(), e, "save session error，id:[%s]",session.getId());
        }
    }

    @Override
    public void deleteSession(Serializable sessionId) {

        if (sessionId == null) {
            throw new NullPointerException("session id is empty");
        }
        try {
            getJedisManager().deleteByKey(DB_INDEX, buildRedisSessionKey(sessionId));
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(), e, "删除session出现异常，id:[%s]",sessionId);
        }
    }

    @Override
    public Session getSession(Serializable sessionId) {
        if (null==sessionId){
            throw new NullPointerException("getSession  session id is empty");
        }
        Session session=null;

        byte[] valueByKey = new byte[0];
        try {
            valueByKey = getJedisManager().getValueByKey(DB_INDEX, buildRedisSessionKey(sessionId));
            session=(Session)SerializeUtil.deserialize(valueByKey);
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(),e,"获取Session异常，id：[%s]",sessionId );
        }
        return session;
    }

    @Override
    public Collection<Session> getAllSessions() {
        List values = null;
        try {
            values = getJedisManager().values(DB_INDEX, REDIS_SHIRO_SESSION + "*");
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(), e, "获取全部session异常");
        }
        return values;
    }

    private byte[] buildRedisSessionKey(Serializable sessionId) {
        String preKey = REDIS_SHIRO_SESSION + sessionId;
        return preKey.getBytes();
    }
}
