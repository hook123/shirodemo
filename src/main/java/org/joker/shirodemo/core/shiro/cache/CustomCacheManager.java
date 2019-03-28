package org.joker.shirodemo.core.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.joker.shirodemo.core.redis.JedisManager;

/**
 * @Author joker
 * @Date 3/27/19 11:30 PM
 * @Description
 */
public class CustomCacheManager implements CacheManager , Destroyable {

    private JedisManager jedisManager;

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    public JedisManager getJedisManager() {
        return jedisManager;
    }


    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCache<>(s, this.jedisManager);
    }

    @Override
    public void destroy() throws Exception {
        //如果和其他系统，或者应用在一起就不能关闭
        //getJedisManager().getJedis().shutdown();
    }
}
