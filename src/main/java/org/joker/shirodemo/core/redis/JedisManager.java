package org.joker.shirodemo.core.redis;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http2.ByteUtil;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;

import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.common.utils.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;


/**
 *
 * Redis Manager Utils
 *
 */
public class JedisManager {

    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = getJedisPool().getResource();
        } catch (JedisConnectionException e) {
        	String message = StringUtils.trim(e.getMessage());
        	if("Could not get a resource from the pool".equalsIgnoreCase(message)){
        		System.out.println("++++++++++请检查你的redis服务++++++++");
        		System.out.println("|①.请检查是否安装redis服务|");
        		System.out.println("|②.请检查redis 服务是否启动。启动口诀[安装目录中的redis-server.exe，双击即可，如果有错误，请用CMD方式启动，怎么启动百度。]|");
        		System.out.println("|③.请检查redis启动是否带配置文件启动，也就是是否有密码，是否端口有变化（默认6379）。解决方案，参考第二点。如果需要配置密码和改变端口，请修改spring-cache.xml配置。|");

        		System.out.println("|PS.如果对Redis表示排斥，请使用Ehcache版本");
        		System.out.println("项目退出中....生产环境中，请删除这些东西。我来自。JedisManage.java line:32");
        		System.exit(0);//停止项目
        	}
        	throw new JedisConnectionException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jedis;
    }

    /**
     * 释放jedis资源，3.0版本已废弃此方法，被{@link Jedis#close()}代替，为兼容所
     * @param jedis
     * @param isBroken
     */
    public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        /**
         * @deprecated starting from Jedis 3.0 this method will not be exposed.
         * Resource cleanup should be done using @see {@link Jedis#close()}
        if (isBroken){
            getJedisPool().returnBrokenResource(jedis);
        }else{
            getJedisPool().returnResource(jedis);
        }
        */
        jedis.close();
    }

    /**
     * 通过key获取value
     * @param dbIndex 数据库索引
     * @param key  键
     * @return
     */
    public byte[] getValueByKey(int dbIndex, byte[] key) {
        Jedis jedis = null;
        byte[] result;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);

        }
        return result;
    }

    /**
     * 删除Key
     * @param dbIndex 数据库索引
     * @param key  要删除的键
     * @throws Exception
     */
    public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        Long result = null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            LoggerUtils.fmtDebug(getClass(), "删除[%s]结果：%s" , new String(key), result);
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 保存键值对
     * @param dbIndex  数据库索引
     * @param key 键
     * @param value 值
     * @param expireTime  过期时间
     */
    public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime) {
        Jedis jedis = null;
        boolean isBroken = false;
        String result=null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);

        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 清理缓存
     * @param dbIndex 数据库索引
     * @return
     */

    public String clear(int dbIndex) throws Exception {
        Jedis jedis=null;
        boolean isBroken = false;
        String result=null;

        try {
            jedis=getJedis();
            jedis.select(dbIndex);
            result = jedis.flushDB();
        } catch (Exception e) {
            throw e;
        }finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }


    /**
     * 取出所有key
     * @param dbIndex 数据库索引
     * @param pattern 匹配字符串，取出所有用 *
     * @return
     */

    public HashSet keys(int dbIndex,String pattern) {
        Jedis jedis = getJedis();
        jedis.select(dbIndex);
        Set<byte[]> keys = jedis.keys(pattern.getBytes());
        HashSet<byte[]> set = new HashSet();
        for (byte[] bs : keys) {
            set.add(bs);
        }
        return  set;
    }

    /**
     * 取出所有value
     * @param dbIndex 数据库索引
     * @param pattern 匹配字符串，取出所有用 *
     * @return
     */
    public List values(int dbIndex,String pattern) {
        Set<byte[]> keys = this.keys(dbIndex,pattern);
        List<Object> values = new ArrayList<Object>();
        for (byte[] key : keys) {
            byte[] bytes =getValueByKey(dbIndex,key);
            values.add(SerializeUtil.deserialize(bytes));
        }
        return values;
    }

    /**
     * 缓存大小
     *
     * @return
     */
    public int size(int dbIndex) {
        Jedis jedis = getJedis();
        jedis.select(dbIndex);
        Long size = jedis.dbSize();
        return size.intValue();
    }


	/**
	 * 获取所有Session
	 * @param dbIndex
	 * @param redisShiroSession
	 * @return
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public Collection<Session> AllSession(int dbIndex, String redisShiroSession) {
//
//		Jedis jedis = null;
//        boolean isBroken = false;
//        Set<Session> sessions = new HashSet<Session>();
//		try {
//            jedis = getJedis();
//            jedis.select(dbIndex);
//
//            Set<byte[]> byteKeys = jedis.keys((JedisShiroSessionRepository.REDIS_SHIRO_ALL).getBytes());
//            if (byteKeys != null && byteKeys.size() > 0) {
//                for (byte[] bs : byteKeys) {
//                	Session obj = SerializeUtil.deserialize(jedis.get(bs),
//                    		 Session.class);
//                     if(obj instanceof Session){
//                    	 sessions.add(obj);
//                     }
//                }
//            }
//        } catch (Exception e) {
//            isBroken = true;
//            throw e;
//        } finally {
//            returnResource(jedis, isBroken);
//        }
//        return sessions;
//	}
}
