package org.joker.shirodemo.core.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.common.utils.SerializeUtil;
import org.joker.shirodemo.core.redis.JedisManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sun.security.pkcs11.Secmod;

import java.util.*;

/**
 * @Author joker
 * @Date 3/26/19 8:15 PM
 * @Description
 */
public class ShiroCache <K, V> implements Cache<K, V> {

    /**
     * 为了不和其他的缓存混淆，采用追加前缀方式以作区分
     */
    private static final String KEK_PREFIX = "shiro-demo-cache:";
    /**
     * Redis 分片(分区)，也可以在配置文件中配置
     */
    private static final int DB_INDEX = 1;


    private String name;

    /**
     * 单例的
     */
    private JedisManager jedisManager;


    public ShiroCache(String name, JedisManager jedisManager) {
        this.name = name;
        this.jedisManager = jedisManager;
    }
    /**
     * 自定义relm中的授权/认证的类名加上授权/认证英文名字
     */
    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 通过key取value
     * @param key
     * @return 值
     * @throws CacheException
     */
    
    @Override
    public V get(K key) throws CacheException {

        byte[] byteKey = buildCacheKey(key);
        byte[] byteValue = new byte[0];
        try {
            byteValue = jedisManager.getValueByKey(DB_INDEX, byteKey);
        } catch (Exception e) {
            LoggerUtils.error(this.getClass(), "##ShiroCache##  get value by cache throw exception",e);
        }
        if (byteValue == null) {
            return null;
        }
        return (V) SerializeUtil.deserialize(byteValue);
    }

    /**
     * 将 键：值 存入缓存
     * @param key
     * @param value
     * @return 以前的value
     * @throws CacheException
     */
    @Override
    public V put(K key, V value) throws CacheException {

        V previos = get(key);
        try {
            jedisManager.saveValueByKey(DB_INDEX, buildCacheKey(key),
                    SerializeUtil.serialize(value), -1);
        } catch (Exception e) {
            LoggerUtils.error(getClass(), "##ShiroCache##  put cache throw exception",e);
        }
        return previos;
    }

    /**
     * 移除key
     * @param key
     * @return 以前的值
     * @throws CacheException
     */
    @Override
    public V remove(K key) throws CacheException {
        V previos = get(key);
        try {
            jedisManager.deleteByKey(DB_INDEX, buildCacheKey(key));
        } catch (Exception e) {
            LoggerUtils.error(getClass(), "##ShiroCache##  remove cache  throw exception",e);
        }
        return previos;
    }

    /**
     * 缓存清理
     *
     * @throws CacheException
     */
    @Override
    public void clear() {

        try {
            jedisManager.clear(DB_INDEX);
        } catch (Exception e) {
            LoggerUtils.error(getClass(),"##ShiroCache##  clear cache  throw exception" , e);
        }
    }

    /**
     * 缓存大小
     *
     * @return
     */
    @Override
    public int size() {
        return jedisManager.size(DB_INDEX);
    }

    /**
     * 取出所有key
     *
     * @return
     */
    @Override
    public Set<K> keys() {
        return jedisManager.keys(DB_INDEX,KEK_PREFIX+"*");
    }

    /**
     * 取出所有value
     *
     * @return
     */
    @Override
    public Collection<V> values() {
        return jedisManager.values(DB_INDEX,KEK_PREFIX+"*");
    }



    /**
     * 获得byte[]型的key
     *
     * @param key
     * @return
     */

    private byte[] buildCacheKey(Object key) {
        if (key instanceof String) {
            String preKey = KEK_PREFIX + getName() + ":" + key;
            return preKey.getBytes();
        } else {
            return SerializeUtil.serialize(key);
        }
    }
}
